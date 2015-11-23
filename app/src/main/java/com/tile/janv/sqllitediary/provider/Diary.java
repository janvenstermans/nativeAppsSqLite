package com.tile.janv.sqllitediary.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by janv on 23-Nov-15.
 *
 * inspired by https://android.googlesource.com/platform/development/+/05523fb0b48280a5364908b00768ec71edb847a2/samples/NotePad/src/com/example/android/notepad/NotePad.java
 */
public final class Diary {
    public static final String AUTHORITY = "com.tile.janv.sqllitediary.provider.diary";

    private Diary() {}

    public static final class DiaryColumns implements BaseColumns {
        private DiaryColumns() {}

        /**
         * The content:// style URL for this table
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
