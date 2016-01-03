package com.tile.janv.sqllitediary.provider;

import android.content.UriMatcher;

import java.util.HashMap;

/**
 * Created by janv on 23-Nov-15.
 */
public class MyDbConstants  {
    public static final String TAG = "DiaryEntriesProvider";

    public static final String DATABASE_NAME = "diary.db";
    public static final int DATABASE_VERSION = 1;
    public static final String ENTRIES_TABLE_NAME = "entries";

    // all columns
    private HashMap<String, String> sEntriesProjectionMap;

    public static final int ENTRIES = 1;
    public static final int ENTRY_ID = 2;

    private final UriMatcher sUriMatcher;

    public MyDbConstants() {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(Entries.AUTHORITY, "entries", ENTRIES);
        sUriMatcher.addURI(Entries.AUTHORITY, "entries/#", ENTRY_ID);
        sEntriesProjectionMap = new HashMap<String, String>();
        sEntriesProjectionMap.put(Entries.DiaryColumns._ID, Entries.DiaryColumns._ID);
        sEntriesProjectionMap.put(Entries.DiaryColumns.TITLE, Entries.DiaryColumns.TITLE);
        sEntriesProjectionMap.put(Entries.DiaryColumns.ENTRY, Entries.DiaryColumns.ENTRY);
        sEntriesProjectionMap.put(Entries.DiaryColumns.CREATED_DATE, Entries.DiaryColumns.CREATED_DATE);
    }

    public HashMap<String, String> getsEntriesProjectionMap() {
        return sEntriesProjectionMap;
    }

    public UriMatcher getsUriMatcher() {
        return sUriMatcher;
    }
}
