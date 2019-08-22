package com.example.criminalintent;

public class CrimeDbSchema {
    public static final class CrimeTable{
        public static final String NAME="crimes";

        //Inner class that describes the table
        public static final class Cols{
            public static final String UUID = "uidd";
            public static final String TITLE = "title";
            public static final String DATE ="date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";
        }
    }
}
