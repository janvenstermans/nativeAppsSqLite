package com.tile.janv.sqllitediary.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by janv on 23-Nov-15.
 * @see https://android.googlesource.com/platform/development/+/05523fb0b48280a5364908b00768ec71edb847a2/samples/NotePad/src/com/example/android/notepad/NotePadProvider.java
 */
public class MyDbHelper extends SQLiteOpenHelper {

    public MyDbHelper(Context context) {
        super(context, MyDbConstants.DATABASE_NAME, null, MyDbConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MyDbConstants.ENTRIES_TABLE_NAME + " ("
                + Entries.DiaryColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Entries.DiaryColumns.TITLE + " TEXT NOT NULL,"
                + Entries.DiaryColumns.ENTRY + " TEXT NOT NULL,"
                + Entries.DiaryColumns.CREATED_DATE + " LONG"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MyDbConstants.TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + MyDbConstants.ENTRIES_TABLE_NAME);
        onCreate(db);
    }

}
