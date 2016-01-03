package com.tile.janv.sqllitediary.provider;

import android.database.sqlite.SQLiteDatabase;

/**
 * Db management section.
 * @see http://www.dmytrodanylyk.com/pages/blog/concurrent-database.html
 */
public class MyDbManager  {

//    private AtomicInteger mOpenCounter = new AtomicInteger();

    private static MyDbManager instance;
    private static MyDbHelper mDatabaseHelper;

    private SQLiteDatabase mDatabaseReadable;
//    private SQLiteDatabase mDatabaseWritable;

    private MyDbManager() {}

    //-----------------------------------------------------
    // static methods to construct and/or retrieve instance
    //-----------------------------------------------------

    public static synchronized void initializeInstance(MyDbHelper helper) {
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

    //-----------------------------------------------------
    // main methods, to open and close the database
    //-----------------------------------------------------

    /**
     * Make sure there is only one readable session to the db.
     * @return
     */
    public SQLiteDatabase getDatabase() {
        if (mDatabaseReadable == null) {
            mDatabaseReadable = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabaseReadable;
    }

    public void closeDatabase() {
        if (mDatabaseReadable != null) {
            mDatabaseReadable.close();
            mDatabaseReadable = null;
        }
    }

   /* public synchronized SQLiteDatabase openDatabase() {
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
    }*/
}
