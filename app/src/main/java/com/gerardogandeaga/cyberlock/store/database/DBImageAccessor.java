package com.gerardogandeaga.cyberlock.store.database;

import android.content.ContentValues;

import com.gerardogandeaga.cyberlock.interfaces.DBImageConstants;
import com.gerardogandeaga.cyberlock.objects.savable.Image;
import com.gerardogandeaga.cyberlock.util.Storage;

import net.sqlcipher.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gerardogandeaga
 * created on 2018-08-15
 *
 * DBImageAccessor extends DBAccessor to be able to save image data to the database.
 * this class also performas file transfers when saving images.
 */
public class DBImageAccessor extends DBAccessor<Image> implements DBImageConstants {

    private DBImageAccessor() {
        super();
    }

    public static synchronized DBImageAccessor getInstance() {
        return (DBImageAccessor) (INSTANCE = (INSTANCE == null || !(INSTANCE instanceof DBImageAccessor) ? new DBImageAccessor() : INSTANCE));
    }

    /**
     * add image to content value and query for insertion.
     * must call close() to complete transactions!
     */
    @Override
    public void save(Image image) {
        storeImage(image);
        // save image data
        ContentValues values = new ContentValues();
        values.put(KEY_DISPLAY_NAME,    image.getDisplayName());
        values.put(KEY_CURRENT_BUCKET,  image.getCurrentBucket());
        values.put(KEY_CURRENT_URI,     image.getCurrentUri());
        values.put(KEY_ORIGINAL_BUCKET, image.getOriginalBucket());
        values.put(KEY_ORIGINAL_URI,    image.getOriginalUri());

        getDatabase().insert(TABLE_IMAGES, null, values);
    }

    /**
     * **note**
     * when saving images to the db, we run the save and file movement on a new thread to free up space on main thread.
     * --------
     * saving image information will first move image to its new directory in the application and save it's information to the db
     * @param imageList images to be moved and saved
     */
    @Override
    public void save(List<Image> imageList) {
        for (Image image : imageList) {
            storeImage(image);
            save(image);
        }
    }

    @Override
    public void remove(Image image) {
        getDatabase().delete(TABLE_IMAGES, UNIQUE_ID + " = ?", new String[]{image.getId()});
    }

    @Override
    public void remove(List<Image> imageList) {
        for (Image image : imageList) {
            remove(image);
        }
    }

    @Override
    public List<Image> getAllItems() {
        List<Image> imageArrayList = new ArrayList<>();
        Cursor cursor = getDatabase().rawQuery(QUERY_ORDER_ID_DESC, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            imageArrayList.add(buildItem(cursor));
            cursor.moveToNext();
        }
        return imageArrayList;
    }

    @Override
    public Image buildItem(Cursor cursor) {
        String id =             cursor.getString(POS_UNIQUE_ID);
        String displayName =    cursor.getString(POS_DISPLAY_NAME);
        String currentBucket =  cursor.getString(POS_CURRENT_BUCKET);
        String currentUri =     cursor.getString(POS_CURRENT_URI);
        String originalBucket = cursor.getString(POS_ORIGINAL_BUCKET);
        String originalUri =    cursor.getString(POS_ORIGINAL_URI);
        // create
        return new Image(id, displayName, currentBucket, currentUri, originalBucket, originalUri);
    }

    /**
     * moves files to image dir and sets new uri to be used by reference
     * @param image to be moved and updated
     */
    private void storeImage(Image image) {
        String originalPath = image.getCurrentUri();
        // move image to new dir and set the resulting uri to current uri
        String newPath = Storage.FileManager.moveFile(originalPath, Storage.FILES_DIR + Storage.IMAGE_DIRECTORY);
        // set new and original paths
        image.withOriginalUri(originalPath);
        image.withCurrentUri(newPath);
    }
}
