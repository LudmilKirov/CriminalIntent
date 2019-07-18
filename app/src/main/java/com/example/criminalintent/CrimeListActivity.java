package com.example.criminalintent;

import androidx.fragment.app.Fragment;

//Extends single fragment activity
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}