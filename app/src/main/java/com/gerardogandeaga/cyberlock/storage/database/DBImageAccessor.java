package com.gerardogandeaga.cyberlock.storage.database;

import android.content.ContentValues;

import com.gerardogandeaga.cyberlock.App;
import com.gerardogandeaga.cyberlock.interfaces.constants.DBImageConstants;
import com.gerardogandeaga.cyberlock.objects.savable.Image;
import com.gerardogandeaga.cyberlock.util.Storage;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author gerardogandeaga
 * created on 2018-08-15
 */
public class DBImageAccessor implements DBImageConstants {
    private DatabaseOpenHelper mOpenHelper;
    private static volatile DBImageAccessor INSTANCE;
    private static final String QUERY = "SELECT * From " + TABLE_IMAGES + " ORDER BY " + DATE_CREATED + " DESC";

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
     * saving image information will first move image to its new directory in the application and save it's information to the db
     * @param imageArrayList images to be moved and saved
     */
    public void save(ArrayList<Image> imageArrayList) {
        // first move images to new dir
        Storage.FileManager.storeImages(imageArrayList);

        // save image data
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        for (Image image : imageArrayList) {
            ContentValues values = new ContentValues();
            values.put(DATE_CREATED,        new Date().getTime());
            values.put(KEY_ID,              image.getId());
            values.put(KEY_DISPLAY_NAME,    image.getId());
            values.put(KEY_CURRENT_BUCKET,  image.getCurrentBucket());
            values.put(KEY_CURRENT_PATH,    image.getCurrentPath());
            values.put(KEY_CURRENT_URI,     image.getCurrentUri());
            values.put(KEY_ORIGINAL_BUCKET, image.getOriginalBucket());
            values.put(KEY_ORIGINAL_PATH,   image.getOriginalPath());
            values.put(KEY_ORIGINAL_URI,    image.getOriginalUri());
            db.insert(TABLE_IMAGES, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void delete(ArrayList<Image> imageArrayList) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        db.beginTransaction();

        for (Image image : imageArrayList) {
            String date = Long.toString(image.getTimeCreated());
            db.delete(TABLE_IMAGES, DATE_CREATED + " = ?", new String[]{date});
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * @return array list of images
     */
    public ArrayList<Image> getImages() {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        ArrayList<Image> imageArrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery(QUERY, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            imageArrayList.add(buildImage(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return imageArrayList;
    }

    private Image buildImage(Cursor cursor) {
        long created =          cursor.getLong(POS_DATE_CREATED);
        String id =             cursor.getString(POS_ID);
        String displayName =    cursor.getString(POS_DISPLAY_NAME);
        String currentBucket =  cursor.getString(POS_CURRENT_BUCKET);
        String currentPath =    cursor.getString(POS_CURRENT_PATH);
        String currentUri =     cursor.getString(POS_CURRENT_URI);
        String originalBucket = cursor.getString(POS_ORIGINAL_BUCKET);
        String originalPath =   cursor.getString(POS_ORIGINAL_PATH);
        String originalUri =    cursor.getString(POS_ORIGINAL_URI);
        // create
        return new Image(created, id, displayName, currentBucket, currentPath, currentUri, originalBucket, originalPath, originalUri);
    }
}
