package com.example.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private int mUpdatedPosition ;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list,
                container, false);

        mCrimeRecyclerView = (RecyclerView) view
                .findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    //Update the RecyclerView
    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }


    //Maintains a reference to a single view
    // - Text View,so te itemView must be TextView,
    // later more responsibilities
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        private Crime mCrime;

        //Add the title text view,date text view
        // and the check box
        public CrimeHolder(View itemView) {
            super(itemView);


            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
        }
        //Using this to reduce the time because calling
        // id's is resourceful and onBind is called more
        // often adn the work is already done
        public void bindCrime(Crime crime) {
            mCrime = crime;
            itemView.setOnClickListener(this);
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }
        //when clicked
        @Override
        public void onClick(View view) {
            //When pressing a list in CrimeListFragment
            // to start instance of CrimePageActivity
            Intent intent = CrimePageActivity.newIntent(getActivity(),mCrime.getID());
            //Get the position of the changed crime
            mUpdatedPosition= this.getAdapterPosition();
            startActivity(intent);
        }
    }

    //With the ViewHolder defined,create the adapter
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        //Called by the RecyclerView when it
        // need a new View to display an item

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            //The view use a layout which contains
            // single TextView,styled to look nice as list
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        //Will bind a ViewHolder's View.
        //Find the right position to update the View
        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if(mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }
        else {
            //Check if the user is touched one of the crimes,
            // then notify the adapter
                mAdapter.notifyItemChanged(mUpdatedPosition);
            }
        }

    }

