package com.gerardogandeaga.cyberlock.database;

import android.content.ContentValues;

import com.gerardogandeaga.cyberlock.App;
import com.gerardogandeaga.cyberlock.interfaces.constants.DBImageConstants;
import com.gerardogandeaga.cyberlock.objects.savable.Image;
import com.gerardogandeaga.cyberlock.util.Storage;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gerardogandeaga
 * created on 2018-08-15
 */
public class DBImageAccessor implements DBImageConstants {
    private static final String TAG = "DBImageAccessor";
    private DatabaseOpenHelper mOpenHelper;
    private SQLiteDatabase mSQLiteDatabase;
    private static volatile DBImageAccessor INSTANCE;
    private static final String QUERY = "SELECT * From " + TABLE_IMAGES + " ORDER BY " + UNIQUE_ID + " DESC";

    private DBImageAccessor() {
        this.mOpenHelper = App.getDatabase();
    }

    public static synchronized DBImageAccessor getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DBImageAccessor();
        }
        return INSTANCE;
    }

    /**
     * starts the db to a writable state
     */
    public void openWritable() {
        this.mSQLiteDatabase = mOpenHelper.getWritableDatabase();
        mSQLiteDatabase.beginTransaction();
    }

    /**
     * starts the db to a readable state
     */
    public void openReadable() {
        this.mSQLiteDatabase = mOpenHelper.getReadableDatabase();
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

    /**
     * **note**
     * when saving images to the db, we run the save and file movement on a new thread to free up space on main thread.
     * --------
     * saving image information will first move image to its new directory in the application and save it's information to the db
     * @param imageList images to be moved and saved
     */
    public void save(final List<Image> imageList) {
        for (Image image : imageList) {
            storeImage(image);
            save(image);
        }
    }

    /**
     * add image to content value and query for insertion.
     * must call close() to complete transactions!
     */
    public void save(Image image) {
        storeImage(image);
        // save image data
        ContentValues values = new ContentValues();
        values.put(KEY_DISPLAY_NAME,    image.getDisplayName());
        values.put(KEY_CURRENT_BUCKET,  image.getCurrentBucket());
        values.put(KEY_CURRENT_PATH,    image.getCurrentPath());
        values.put(KEY_CURRENT_URI,     image.getCurrentUri());
        values.put(KEY_ORIGINAL_BUCKET, image.getOriginalBucket());
        values.put(KEY_ORIGINAL_PATH,   image.getOriginalPath());
        values.put(KEY_ORIGINAL_URI,    image.getOriginalUri());

        mSQLiteDatabase.insert(TABLE_IMAGES, null, values);
    }

    public void delete(List<Image> imageList) {
        for (Image image : imageList) {
            delete(image);
        }
    }

    private void delete(Image image) {
        String date = Long.toString(image.getTimeCreated());
        mSQLiteDatabase.delete(TABLE_IMAGES, DATE_CREATED + " = ?", new String[]{date});
    }

    /**
     * @return list of images
     */
    public List<Image> getImages() {
        List<Image> imageArrayList = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.rawQuery(QUERY, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Image image = buildImage(cursor);
            imageArrayList.add(image);
            System.out.println("getImage: " + image);
            cursor.moveToNext();
        }
        return imageArrayList;
    }

    private Image buildImage(Cursor cursor) {
        String id =             cursor.getString(POS_UNIQUE_ID);
        String displayName =    cursor.getString(POS_DISPLAY_NAME);
        String currentBucket =  cursor.getString(POS_CURRENT_BUCKET);
        String currentPath =    cursor.getString(POS_CURRENT_PATH);
        String currentUri =     cursor.getString(POS_CURRENT_URI);
        String originalBucket = cursor.getString(POS_ORIGINAL_BUCKET);
        String originalPath =   cursor.getString(POS_ORIGINAL_PATH);
        String originalUri =    cursor.getString(POS_ORIGINAL_URI);
        // create
        return new Image(id, displayName, currentBucket, currentPath, currentUri, originalBucket, originalPath, originalUri);
    }

    /**
     * moves files to image dir and sets new uri to be used by reference
     * @param image to be moved and updated
     */
    private void storeImage(Image image) {
        // set original uri to current
        image.withOriginalUri(image.getCurrentUri());
        // move image to new dir and set the resulting uri to current uri
        image.withCurrentUri(
                Storage.FileManager.moveFile(image.getOriginalUri(), Storage.FILES_DIR + Storage.IMAGE_DIRECTORY)
        );
    }
}
