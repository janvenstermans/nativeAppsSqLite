package com.tile.janv.sqllitediary.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Constant class for the Entries table in the database.
 *
 * inspired by https://android.googlesource.com/platform/development/+/05523fb0b48280a5364908b00768ec71edb847a2/samples/NotePad/src/com/example/android/notepad/NotePad.java
 */
public final class Entries {

    /**
     * Should be identical as in the manifest file.
     */
    public static final String AUTHORITY = "com.tile.janv.sqllitediary.provider.diary";

    /**
     * The content:// style URL for this table.
     * This is the url to query according to http://www.tutorialspoint.com/android/android_content_providers.htm.
     */
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/entries");

    /**
     * The MIME type of {@link #CONTENT_URI} providing a directory of diary entries.
     */
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.note";

    /**
     * The MIME type of a {@link #CONTENT_URI} sub-directory of a diary entry.
     */
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.note";

    private Entries() {}

    public static final class DiaryColumns implements BaseColumns {
        private DiaryColumns() {}

        /**
         * The title of the diary entry
         * <P>Type: TEXT</P>
         */
        public static final String TITLE = "title";
        /**
         * The content of the diary entry
         * <P>Type: TEXT</P>
         */
        public static final String ENTRY = "entry";
        /**
         * The timestamp for when the entry was created
         * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
         */
        public static final String CREATED_DATE = "created";

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = CREATED_DATE + " DESC";

    }
}
