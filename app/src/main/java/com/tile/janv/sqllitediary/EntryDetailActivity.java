package com.tile.janv.sqllitediary;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.tile.janv.sqllitediary.provider.Entries;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EntryDetailActivity extends Activity {

    private static final String ENTRY_ID = "diaryEntryId";

    @Bind(R.id.entry_detail_title)
    protected TextView titleText;

    @Bind(R.id.entry_detail_date)
    protected TextView dateText;

    @Bind(R.id.entry_detail_content)
    protected TextView contentText;

    public static Intent createIntent(Context context, long entryId) {
        Intent intent = new Intent(context, EntryDetailActivity.class);
        intent.putExtra(ENTRY_ID, entryId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_detail);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras == null || !extras.containsKey(ENTRY_ID)) {
            throw new IllegalArgumentException("activity needs an entry id");
        }
        long entryId = extras.getLong(ENTRY_ID);
        loadEntry(entryId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_entry_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadEntry(long entryId) {
        ContentResolver contentResolver = getContentResolver();
        String[] projection = new String[]{Entries.DiaryColumns.TITLE,
                Entries.DiaryColumns.CREATED_DATE, Entries.DiaryColumns.ENTRY,};
        Cursor cursor = contentResolver.query(ContentUris.withAppendedId(Entries.CONTENT_URI, entryId),
                projection,
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            String title = cursor.getString(0);
            String date = cursor.getString(1);
            String content = cursor.getString(2);
            titleText.setText(title);
            dateText.setText(date);
            contentText.setText(content);
        } else {
            throw new IllegalArgumentException("cannot find entry with id " + entryId);
        }
    }
}
