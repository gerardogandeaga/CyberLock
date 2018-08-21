package com.gerardogandeaga.cyberlock.interfaces.constants;

/**
 * @author gerardogandeaga
 * created on 2018-08-14
 *
 *
 */
public interface DBImageConstants extends DBConstants {
    String TABLE_IMAGES = "image_table";

    /**
     * unique integer tag
     */
    String KEY_ID = "image_id";
    int POS_ID = 1;

    /**
     * name of the image with extension
     */
    String KEY_DISPLAY_NAME = "image_display_name";
    int POS_DISPLAY_NAME = 2;

    /**
     * current album image belongs to
     */
    String KEY_CURRENT_BUCKET = "image_c_bucket";
    int POS_CURRENT_BUCKET = 3;

    /**
     * path to image's directory
     */
    String KEY_CURRENT_PATH = "image_c_path";
    int POS_CURRENT_PATH = 4;

    /**
     * complete path to image
     */
    String KEY_CURRENT_URI = "image_c_uri";
    int POS_CURRENT_URI = 5;

    /**
     * album image belonged to before being imported
     */
    String KEY_ORIGINAL_BUCKET = "image_o_bucket";
    int POS_ORIGINAL_BUCKET = 6;

    /**
     * path to image's directory before being imported
     */
    String KEY_ORIGINAL_PATH = "image_o_path";
    int POS_ORIGINAL_PATH = 7;

    /**
     * complete path to image before being imported
     */
    String KEY_ORIGINAL_URI = "image_o_uri";
    int POS_ORIGINAL_URI = 8;
}
