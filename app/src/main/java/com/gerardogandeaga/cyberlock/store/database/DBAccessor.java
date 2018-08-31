package com.gerardogandeaga.cyberlock.store.database;

import com.gerardogandeaga.cyberlock.App;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.List;

/**
 * @author gerardogandeaga
 * created on 2018-08-23
 */
public abstract class DBAccessor<Item> {
    static volatile DBAccessor INSTANCE;
    private DatabaseOpenHelper mDatabaseOpenHelper;
    private SQLiteDatabase mSQLiteDatabase;

    // abstract methods

    /**
     * save a single instance of the item to the db
     * @param item abstract item with data that will be implemented in its own way to save
     */
    public abstract void save(Item item);

    /**
     * save multiple instance of items to db
     * @param itemList abstract list of items to be saved
     */
    public abstract void save(List<Item> itemList);

    /**
     * remove item from db
     * @param item item to match and be removed
     */
    public abstract void remove(Item item);

    /**
     * removes multiple items from db
     * @param itemList items to be removed
     */
    public abstract void remove(List<Item> itemList);

    /**
     * creates a list of all items from the database
     * @return list of items
     */
    public abstract List<Item> getAllItems();

    /**
     * builds an item from the position of the cursor in the db
     * @param cursor positional information in the db that hold all information of a specific item
     * @return item
     */
    public abstract Item buildItem(Cursor cursor);

    // main methods

    DBAccessor() {
        this.mDatabaseOpenHelper = App.getDatabase();
    }

    protected SQLiteDatabase getDatabase() {
        return mSQLiteDatabase;
    }

    /**
     * starts the db to a writable state
     */
    public void openWritable() {
        this.mSQLiteDatabase = mDatabaseOpenHelper.getWritableDatabase();
        mSQLiteDatabase.beginTransaction();
    }

    /**
     * starts the db to a readable state
     */
    public void openReadable() {
        this.mSQLiteDatabase = mDatabaseOpenHelper.getReadableDatabase();
        mSQLiteDatabase.beginTransaction();
    }

    /**
     * finalizes transactions
     */
    public void close() {
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
        this.mSQLiteDatabase = null;
    }
}
