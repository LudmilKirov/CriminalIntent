package com.example.criminalintent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.security.auth.callback.Callback;

class CrimeListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE="subtitle";

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private int mUpdatedPosition;
    private boolean mSubtitleVisible;
    private TextView mNoCrimesTextView;
    private Button mNoCrimesButton;
    private Callbacks mCallbacks;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        //Create a view for the layout fragment_crime_list
        View view = inflater.inflate(R.layout.fragment_crime_list,
                container, false);


        mNoCrimesTextView = (TextView) view.findViewById(R.id.no_crimes_text_view);
        mNoCrimesButton = (Button) view.findViewById(R.id.no_crimes_add_button);
        mNoCrimesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crime crime = new Crime();
                CrimeLab.get(CrimeListFragment.this.getActivity()).addCrime(crime);
                Intent intent = CrimePageActivity.newIntent(CrimeListFragment.this.getActivity(), crime.getID());
                CrimeListFragment.this.startActivity(intent);
            }
        });

        //Get the id
        mCrimeRecyclerView = (RecyclerView) view
                .findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //To save the value of the subtitle if rotate
        if(savedInstanceState != null){
            mSubtitleVisible=savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        updateUI();

        return view;
    }

    //To save when rotation the setSubtitle value
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubtitleVisible);
    }

    //Update the RecyclerView
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    //This populates the Menu instance with the items defined in your file
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        //Trigger a recreation of the action items
        // when the user presses on the Show Subtitle action item
        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    //Once you have handled the MenuItem,you should return true
    // to indicate that no further processing is necessary.
    // The default case calls the superclass implementation
    // if the item ID is not in your implementation.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
               updateUI();
               mCallbacks.onCrimeSelectred(crime);
                return true;
            //Show the crimes,and after add a new update
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    //Tell the fragment manager that your fragment
    // should receive a call to onCreateOptionsMenu
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            mCallbacks= (Callbacks) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks=null;
    }

    //Maintains a reference to a single view
    // - Text View,so te itemView must be TextView,
    // later more responsibilities
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        private Crime mCrime;

        //Add the title text view,date text view
        // and the check box
        public CrimeHolder(View itemView) {
            super(itemView);

            mTitleTextView =
                    (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView =
                    (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox =
                    (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
        }

        //Using this to reduce the time because calling
        // id's is resourceful and onBind is called more
        // often adn the work is already done
        public void bindCrime(Crime crime) {
            mCrime = crime;
            itemView.setOnClickListener(this);
            mTitleTextView.setText(mCrime.getTitle());

            CharSequence s =
                    android.text.format.DateFormat
                            .format("dd/MM/yy kk:mm", mCrime.getDate());
            mDateTextView.setText(s);

            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        //when clicked
        @Override
        public void onClick(View view) {
//            //When pressing a list in CrimeListFragment
//            // to start instance of CrimePageActivity
//            Intent intent = CrimePageActivity.newIntent(getActivity(), mCrime.getID());
//            //Get the position of the changed crime
            mCallbacks.onCrimeSelectred(mCrime);
            mUpdatedPosition = this.getAdapterPosition();
//            startActivity(intent);
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

        public void setCrimes(List<Crime> crimes){
            mCrimes=crimes;
        }
    }

    //Update the user interface
    public void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (crimes.size() == 0) {
            mNoCrimesTextView.setVisibility(View.VISIBLE);
            mNoCrimesButton.setVisibility(View.VISIBLE);
        } else {
            mNoCrimesTextView.setVisibility(View.GONE);
            mNoCrimesButton.setVisibility(View.GONE);
        }
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            //Check if the user is touched one of the crimes,
            // then notify the adapter
            mAdapter.setCrimes(crimes);
            mAdapter.notifyItemChanged(mUpdatedPosition);
        }
        //When press the back button to
        // update the count of the crimes
        updateSubtitle();
    }
    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());

        //Get the count of the crimes
        int crimeCount = crimeLab.getCrimes().size();
        //Using the right format, 1 crime , 2 crimes etc
        @SuppressLint("StringFormatMatches")
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural,crimeCount,crimeCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Objects.requireNonNull(Objects.requireNonNull(activity).getSupportActionBar()).setSubtitle(subtitle);
    }

    //Required interface for hosting activities.

    public interface Callbacks{
        void onCrimeSelectred(Crime crime);
    }


}