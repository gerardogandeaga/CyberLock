package com.gerardogandeaga.cyberlock.store.database;

import android.content.ContentValues;

import com.gerardogandeaga.cyberlock.interfaces.DBImageBucketConstants;
import com.gerardogandeaga.cyberlock.objects.Bucket;

import net.sqlcipher.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gerardogandeaga
 * created on 2018-08-23
 *
 * todo finish bucket accessor
 */
public class DBImageBucketAccessor extends DBAccessor<Bucket> implements DBImageBucketConstants {

    private DBImageBucketAccessor() {
        super();
    }

    public static synchronized DBImageBucketAccessor getInstance() {
        return (DBImageBucketAccessor) (INSTANCE = (INSTANCE == null || !(INSTANCE instanceof DBImageBucketAccessor) ? new DBImageBucketAccessor() : INSTANCE));
    }

    @Override
    public void save(Bucket bucket) {
        ContentValues values = new ContentValues();
        values.put(KEY_BUCKET_NAME,      bucket.getName());
        values.put(KEY_BUCKET_COVER_URI, bucket.getCoverUri());

        getDatabase().insert(TABLE_IMAGE_BUCKETS, null, values);
    }

    @Override
    public void save(List<Bucket> bucketList) {
        for (Bucket bucket : bucketList) {
            save(bucket);
        }
    }

    @Override
    public void remove(Bucket bucket) {
        getDatabase().delete(TABLE_IMAGE_BUCKETS, UNIQUE_ID + " = ?", new String[]{Integer.toString(bucket.getId())});
    }

    @Override
    public void remove(List<Bucket> bucketList) {
        for (Bucket bucket : bucketList) {
            remove(bucket);
        }
    }

    @Override
    public List<Bucket> getAllItems() {
        List<Bucket> bucketList = new ArrayList<>();
        Cursor cursor = getDatabase().rawQuery(QUERY_ORDER_ID_DESC, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            bucketList.add(buildItem(cursor));
            cursor.moveToNext();
        }
        return bucketList;
    }

    @Override
    public Bucket buildItem(Cursor cursor) {
        int id =       cursor.getInt(POS_UNIQUE_ID);
        String name =     cursor.getString(POS_BUCKET_NAME);
        String coverUri = cursor.getString(POS_COVER_URI);
        // create
        return new Bucket(id, name, coverUri);
    }
}
