package com.example.criminalintent;

import android.content.Intent;

import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;

//Extends single fragment activity
public class CrimeListActivity extends SingleFragmentActivity
        implements CrimeListFragment.Callbacks,
        CrimeFragment.Callbacks {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
    @Override
    protected int getLayoutResId(){
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelectred(Crime crime) {
        if(findViewById(R.id.detail_fragment_container)== null){
            Intent intent= CrimePageActivity.newIntent(this,crime.getID());
            startActivity(intent);
        }
        else{
            Fragment newDetail=CrimeFragment.newInstance(crime.getID());

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_fragment_container,newDetail)
                    .commit();
        }
    }

    public void onCrimeUpdated(Crime crime){
        CrimeListFragment listFragment= (
                CrimeListFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}