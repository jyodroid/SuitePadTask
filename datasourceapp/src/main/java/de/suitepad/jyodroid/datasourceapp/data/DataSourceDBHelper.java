package de.suitepad.jyodroid.datasourceapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import de.suitepad.jyodroid.datasourceapp.data.DataSourceContract.MenuItemEntry;

/**
 * Created by johntangarife on 6/20/17.
 */

final class DataSourceDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DataSourceApp.db";
    private static final String DEFAULT_JSON_NAME = "sample.json";
    private static final String LOG_TAG = DataSourceDBHelper.class.getSimpleName();

    private Context mContext;

    DataSourceDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MENU_ITEMS_TABLE = "CREATE TABLE " + MenuItemEntry.TABLE_NAME +
                " (" +
                MenuItemEntry.COLUMN_FILE_NAME + " TEXT PRIMARY KEY, " +
                MenuItemEntry.COLUMN_CONTENT + " TEXT NOT NULL" +
                " );";

        //Execute
        db.execSQL(SQL_CREATE_MENU_ITEMS_TABLE);

        //Insert default data
        insertDefaultValues(MenuItemEntry.TABLE_NAME, DEFAULT_JSON_NAME, mContext, db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Reset data on table if database structure changed
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + MenuItemEntry.TABLE_NAME);
            onCreate(db);
        }
    }

    /**
     * Insert default values from Json
     *
     * @param tableName name of the table where is intended to do the insertion
     * @param jsonName  name of file where is the default data
     * @param mContext  app context
     * @param db        SQL database where is table #tableName
     */

    private void insertDefaultValues(String tableName, String jsonName,
                                     Context mContext, SQLiteDatabase db) {

        final String SQL_INSERT_JSON =
                "INSERT INTO " + tableName +
                        " VALUES ( \"" + jsonName + "\",\'" + getJSON(jsonName, mContext) + "\')";

        db.execSQL(SQL_INSERT_JSON);
    }

    /**
     * Get items from JSON file in database
     *
     * @param fileName of json file
     * @param context  context of application
     * @return List with menu items
     */
    private String getJSON(String fileName, Context context) {

        final String JSON_FORMAT = "UTF-8";

        try {
            InputStream inputStream = context.getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            return new String(buffer, JSON_FORMAT);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error parsing JSON ", e);
            return null;
        }
    }
}
