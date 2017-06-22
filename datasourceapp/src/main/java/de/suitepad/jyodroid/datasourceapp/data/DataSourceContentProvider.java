package de.suitepad.jyodroid.datasourceapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import de.suitepad.jyodroid.datasourceapp.data.DataSourceContract.MenuItemEntry;

/**
 * Created by johntangarife on 6/20/17.
 */

public final class DataSourceContentProvider extends ContentProvider {

    private static final int ALL_ITEMS = 100;
    private static final int ITEM_BY_ID = 101;

    private DataSourceDBHelper mDbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder sQueryBuilder;

    static {
        sQueryBuilder = new SQLiteQueryBuilder();
        sQueryBuilder.setDistinct(true);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DataSourceDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        switch (sUriMatcher.match(uri)) {
            case ALL_ITEMS:
                return mDbHelper.getReadableDatabase().query(
                        MenuItemEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
            case ITEM_BY_ID:
                return getItemById(uri, projection, sortOrder);

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case ITEM_BY_ID:
                return MenuItemEntry.CONTENT_TYPE;
            case ALL_ITEMS:
                return MenuItemEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    /**
     * All paths added to the UriMatcher have a corresponding code to return when a match is
     * found.  The code passed into the constructor represents the code to return for the root
     * URI.  It's common to use NO_MATCH as the code for this case.
     *
     * @return corresponding uri matcher
     */
    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DataSourceContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, DataSourceContract.PATH_MENU_ITEM, ALL_ITEMS);
        matcher.addURI(authority, DataSourceContract.PATH_MENU_ITEM + "/*", ITEM_BY_ID);
        return matcher;
    }

    /**
     * @param uri        original uri
     * @param projection of elements in table to be returned on {@link Cursor}
     * @param sortOrder  order in the elements in table to be returned on {@link Cursor}
     * @return query result
     */
    private Cursor getItemById(Uri uri, String[] projection, String sortOrder) {

        String id = DataSourceContract.getMenuItemIdFromUri(uri);

        sQueryBuilder.setTables(MenuItemEntry.TABLE_NAME);

        String selection = MenuItemEntry.TABLE_NAME + "." + MenuItemEntry.COLUMN_ID + " = ?";

        String[] selectionArgs = new String[]{id};

        return sQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

    }
}
