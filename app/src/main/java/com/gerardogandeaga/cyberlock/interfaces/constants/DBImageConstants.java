package com.gerardogandeaga.cyberlock.interfaces.constants;

/**
 * @author gerardogandeaga
 * created on 2018-08-14
 */
public interface DBImageConstants extends DBConstants {

    String TABLE_IMAGE = "image_gallery";

    String KEY_BUCKET = "image_bucket";
    int POS_KEY_BUCKET = 1;

    String KEY_IMAGE_ID = "image_id";
    int POS_KEY_IMAGE_ID = 2;

    String KEY_IMAGE_PATH = "image_path";
    int POS_KEY_IMAGE_PATH = 3;

    String KEY_IMAGE_URI = "image_uri";
    int POS_KEY_IMAGE_URI = 4;
}
