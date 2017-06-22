package de.suitepad.jyodroid.datasourceapp;

import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;

import com.facebook.stetho.Stetho;

import de.suitepad.jyodroid.datasourceapp.data.DataSourceContract;

/**
 * Created by johntangarife on 6/21/17.
 */

public class DataSourceApp extends Application {
    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     * If you override this method, be sure to call super.onCreate().
     */
    @Override
    public void onCreate() {

        super.onCreate();

        ContentResolver cr = getContentResolver();

        String[] projection = new String[]{
                DataSourceContract.MenuItemEntry.COLUMN_ID,
                DataSourceContract.MenuItemEntry.COLUMN_NAME,
                DataSourceContract.MenuItemEntry.COLUMN_TYPE,
                DataSourceContract.MenuItemEntry.COLUMN_PRICE
        };

        Cursor cursor = cr.query(DataSourceContract.MenuItemEntry.CONTENT_URI, projection, null, null, null);

        //Enable Stetho only in debugging builds.
        if (BuildConfig.DEBUG) {
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                            .build());
        }
    }
}
