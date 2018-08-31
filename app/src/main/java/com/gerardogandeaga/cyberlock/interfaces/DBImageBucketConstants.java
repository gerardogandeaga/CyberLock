package com.gerardogandeaga.cyberlock.interfaces;

/**
 * @author gerardogandeaga
 * created on 2018-08-23
 */
public interface DBImageBucketConstants extends DBConstants {

    /**
     * table name for the image bucket section in the db
     */
    String TABLE_IMAGE_BUCKETS = "image_bucket_table";

    /**
     * name of image bucket
     */
    String KEY_BUCKET_NAME = "bucket_name";
    int POS_BUCKET_NAME = 1;

    /**
     * path of the bucket
     */
    String KEY_BUCKET_PATH = "bucket_path";
    int POS_BUCKET_PATH = 2;

    /**
     * path to top uri to show as preview
     */
    String KEY_BUCKET_COVER_URI = "bucket_cover_image_uri";
    int POS_COVER_URI = 3;

    /**
     *
     */
    String QUERY_ORDER_ID_DESC = "SELECT * FROM " + TABLE_IMAGE_BUCKETS + " ORDER BY " + UNIQUE_ID + " DESC";
}
