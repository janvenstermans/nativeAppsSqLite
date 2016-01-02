package com.tile.janv.sqllitediary;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.tile.janv.sqllitediary.provider.Entries;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EntryAddActivity extends Activity {

    @Bind(R.id.entry_add_title)
    protected EditText titleEditField;
    @Bind(R.id.entry_add_content)
    protected EditText contentEditField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_add);
        ButterKnife.bind(this);
    }

    //-----------------
    // menu methods
    //-----------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_entry_add, menu);
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

    //-----------------
    // ui methods
    //-----------------

    @OnClick(R.id.entry_add_button_label)
    public void onSubmit() {
        String title = titleEditField.getText().toString();
        String content = contentEditField.getText().toString();
        // check if all filled in
        if (title == null || title.isEmpty() ||
                content == null || content.isEmpty()) {
            Toast.makeText(this, getString(R.string.entry_add_fail), Toast.LENGTH_SHORT).show();
            return;
        }
        ContentResolver contentResolver = getContentResolver();
        // Add a new diary record
        ContentValues values = new ContentValues();
        values.put(Entries.DiaryColumns.TITLE, title);
        values.put(Entries.DiaryColumns.ENTRY, content);
        values.put(Entries.DiaryColumns.CREATED_DATE, new Date().toString());

        Uri uri = contentResolver.insert(Entries.CONTENT_URI, values);
        Toast.makeText(this, getString(R.string.entry_add_success, uri.toString()), Toast.LENGTH_LONG).show();
        resetTextFields();
    }

    private void resetTextFields() {
        titleEditField.setText("");
        contentEditField.setText("");
    }
}
