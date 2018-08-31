package com.gerardogandeaga.cyberlock

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.gerardogandeaga.cyberlock.store.database.DatabaseOpenHelper
import net.sqlcipher.database.SQLiteDatabase

/**
 * @author gerardogandeaga
 * created on 2018-07-30
 *
 * App class preserves constants that do not need to be initiliazed more than once across application life cycles,
 * such as easy contexts and single db inits
 */
@SuppressLint("StaticFieldLeak")
class App : Application() {

    companion object {
        private val TAG = "App"

        // static getters
        @JvmStatic private lateinit var AppContext: Context
        @JvmStatic private lateinit var Database: DatabaseOpenHelper
    }

    init {
        AppContext = this
    }

    override fun onCreate() {
        super.onCreate()
        // todo ask permissions

        // todo create needed dirs

        // start db
        loadDatabase()
    }

    private fun loadDatabase() {
        // start the sql db
        SQLiteDatabase.loadLibs(AppContext)

        // this do is always created on application startup and is made static to only one instance of the
        // db at all times for better performance
        Database = DatabaseOpenHelper(AppContext)

        if (getDatabasePath(DatabaseOpenHelper.DATABASE).exists()) {
            println("app: Database does exists")
        } else {
            println("app: Database does not exist")
        }

        // init db
        // begin db and set password // todo create db accessors
        val database = App.Database
        database.recycle()

        App.Database.setPassword("TMP_PASSWORD")
        database.update()
        //
    }
}
