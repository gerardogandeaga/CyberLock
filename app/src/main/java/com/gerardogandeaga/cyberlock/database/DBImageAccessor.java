package com.gerardogandeaga.cyberlock.database;

import com.gerardogandeaga.cyberlock.App;
import com.gerardogandeaga.cyberlock.interfaces.DBImageConstants;

/**
 * @author gerardogandeaga
 * created on 2018-08-15
 */
public class DBImageAccessor implements DBImageConstants {
    private DatabaseOpenHelper mDatabase;
    private static volatile DBImageAccessor INSTANCE;
    private static final String QUERY = "SELECT * From " + TABLE_IMAGE + " ORDER BY " + TIME_CREATED + " DESC";

    private DBImageAccessor() {
        this.mDatabase = App.getDatabase();
    }

    public static synchronized DBImageAccessor getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DBImageAccessor();
        }
        return INSTANCE;
    }

    public void save() {

    }
}
