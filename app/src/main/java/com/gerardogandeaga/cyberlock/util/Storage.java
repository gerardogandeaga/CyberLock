package com.gerardogandeaga.cyberlock.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.gerardogandeaga.cyberlock.objects.savable.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * @author gerardogandeaga
 * created on 2018-08-18
 */
public class Storage {
    private static final String TAG = "Storage";

    private static final String PACKAGE = "/com.gerardogandeaga.cyberlock/";
    // directories
    public static final String IMAGE_DIRECTORY = Environment.getExternalStorageDirectory() + PACKAGE + "secure_image_dir";

    /**
     * @return returns true if an sd card is detected and ready to be used, false otherwise
     */
    private static boolean isSDPresent() {
        return android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * saving files device storage
     */
    public static class FileManager {

        /**
         * creates application directories on initial launch of application
         */
        public static void initApplicationDirectories(Context context) throws FileNotFoundException {
            // image dir
            File dir = new File(Environment.getExternalStorageDirectory(), "secure_image_dir");
            if (!dir.exists()) {
                if (!dir.mkdir()) {
                    throw new FileNotFoundException("image dir was not created");
                }
            } else {
                Log.i(TAG, "initApplicationDirectories: image dir already exists");
            }
        }

        /**
         * move images to "secure_image_dir" directory and updates uri to new dir
         * @param images images to be moved and updated
         */
        public static void storeImages(ArrayList<Image> images) {
            for (Image image : images) {
                // set original uri to current
                image.withOriginalUri(image.getCurrentUri());
                // move image to new dir and set the resulting uri to current uri
                image.withCurrentUri(moveFile(image.getOriginalUri(), Storage.IMAGE_DIRECTORY));
            }
        }

        /**
         * moves file to new location and returns new complete path to the moved file
         * @param srcUri absolute path of source file
         * @param destination directory of where the file is to be moved
         * @return new absolute path to the file
         */
        public static String moveFile(String srcUri, String destination) {
            try {
                File srcFile = new File(srcUri);
                File destFile = new File(destination);
                File newFile = new File(destFile, srcFile.getName()); // create new file with same name as old
                try (FileChannel outputChannel = new FileOutputStream(newFile).getChannel();
                     FileChannel inputChannel = new FileInputStream(srcFile).getChannel()) {
                    // move file to new directory
                    inputChannel.transferTo(0, inputChannel.size(), outputChannel);
                    inputChannel.close();
                    // delete the old file
//                srcFile.delete();

                    return newFile.getAbsolutePath();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
