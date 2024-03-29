package com.example.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {

    public static final String EXTRA_DATE = "com.example.criminalintent";
    private static final String ARG_DATE = "date";
    private DatePicker mDatePicker;
    private Button mOkButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // create a Calendar object and use the Date to configure
        // the Calendar.Then initialize the DatePicker.
        Date date = (Date) getArguments().getSerializable(ARG_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View v = inflater.inflate(R.layout.dialog_date, container, false);
        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);
        mDatePicker.init(year, month, day, null);
        //Add a ok button for the date picker
        mOkButton = (Button) v.findViewById(R.id.ok_button);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                //When clicked add the date
                int year1 = mDatePicker.getYear();
                int month1 = mDatePicker.getMonth();
                int day1 = mDatePicker.getDayOfMonth();
                Date date1 = new GregorianCalendar(year1, month1, day1).getTime();
                DatePickerFragment.this.sendResult(Activity.RESULT_OK, date1);
                if (DatePickerFragment.this.getTargetFragment() != null) {
                    DatePickerFragment.this.dismiss();
                } else {
                    DatePickerFragment.this.getActivity().finish();
                }
            }
        });
        return v;
    }

    //Create an intent,puts the date on it as an extra,
    // and then calls CrimeFragment.onActivityResult()
    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    //To get data into your DatePickerFragment,you are going
    // to stash the date in DatePickerFragment's bundle,
    // where the DatePickerFragment can access it
    // .Creating and setting fragment argument is
    // typically done in a newInstance method that replaces fragment constructor.
    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
}