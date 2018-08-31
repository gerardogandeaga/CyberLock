package com.gerardogandeaga.cyberlock.interfaces;

/**
 * @author gerardogandeaga
 * created on 2018-08-14
 *
 *
 */
public interface DBImageConstants extends DBConstants {

    /**
     * table name for the image section in the db
     */
    String TABLE_IMAGES = "image_table";

    /**
     * name of the image with extension
     */
    String KEY_DISPLAY_NAME = "display_name";
    int POS_DISPLAY_NAME = 1;

    /**
     * current album image belongs to
     */
    String KEY_CURRENT_BUCKET = "c_bucket";
    int POS_CURRENT_BUCKET = 2;

    /**
     * complete path to image
     */
    String KEY_CURRENT_URI = "c_uri";
    int POS_CURRENT_URI = 3;

    /**
     * album name image belonged to before being imported
     */
    String KEY_ORIGINAL_BUCKET = "o_bucket";
    int POS_ORIGINAL_BUCKET = 4;

    /**
     * complete path to image before being imported
     */
    String KEY_ORIGINAL_URI = "o_uri";
    int POS_ORIGINAL_URI = 5;

    /**
     *
     */
    String QUERY_ORDER_ID_DESC = "SELECT * FROM " + TABLE_IMAGES + " ORDER BY " + UNIQUE_ID + " DESC";
}
