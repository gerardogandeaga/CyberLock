package com.gerardogandeaga.cyberlock.storage.database;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.util.Log;

import com.gerardogandeaga.cyberlock.App;
import com.gerardogandeaga.cyberlock.GlideApp;
import com.gerardogandeaga.cyberlock.interfaces.constants.DBImageConstants;
import com.gerardogandeaga.cyberlock.objects.Media;
import com.gerardogandeaga.cyberlock.objects.savable.Image;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * @author gerardogandeaga
 * created on 2018-08-15
 */
public class DBImageAccessor implements DBImageConstants {
    private static final String TAG = "DBImageAccessor";

    private DatabaseOpenHelper mOpenHelper;
    private static volatile DBImageAccessor INSTANCE;
    private static final String QUERY = "SELECT * From " + TABLE_IMAGE + " ORDER BY " + DATE_CREATED + " DESC";

    private DBImageAccessor() {
        this.mOpenHelper = App.getDatabase();
    }

    public static synchronized DBImageAccessor getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DBImageAccessor();
        }
        return INSTANCE;
    }

    private Cursor getCursor(SQLiteDatabase db) {
        return db.rawQuery(QUERY, null);
    }

    public void save(String directory, ArrayList<Media> mediaArrayList) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        db.beginTransaction();
        try {
            for (Media media : mediaArrayList) {
                Bitmap bitmap = GlideApp.with(App.getContext())
                                        .asBitmap()
                                        .load(media.getUri())
                                        .submit()
                                        .get();
                System.out.println("bitmap : " + bitmap.getHeight() + " x " + bitmap.getWidth());

                ContentValues values = new ContentValues();
                values.put(DATE_CREATED, new Date().getTime());
                values.put(KEY_BUCKET, media.getBucketName());
                values.put(KEY_IMAGE_ID, media.getId());
                values.put(KEY_IMAGE_PATH, directory);
                values.put(KEY_IMAGE_URI, media.get());
                db.insert(TABLE_IMAGE, null, values);
            }

            db.setTransactionSuccessful();
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, "could not load bitmap");
        } finally {
            db.endTransaction();
        }
    }

    public void delete(ArrayList<Image> imageArrayList) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        db.beginTransaction();

        for (Image image : imageArrayList) {
            String date = Long.toString(image.getTimeCreated());
            db.delete(TABLE_IMAGE, DATE_CREATED + " = ?", new String[]{date});
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public ArrayList<Image> getImages() {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        ArrayList<Image> imageArrayList = new ArrayList<>();
        Cursor cursor = getCursor(db);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            imageArrayList.add(buildImage(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return imageArrayList;
    }

    private Image buildImage(Cursor cursor) {
        long created =  cursor.getLong(POS_DATE_CREATED);
        String bucket = cursor.getString(POS_KEY_BUCKET);
        String id =     cursor.getString(POS_KEY_IMAGE_ID);
        String path =   cursor.getString(POS_KEY_IMAGE_PATH);
        String uri =    cursor.getString(POS_KEY_IMAGE_URI);
        // create
        return new Image(created, bucket, id, path, uri);
    }
}
