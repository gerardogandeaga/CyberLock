package com.gerardogandeaga.cyberlock.util;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author gerardogandeaga
 * created on 2018-08-18
 */
public class Storage {
    private static final String TAG = "Storage";
    // directories
    public static final String FILES_DIR = "/data/user/0/com.gerardogandeaga.cyberlock/files/";
    public static final String IMAGE_DIRECTORY = "secure_image_dir";

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
        public static void initApplicationDirectories() {
            File imageDir = new File(FILES_DIR, IMAGE_DIRECTORY);
            if (!imageDir.exists()) {
                imageDir.mkdir();
            }
            System.out.println("initApplicationDirectories: | " + imageDir.exists() + " | " + imageDir.getAbsolutePath());
        }

        /**
         * moves file to new location and returns new complete path to the moved file
         * @param srcPath absolute path of source file
         * @param destPath directory of where the file is to be moved
         * @return new absolute path to the file
         */
        public static String moveFile(String srcPath, String destPath) {
            try {
                File srcFile = new File(srcPath);
                File destFile = new File(destPath);
                File newFile = new File(destFile, srcFile.getName()); // create new file with same name as old
                try (FileChannel outputChannel = new FileOutputStream(newFile).getChannel();
                     FileChannel inputChannel = new FileInputStream(srcFile).getChannel()) {
                    // move file to new directory
                    inputChannel.transferTo(0, inputChannel.size(), outputChannel);
                    inputChannel.close();
                    // delete the old file
//                srcFile.delete();
                    System.out.println(
                            "moving: " + srcFile.getAbsolutePath() + " -> " + newFile.getAbsolutePath()
                    );
                    return newFile.getAbsolutePath();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
