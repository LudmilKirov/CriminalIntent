package com.example.criminalintent;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

public class Crime {
    private UUID mID;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mSuspect;
    private String mNumber;

    //Constructor
    public Crime() {
        this(UUID.randomUUID());
    }

    public Crime(UUID id){
        mID=id;
        mDate=new Date();
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
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public String getSuspectNumber() {
        return mNumber;
    }

    public void setSuspectNumber(String number) {
        mNumber = number;
    }

    public String getPhotoFilename() {
        return "IMG_" + getID().toString() + ".jpg";
    }
}