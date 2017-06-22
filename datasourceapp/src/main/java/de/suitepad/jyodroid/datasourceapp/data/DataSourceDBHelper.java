package de.suitepad.jyodroid.datasourceapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
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

    private Context mContext;

    public DataSourceDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MENU_ITEMS_TABLE = "CREATE TABLE " + MenuItemEntry.TABLE_NAME + " (" +
                MenuItemEntry.COLUMN_ID + " TEXT PRIMARY KEY," +
                MenuItemEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                MenuItemEntry.COLUMN_PRICE + " REAL NOT NULL, " +
                MenuItemEntry.COLUMN_TYPE + " TEXT NOT NULL" +
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
     * @param defaultJsonName name of file where is the default data
     * @param mContext        app context
     */
    private void insertDefaultValues(String tableName, String defaultJsonName,
                                     Context mContext, SQLiteDatabase db) {
        List<MenuItem> defaultItems = getItemsFromJSON(defaultJsonName, mContext);

        StringBuilder queryFormatBuilder = new StringBuilder();
        queryFormatBuilder
                .append("INSERT INTO ")
                .append(tableName)
                .append(" VALUES ( \"%1$s\", \"%2$s\", %3$f, \"%4$s\")");

        if (defaultItems != null) {
            for (MenuItem item : defaultItems) {

                String insertQuery =
                        String.format(queryFormatBuilder.toString(), item.getId(),
                                item.getName(), item.getPrice(), item.getType());

                db.execSQL(insertQuery);
            }
        }
    }

    /**
     * Get items from JSON file in database
     *
     * @param fileName of json file
     * @param context  context of application
     * @return List with menu items
     */
    private List<MenuItem> getItemsFromJSON(String fileName, Context context) {

        final String JSON_FORMAT = "UTF-8";

        try {
            InputStream inputStream = context.getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String json = new String(buffer, JSON_FORMAT);

            Gson gson = new Gson();
            return gson.fromJson(json, new TypeToken<List<MenuItem>>() {
            }.getType());


        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
