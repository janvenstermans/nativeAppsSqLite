package com.tile.janv.sqllitediary.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Extension of {@link ContentProvider} for diary entries.
 */
public class MyDb extends ContentProvider {

    private MyDbConstants constants = new MyDbConstants();

    @Override
    public boolean onCreate() {
        open(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(MyDbConstants.ENTRIES_TABLE_NAME);
        switch (constants.getsUriMatcher().match(uri)) {
            case MyDbConstants.ENTRIES:
                qb.setProjectionMap(constants.getsEntriesProjectionMap());
                break;
            case MyDbConstants.ENTRY_ID:
                qb.setProjectionMap(constants.getsEntriesProjectionMap());
                qb.appendWhere(Entries.DiaryColumns._ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        // If no sort order is specified use the default
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = Entries.DiaryColumns.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }

        // Get the database and run the query
        SQLiteDatabase db = MyDbManager.getInstance().getDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
        // Tell the cursor what uri to watch, so it knows when its source data changes
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (constants.getsUriMatcher().match(uri)) {
            case MyDbConstants.ENTRIES:
                return Entries.CONTENT_TYPE;
            case MyDbConstants.ENTRY_ID:
                return Entries.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    /**
     * see http://www.tutorialspoint.com/android/android_content_providers.htm
     * @param uri
     * @param values
     * @return
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID =  MyDbManager.getInstance().getDatabase().insert(MyDbConstants.ENTRIES_TABLE_NAME, "", values);

        if (rowID > 0)
        {
            Uri createdUri = ContentUris.withAppendedId(Entries.CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(createdUri, null);
            return createdUri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    // http://www.tutorialspoint.com/android/android_content_providers.htm
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        SQLiteDatabase db = MyDbManager.getInstance().getDatabase();
        switch (constants.getsUriMatcher().match(uri)){
            case MyDbConstants.ENTRIES:
                count = db.delete(MyDbConstants.ENTRIES_TABLE_NAME, selection, selectionArgs);
                break;

            case MyDbConstants.ENTRY_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(MyDbConstants.ENTRIES_TABLE_NAME, Entries.DiaryColumns._ID +  " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    //private methods

    private void open(Context context) {
        MyDbManager.initializeInstance(new MyDbHelper(context));
        MyDbManager.getInstance().getDatabase();
    }

    private void close() {
        MyDbManager.getInstance().closeDatabase();
    }
}
