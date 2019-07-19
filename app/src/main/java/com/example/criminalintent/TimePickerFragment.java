package com.example.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment {
    public static final String EXTRA_TIME = "com.example.criminalintent";
    public static final String ARG_TIME = "time";

    private TimePicker mTimePicker;
    private Date mDate;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    //DatePickerFragment needs to initialize the DatePicker
    // using the information held in the Date.It requires
    // integers for the month,date and year.So you need to
    // create a Calendar object and use the Date to configure
    // the Calendar.Then initialize the DatePicker.
    public Dialog onCreateDialog( Bundle savedInstanceState) {
        mDate = (Date) getArguments().getSerializable(ARG_TIME);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_time, null);

        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_picker);
        mTimePicker.setHour(hour);
        mTimePicker.setMinute(minute);

        //AlertDialog.Builder provides a fluent interface for
        // constructing an AlertDialog instance.First pass a
        // Context which return an instance if AlertDialog.
        // Builder and call setTitle and and positive button
        return new AlertDialog
                .Builder(getActivity())
                .setView(v)
                .setTitle(R.string.time_picker_title)
                //When pres the positive button in the dialog you have to retrieve
                // the date form the DatePicker and send the result back to CrimeFragment.
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int hour1 = mTimePicker.getHour();
                        int minute1 = mTimePicker.getMinute();

                        Calendar calendar = new GregorianCalendar();
                        calendar.setTime(mDate);
                        calendar.set(Calendar.HOUR, hour1);
                        calendar.set(Calendar.MINUTE, minute1);

                        sendResult(CrimeListActivity.RESULT_OK, calendar.getTime());
                    }
                }).create();
    }

    //To get data into your DatePickerFragment,you are going
    // to stash the date in DatePickerFragment's bundle,
    // where the DatePickerFragment can access it
    // .Creating and setting fragment argument is
    // typically done in a newInstance method that replaces fragment constructor.
    public static TimePickerFragment newInstance(Date time) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, time);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //Create an intent,puts the date on it as an extra,
    // and then calls CrimeFragment.onActivityResult()
    private void sendResult(int resultCode, Date time) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, time);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}
