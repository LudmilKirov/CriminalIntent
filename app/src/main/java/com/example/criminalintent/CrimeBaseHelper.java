package com.example.criminalintent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.criminalintent.CrimeDbSchema.CrimeTable;

//SQl open helper is a class designed to get
// rid of the grunt work of opening database.
public class CrimeBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME="crimeBase.db";

    public CrimeBaseHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }
    //Create the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table "
                        + CrimeTable.NAME
                        + "("
                        + "_id integer primary key autoincrement, "
                        + CrimeTable.Cols.UUID + ", "
                        + CrimeTable.Cols.TITLE + ", "
                        + CrimeTable.Cols.DATE + ", "
                        + CrimeTable.Cols.SOLVED + ", "
                        + CrimeTable.Cols.SUSPECT
                        +")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
