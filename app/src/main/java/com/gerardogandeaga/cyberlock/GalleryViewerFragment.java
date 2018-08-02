package com.gerardogandeaga.cyberlock;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * @author gerardogandeaga
 * created on 2018-08-01
 */
public class GalleryViewerFragment extends Fragment {
    private GridView mGallery;
    private ArrayList<String> mImagesPaths;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (isSDPresent) {
            // grid view settings
            mGallery = new GridView(getActivity());
            mGallery.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mGallery.setColumnWidth(280);
            mGallery.setGravity(Gravity.CENTER);
            mGallery.setHorizontalSpacing(2);
            mGallery.setNumColumns(2);
            mGallery.setPadding(5, 5, 5, 5);
            mGallery.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            mGallery.setVerticalSpacing(2);

            mGallery.setAdapter(new ImageAdapter(getActivity()));
            mGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (null != mImagesPaths && !mImagesPaths.isEmpty()) {
                        Toast.makeText(getActivity(), "position " + position + " " + mImagesPaths.get(position), Toast.LENGTH_LONG).show();
                    }
                }
            });

            Toast.makeText(getActivity(), "SD card detected!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "no storage detected", Toast.LENGTH_LONG).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return mGallery;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    class ImageAdapter extends BaseAdapter {
        Context mContext;

        ImageAdapter(Context context) {
            mContext = context;
            mImagesPaths = getImagePaths();
        }

        @Override
        public int getCount() {
            return mImagesPaths.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(
                        new GridView.LayoutParams(270, 270)
                );
            } else {
                imageView = (ImageView) convertView;
            }

            Glide.with(mContext).load(mImagesPaths.get(position)).into(imageView);

            return imageView;
        }

        private ArrayList<String> getImagePaths() {
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
            // stores all the mImagesPaths from the gallery in cursor
            Cursor cursor = getActivity().getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    columns,
                    null,
                    null,
                    MediaStore.Images.Media._ID
            );
            // total number of mImagesPaths
            int count = cursor.getCount();

            // create an array to store path to all mImagesPaths
            ArrayList<String> imagePaths = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                cursor.moveToPosition(i);
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                // store the path of the image
                imagePaths.add(i, cursor.getString(dataColumnIndex));
                Log.i("PATH: ", imagePaths.get(i));
            }
            // the cursor should be freed up
            cursor.close();

            return imagePaths;
        }
    }
}
