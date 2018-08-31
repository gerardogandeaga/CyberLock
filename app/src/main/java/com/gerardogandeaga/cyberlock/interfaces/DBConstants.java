package com.gerardogandeaga.cyberlock.interfaces;

/**
 * @author gerardogandeaga
 * created on 2018-08-16
 */
public interface DBConstants {

    /**
     * key to mainly used for auto incremental primary keys. when inserting multiple items at a time
     * should not be used with date created.
     */
    String UNIQUE_ID = "id";
    int POS_UNIQUE_ID = 0;

    /**
     * key used for single input items.
     * should not be used with unique id
     */
    String DATE_CREATED = "date_created";
    int POS_DATE_CREATED = 0;

    /**
     * global usage keys
     */
    String DATE_MODIFIED = "date_modified";
    int POS_DATE_MODIFIED = 1;
}
