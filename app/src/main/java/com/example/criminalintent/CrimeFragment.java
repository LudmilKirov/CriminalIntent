package com.example.criminalintent;

import android.accessibilityservice.GestureDescription;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URI;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.zip.Inflater;

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 0;
    private static final int REQUEST_CONTACT=1;


    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private Button mReportButton;
    private Button mSuspectButton;
    private CheckBox mSolvedCheckbox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //When a fragment need to access its arguments,
        // it calls the getArguments()
        // and type specific get methods of Bundle
        setHasOptionsMenu(true);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Override
    public void onPause() {
        super.onPause();
        //Update a copy of Crime
        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
       // CrimeLab.get(getActivity()).deleteCrime(mCrime);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //When create run fragment_crime.xml
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        //Set the title
        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Do nothing
            }
        });

        //Create the button for the date
        mDateButton = (Button) v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                FragmentManager manager = CrimeFragment.this.getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });
        //Adding a button to set the time
        mTimeButton = (Button) v.findViewById(R.id.crime_time);
        //TODO update the time method
        //Update the time
        updateTime();
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = CrimeFragment.this.getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });

        mReportButton = (Button) v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v12) {
                Intent i = ShareCompat.IntentBuilder.from(CrimeFragment.this.getActivity())
                        .setType("text/plain")
                        .setText(CrimeFragment.this.getCrimeReport())
                        .setSubject(CrimeFragment.this.getString(R.string.crime_report_subject))
                        .setChooserTitle(CrimeFragment.this.getString(R.string.send_report))
                        .createChooserIntent();
                CrimeFragment.this.startActivity(i);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
       // pickContact.addCategory(Intent.CATEGORY_HOME);
        mSuspectButton=(Button) v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(pickContact,REQUEST_CONTACT);
            }
        });

        if(mCrime.getSuspect() != null){
            mSuspectButton.setText(mCrime.getSuspect());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if(packageManager.resolveActivity(pickContact,PackageManager.MATCH_DEFAULT_ONLY)==null){
            mSuspectButton.setEnabled(false);
        }
        //Create a check box
        mSolvedCheckbox = (CheckBox) v.findViewById(R.id.crime_solved);
        //Set if the case is solved
        mSolvedCheckbox.setChecked(mCrime.isSolved());
        mSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });



        return v;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        //Updating the date
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }
        if (requestCode == REQUEST_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setDate(date);
            updateTime();
        }
        else if(requestCode == REQUEST_CONTACT && data != null){
            Uri contactUri = data.getData();
            //Specify which fields you want your query to return values for.

            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };

            //Perform your query - the contractURi is like a "where" clause here

            Cursor c = getActivity().getContentResolver().query(contactUri,queryFields,null,null
            ,null);

            try{
                //Double-check that you actually got results
                if(c.getCount() == 0){
                    return;
                }

                //Pull out the first column of the first
                // row of data - that is your suspect's name.
                c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            }
            finally {
                c.close();
            }
        }
    }

    //Create the functionality of the remove button
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete:
                CrimeLab.get(getActivity()).deleteCrime(mCrime);
                getActivity().finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    //Update the date with the formatted date
    // Get format date to day,month,year
    private void updateDate() {
        CharSequence s = android.text.format.DateFormat.format("dd/MM/yy", mCrime.getDate());
        mDateButton.setText(s);
    }

    //When Crime Activity need to create a Crime Fragment
    // it will call this method.
    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    //Update the time with the right format 24 hours and minutes
    private void updateTime() {
        CharSequence s = android.text.format.DateFormat.format("kk:mm", mCrime.getDate());
        mTimeButton.setText(s);
    }

    private String getCrimeReport(){
        String solvedString = null;

        if(mCrime.isSolved()){
            solvedString=getString(R.string.crime_report_solved);
        }
        else{
            solvedString=getString(R.string.crime_report_unsolved);
        }

        CharSequence s = android.text.format.DateFormat.format("kk:mm", mCrime.getDate());

        String suspect = mCrime.getSuspect();
        if(suspect == null){
            suspect = getString(R.string.crime_report_no_suspect);
        }
        else{
            suspect = getString(R.string.crime_report_suspect,suspect);
        }

        String report = getString(R.string.crime_report,mCrime.getTitle(),s,solvedString,suspect);

        return report;
    }

}