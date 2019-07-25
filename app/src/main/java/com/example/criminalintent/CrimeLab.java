package com.example.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.FontRequest;

import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.criminalintent.CrimeDbSchema.CrimeTable;


//Creat a Singleton Notice that after destroy(),
// the data will be lost,so if want to save a
// data for long time don't use this,but for one
// life it is good way to pass information between controllers
public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    //Add crimes to the list
    public void addCrime(Crime c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeTable.NAME, null, values);

    }

    private CrimeLab(Context context) {

        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();

    }

    //Using a object easily can if later
    // using different list to be changed
    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try {
            //Start with first one and isAfterLast tells
            // that the pointer is off the end of the datasheet
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            //Close the cursor
            cursor.close();
        }
        return crimes;
    }

    //Get particular crime
    public Crime getCrime(UUID id) {

        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + "= ?", new String[]{id.toString()});
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    public void updateCrime(Crime crime) {
        String uuidString = crime.getID().toString();
        ContentValues values = getContentValues(crime);

        //If you put the string directly not using
        // ? and String [],String itself might itself
        // contain SQL code.It is called SQL injection attack.
        //If you use ? it will treat the string as a
        // value not code.So always use ?
        mDatabase.update(CrimeTable.NAME, values,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    // Delete a crime
    public void deleteCrime(Crime crime) {
        String uuidString = crime.getID().toString();
        //Using ? to prevent sql injection
        mDatabase.delete(CrimeTable.NAME,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    //To put in the database
    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getID().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DAVE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);

        return values;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null,//Columns - null selects all columns
                whereClause,
                whereArgs,
                null,//groupBy
                null,//having
                null//orderBy
        );
        return new CrimeCursorWrapper(cursor);
    }

}