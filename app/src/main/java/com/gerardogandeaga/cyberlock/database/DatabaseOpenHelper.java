package com.gerardogandeaga.cyberlock.database;

import android.content.Context;

import com.gerardogandeaga.cyberlock.interfaces.DBImageConstants;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

/**
 * @author gerardogandeaga
 * created on 2018-08-14
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper implements DBImageConstants {
    public static final String DATABASE = "cyberlock.sql";
    private static final int VERSION = 1;

    private SQLiteDatabase mDatabase;
    private String mPassword;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public String getPassword() {
        return mPassword;
    }

    public void recycle() {
        close();
        this.mDatabase = null;
        this.mPassword = null;
    }

    public void update() {
        if (mDatabase == null || !mDatabase.isOpen()) {
            if (getPassword() == null || getPassword().equals("")) {
                throw new IllegalArgumentException("password null or not acceptable");
            }
            this.mDatabase = this.getWritableDatabase(getPassword());
        }
    }

    public SQLiteDatabase getWritableDatabase() {
        update();
        return mDatabase;
    }

    @Override
    public synchronized void close() {
        super.close();
        if (mDatabase != null) {
            if (!mDatabase.isOpen()) {
                mDatabase.close();
                this.mDatabase = null;
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + " IMAGES" + "(" +

//              KEY                  DATA TYPE
                KEY_BUCKET +       " TEXT, " +
                KEY_IMAGE_BITMAP + " BLOB, " +
        ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
