package com.tile.janv.sqllitediary;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.tile.janv.sqllitediary.provider.Entries;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * see http://developer.android.com/reference/android/app/ListActivity.html
 *
 * see http://developer.android.com/training/load-data-background/setup-loader.html
 */
public class DiaryActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // Identifies a particular Loader being used in this component
    private static final int URL_LOADER = 0;

    private DiaryAdapter diaryAdapter;

    //-----------------
    // lifecycle methods
    //-----------------

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // We'll define a custom screen layout here (the one shown above), but
        // typically, you could just use the standard ListActivity layout.
        setContentView(R.layout.activity_diary_overview);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        getLoaderManager().initLoader(URL_LOADER, null, this);

        diaryAdapter = new DiaryAdapter(this);
        setListAdapter(diaryAdapter);
    }

    //-----------------
    // menu methods
    //-----------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_diary_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_add) {
            startActivity(new Intent(this, EntryAddActivity.class));
            return true;
        }

        if (id == R.id.action_delete_all) {
            deleteAll();
            getLoaderManager().initLoader(URL_LOADER, null, this);
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //-----------------
    // LoaderManager.LoaderCallbacks methods
    //-----------------

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] mProjection = new String[] {
                Entries.DiaryColumns._ID,
                Entries.DiaryColumns.TITLE,
                Entries.DiaryColumns.CREATED_DATE};
        return new CursorLoader(
                this,   // Parent activity context
                Entries.CONTENT_URI,        // Table to query
                mProjection,     // Projection to return
                null,            // No selection clause
                null,            // No selection arguments
                null             // Default sort order
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<DiaryEntry> entryList = new ArrayList<>();
        if (data.moveToFirst()) {
            do{
                DiaryEntry entry = new DiaryEntry();
                entry.id = data.getLong(data.getColumnIndex(Entries.DiaryColumns._ID));
                entry.title = data.getString(data.getColumnIndex(Entries.DiaryColumns.TITLE));
                entry.date = data.getString(data.getColumnIndex(Entries.DiaryColumns.CREATED_DATE));
                entryList.add(entry);
            } while (data.moveToNext());
        }
        diaryAdapter.setEntries(entryList);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    //-----------------
    // other methods
    //-----------------

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        startActivity(EntryDetailActivity.createIntent(this, diaryAdapter.getItemId(position)));
    }

    public void deleteAll() {
        ContentResolver contentResolver = getContentResolver();
        int count = contentResolver.delete(Entries.CONTENT_URI, null, null);
        Toast.makeText(this, String.format("removed %d entries", count), Toast.LENGTH_SHORT);
    }
}
