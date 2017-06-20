package de.suitepad.jyodroid.datasourceapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import de.suitepad.jyodroid.datasourceapp.data.DataSourceContract.MenuItemEntry;
import de.suitepad.jyodroid.datasourceapp.model.MenuItem;

/**
 * Created by johntangarife on 6/20/17.
 */

public final class DataSourceDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DataSourceApp.db";
    private static final String DEFAULT_JSON_NAME = "initial_data.json";

    public DataSourceDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MENU_ITEMS_TABLE = "CREATE TABLE " + MenuItemEntry.TABLE_NAME + " (" +
                MenuItemEntry.COLUMN_ID + " TEXT PRIMARY KEY," +
                MenuItemEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                MenuItemEntry.COLUMN_PRICE + " REAL NOT NULL, " +
                MenuItemEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                " );";

        //Execute
        db.execSQL(SQL_CREATE_MENU_ITEMS_TABLE);

        //Insert default data
        List<MenuItem> defaultItems = getDefaultItemsFromJSON(DEFAULT_JSON_NAME);

        if (defaultItems != null) {

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Reset data on table if database structure changed
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + MenuItemEntry.TABLE_NAME);
            onCreate(db);
        }
    }

    private List<MenuItem> getDefaultItemsFromJSON(String fileName) {
        return null;
    }
}
