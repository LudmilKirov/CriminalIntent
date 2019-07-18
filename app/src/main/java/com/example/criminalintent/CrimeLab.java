package com.example.criminalintent;

import android.content.Context;
import android.provider.FontRequest;

import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//Creat a Singleton Notice that after destroy(),
// the data will be lost,so if want to save a
// data for long time don't use this,but for one
// life it is good way to pass information between controllers
public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private ArrayList<Crime> mCrimes;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();
        //Create 100 crimes in the list
        for (int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0);
            mCrimes.add(crime);
        }
    }
    //Using a object easily can if later
    // using different list to be changed
    public List<Crime> getCrimes() {
        return mCrimes;
    }

    //Get particular crime
    public Crime getCrime(UUID id) {
        for (Crime crime : mCrimes) {
            if (crime.getID().equals(id)) {
                return crime;
            }
        }
        return null;
    }
}