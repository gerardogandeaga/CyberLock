package com.gerardogandeaga.cyberlock.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author gerardogandeaga
 * created on 2018-08-18
 */
public class Storage {
    private static final String TAG = "Storage";

    private static final String IMAGE_DIRECTORY = "secure_image_dir";

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

        private void moveFile(String inputPath, String inputFile, String outputPath) throws IOException {
            InputStream in = null;
            OutputStream out = null;
            try {
                // create output directory if it doesn't exist
                File dir = new File(outputPath);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                in = new FileInputStream(inputPath + inputFile);
                out = new FileOutputStream(outputPath + inputFile);

                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                in = null;
                // write the output file
                out.flush();
                out.close();
                out = null;
                // delete te original file
                new File(inputPath + inputFile).delete();
            } catch (FileNotFoundException e) {
                Log.e(TAG, "moveFile: could not find file (" + inputFile + ")");
            } catch (Exception e) {
                Log.e(TAG, "moveFile: something went wrong... -> " + e.getMessage());
            }
        }
    }
}
