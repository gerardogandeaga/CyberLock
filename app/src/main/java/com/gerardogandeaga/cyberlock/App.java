package com.gerardogandeaga.cyberlock;

import android.app.Application;
import android.content.Context;

import com.gerardogandeaga.cyberlock.database.DatabaseOpenHelper;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * @author gerardogandeaga
 * created on 2018-07-30
 *
 * App class preserves constants that do not need to be initiliazed more than once across application life cycles,
 * such as easy contexts and single db inits
 */
public class App extends Application {
    private static final String TAG = "App";

    private static Context Context;
    private static DatabaseOpenHelper Database;

    @Override
    public void onCreate() {
        super.onCreate();

        Context = this;

        // todo ask permissions

        // todo create needed dirs

        // start db
        loadDatabase();
    }

    private void loadDatabase() {
        // start the sql db
        SQLiteDatabase.loadLibs(Context);

        // this do is always created on application startup and is made static to only one instance of the
        // db at all times for better performance
        Database = new DatabaseOpenHelper(Context);

        if (getDatabasePath(DatabaseOpenHelper.DATABASE).exists()) {
            System.out.println("app: database does exists");
        } else {
            System.out.println("app: database does not exist");
        }

        // init db
        // begin db and set password // todo create db accessors
        DatabaseOpenHelper database = App.getDatabase();
        database.recycle();

        App.getDatabase().setPassword("TMP_PASSWORD");
        database.update();
        //
    }

    // static getters

    public static Context getContext() {
        return Context;
    }

    public static DatabaseOpenHelper getDatabase() {
        return Database;
    }
}
