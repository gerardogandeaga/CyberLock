package com.gerardogandeaga.cyberlock.storage;

import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.gerardogandeaga.cyberlock.App;
import com.gerardogandeaga.cyberlock.GlideApp;
import com.gerardogandeaga.cyberlock.objects.savable.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author gerardogandeaga
 * created on 2018-08-17
 */
public class ImageSaver {
    private static final String TAG = "ImageSaver";
    private static final String IMAGE_DIRECTORY = "image_data";

    public String saveMediaToInternalStorage(Image image) throws FileNotFoundException, IOException {
        ContextWrapper wrapper = new ContextWrapper(App.getContext());
        // path to /data/data/app/app_data/image_dir
        File directory = wrapper.getDir(IMAGE_DIRECTORY, ContextWrapper.MODE_PRIVATE);
        // create imageDir
        File path = new File(directory, image.getId());

        try (FileOutputStream out = new FileOutputStream(path)) {
            // compress the bitmap object to write image to the OutputStream
            Bitmap bitmap = GlideApp
                    .with(App.getContext())
                    .asBitmap()
                    .load(image.getCurrentUri())
                    .submit()
                    .get();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            Log.e(TAG, "saveMediaToInternalStorage: could not store image in internal storage");
        }

        return directory.getAbsolutePath();
    }


    private Bitmap loadImageFromInternalStorage(Image image) throws FileNotFoundException {
        File file = new File(image.getCurrentUri(), "profile.jpg");
        return BitmapFactory.decodeStream(new FileInputStream(file));
    }
}
