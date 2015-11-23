package com.tile.janv.sqllitediary.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by janv on 23-Nov-15.
 */
public class MyDB extends ContentProvider {

    private Constants constants = new Constants();

    private static class Constants {
        private static final String TAG = "DiaryEntriesProvider";

        private static final String DATABASE_NAME = "diary.db";
        private static final int DATABASE_VERSION = 2;
        private static final String ENTRIES_TABLE_NAME = "entries";

        // all columns
        private HashMap<String, String> sEntriesProjectionMap;

        private static final int ENTRIES = 1;
        private static final int ENTRY_ID = 2;

        private final UriMatcher sUriMatcher;

        private Constants() {
            sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
            sUriMatcher.addURI(Diary.AUTHORITY, "notes", ENTRIES);
            sUriMatcher.addURI(Diary.AUTHORITY, "notes/#", ENTRY_ID);
            sEntriesProjectionMap = new HashMap<String, String>();
            sEntriesProjectionMap.put(Diary.DiaryColumns._ID, Diary.DiaryColumns._ID);
            sEntriesProjectionMap.put(Diary.DiaryColumns.TITLE, Diary.DiaryColumns.TITLE);
            sEntriesProjectionMap.put(Diary.DiaryColumns.ENTRY, Diary.DiaryColumns.NOTE);
            sEntriesProjectionMap.put(Diary.DiaryColumns.CREATED_DATE, Diary.DiaryColumns.CREATED_DATE);
        }

        public HashMap<String, String> getsEntriesProjectionMap() {
            return sEntriesProjectionMap;
        }

        public UriMatcher getsUriMatcher() {
            return sUriMatcher;
        }
    }

    /**
     * @see https://android.googlesource.com/platform/development/+/05523fb0b48280a5364908b00768ec71edb847a2/samples/NotePad/src/com/example/android/notepad/NotePadProvider.java
     */
    private static class MyDBHelper extends SQLiteOpenHelper {

        public MyDBHelper(Context context) {
            super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + Constants.ENTRIES_TABLE_NAME + " ("
                    + Diary.DiaryColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                    + Diary.DiaryColumns.TITLE + " TEXT NOT NULL,"
                    + Diary.DiaryColumns.ENTRY + " TEXT NOT NULL,"
                    + Diary.DiaryColumns.CREATED_DATE + " LONG"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(Constants.TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + Constants.ENTRIES_TABLE_NAME);
            onCreate(db);
        }
    }

    /**
     * Db management section.
     * @see http://www.dmytrodanylyk.com/pages/blog/concurrent-database.html
     */
    private static class MyDbManager {

        private AtomicInteger mOpenCounter = new AtomicInteger();

        private static MyDbManager instance;
        private static MyDBHelper mDatabaseHelper;

        private SQLiteDatabase mDatabase;

        public MyDbManager() {}

        public static synchronized void initializeInstance(MyDBHelper helper) {
            if (instance == null) {
                instance = new MyDbManager();
                mDatabaseHelper = helper;
            }
        }

        public static synchronized MyDbManager getInstance() {
            if (instance == null) {
                throw new IllegalStateException(MyDbManager.class.getSimpleName() +
                        " is not initialized, call initializeInstance(..) method first.");
            }

            return instance;
        }

        public synchronized SQLiteDatabase openDatabase() {
            if(mOpenCounter.incrementAndGet() == 1) {
                // Opening new database
                //TODO: try writable, otherwise readable
                mDatabase = mDatabaseHelper.getWritableDatabase();
            }
            return mDatabase;
        }

        public synchronized void closeDatabase() {
            if(mOpenCounter.decrementAndGet() == 0) {
                // Closing database
                mDatabase.close();

            }
        }
    }

    //main methods

    public void open(Context context) {
        MyDbManager.initializeInstance(new MyDBHelper(context));
        MyDbManager.getInstance().openDatabase();
    }

    public void close() {
        MyDbManager.getInstance().closeDatabase();
    }

    public DiaryEntry insertDiary(String title, String entry) {
        return null;
    }

    public List<DiaryEntry> getDiaries() throws Exception {
        String[] mSelectionArgs = {""};
        String[] mProjection =
        {
                Diary.DiaryColumns._ID,
                Diary.DiaryColumns.TITLE,
                Diary.DiaryColumns.ENTRY,
                Diary.DiaryColumns.CREATED_DATE
        };


        Cursor cursor = query(Diary.DiaryColumns.CONTENT_URI, mProjection, null, mSelectionArgs, null);
        if (cursor == null) {
            throw new Exception("something went wrong");
        }
        List<DiaryEntry> diaryEntryList = new ArrayList<>();
        return diaryEntryList;
    }

    @Override
    public boolean onCreate() {
        open(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(Constants.ENTRIES_TABLE_NAME);
        switch (constants.getsUriMatcher().match(uri)) {
            case Constants.ENTRIES:
                qb.setProjectionMap(constants.getsEntriesProjectionMap());
                break;
            case Constants.ENTRY_ID:
                qb.setProjectionMap(constants.getsEntriesProjectionMap());
                qb.appendWhere(Diary.DiaryColumns._ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        // If no sort order is specified use the default
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = Diary.DiaryColumns.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }

        // Get the database and run the query
        SQLiteDatabase db = MyDbManager.getInstance().openDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
        // Tell the cursor what uri to watch, so it knows when its source data changes
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (constants.getsUriMatcher().match(uri)) {
            case Constants.ENTRIES:
                return Diary.DiaryColumns.CONTENT_TYPE;
            case Constants.ENTRY_ID:
                return Diary.DiaryColumns.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

}
