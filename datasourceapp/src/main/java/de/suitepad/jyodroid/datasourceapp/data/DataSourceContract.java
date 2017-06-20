package de.suitepad.jyodroid.datasourceapp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by johntangarife on 6/20/17.
 */

public final class DataSourceContract {

    static final String CONTENT_AUTHORITY = "de.suitepad.jyodroid.datasourceapp";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    static final String PATH_MENU_ITEM = "menu_item";

    static final class MenuItemEntry implements BaseColumns {
        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MENU_ITEM).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MENU_ITEM;

        static final String TABLE_NAME = "menu_items";
        static final String COLUMN_ID = "_id";
        static final String COLUMN_NAME = "name";
        static final String COLUMN_PRICE = "price";
        static final String COLUMN_TYPE = "type";

        static Uri buildUri(Long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

}
