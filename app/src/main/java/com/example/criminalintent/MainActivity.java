package com.example.criminalintent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class MainActivity extends SingleFragmentActivity {

   @Override
    protected  Fragment createFragment(){
       return new CrimeFragment();
    }
}
