package de.suitepad.jyodroid.datasourceapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by johntangarife on 6/20/17.
 */

public final class DataSourceContract {

    public static final String CONTENT_AUTHORITY = "de.suitepad.jyodroid.datasourceapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MENU_ITEM = "menu_item";

    public static final class MenuItemEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MENU_ITEM).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MENU_ITEM;

        public static final String TABLE_NAME = "menu_items";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_TYPE = "type";
    }

    public static String getMenuItemIdFromUri(Uri uri) {
        return uri.getPathSegments().get(1);
    }

}
