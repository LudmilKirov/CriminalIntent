package com.example.criminalintent;
import java.util.Date;
import java.util.UUID;

public class Crime {

    private  UUID mID;
    private String mTitle;
    private String mDate;
    private boolean mSolved;

    //Constructor
    public Crime() {
        mID = UUID.randomUUID();
    }

    //Getters and setters
    public UUID getID() {
        return mID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
        mDate=this.mDate;
    }

    public String getDate() {
       mDate = (String) android.text.format.DateFormat.format("dd-mm-yyyy kk:mm", new Date());
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}
