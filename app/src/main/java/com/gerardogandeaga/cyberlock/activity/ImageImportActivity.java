package com.gerardogandeaga.cyberlock.activity;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.gerardogandeaga.cyberlock.PermissionRequester;
import com.gerardogandeaga.cyberlock.R;
import com.gerardogandeaga.cyberlock.dialogs.ImagePreviewDialog;
import com.gerardogandeaga.cyberlock.interfaces.OnButtonPressedListener;
import com.gerardogandeaga.cyberlock.lists.decorations.ImageItemDecoration;
import com.gerardogandeaga.cyberlock.lists.items.BucketItem;
import com.gerardogandeaga.cyberlock.lists.items.ImportImageItem;
import com.gerardogandeaga.cyberlock.objects.Bucket;
import com.gerardogandeaga.cyberlock.objects.savable.Image;
import com.gerardogandeaga.cyberlock.store.MediaFetcher;
import com.gerardogandeaga.cyberlock.store.database.DBImageAccessor;
import com.gerardogandeaga.cyberlock.util.Filter;
import com.gerardogandeaga.cyberlock.util.Scale;
import com.gerardogandeaga.cyberlock.util.Storage;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.ISelectionListener;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.fastadapter.listeners.OnLongClickListener;
import com.mikepenz.fastadapter.select.SelectExtension;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author gerardogandeaga
 * created on 2018-07-28
 *
 * ImageImportActivity handles importing of images from the phone photo library into sqlcipher db
 * permission checks for EXTERNAL STORAGE.
 */
public class ImageImportActivity extends AppCompatActivity {
    private static OnButtonPressedListener ButtonPressedListener;

    private static AppCompatActivity Activity;
    private static FragmentManager Manager;

    private static MenuInflater ActivityMenuInflater;

    // data
    private static List<Bucket> BucketList;
    private static Bucket CurrentBucket;

    // views
    private static RecyclerView GalleryRecyclerView;
    @BindView(R.id.toolbar)   Toolbar mToolbar;
    @BindView(R.id.container) FrameLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateProtocol();
    }

    /**
     * separate function so we can also create view after permissions have been requested and then granted
     */
    private void onCreateProtocol() {
        // here we need to check if we have permission to read storage data. if not then we have to cancel any requests
        if (!PermissionRequester.canReadExternalStorage(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // request permission
            PermissionRequester.requestReadExternalStorage(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            // ---------------- todo move permissions to the App.java and also init applications Directories
            Storage.FileManager.initApplicationDirectories();
            // ----------------

            setContentView(R.layout.activity_toolbar_only);
            ButterKnife.bind(this);

            // initialize views
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Activity = this;

            // request image information
            MediaFetcher.requestImages(null); // <- we call first to init the caches
            BucketList = MediaFetcher.requestAlbums();

            // prepare fragment
            Manager = getFragmentManager();

            // grid layout dimensions
            final double displayWidth = Scale.convertPixelsToDp(Scale.getScreenWidth(this));
            final int numColumns = (int) displayWidth / 120;
            GridLayoutManager galleryLayoutManager = new GridLayoutManager(this, numColumns);

            // recycler view
            GalleryRecyclerView = new RecyclerView(this);
            GalleryRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            GalleryRecyclerView.setLayoutManager(galleryLayoutManager);
            GalleryRecyclerView.addItemDecoration(new ImageItemDecoration(numColumns, 4));

            switchFragments(AlbumsFragment.TAG);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActivityMenuInflater = getMenuInflater();
        return true;
    }

    /**
     * switches between album and ImageArrayList fragments throught tags
     * @param fragTag name of fragment to start
     */
    public static void switchFragments(String fragTag) {
        int container = R.id.container;

        FragmentTransaction transaction = Manager.beginTransaction();

        switch (fragTag) {
            case ImageFragment.TAG:
                ImageFragment imageFrag = new ImageFragment();
                ButtonPressedListener = imageFrag;
                transaction.replace(container, imageFrag);
                break;
            case AlbumsFragment.TAG:
                AlbumsFragment albumFrag = new AlbumsFragment();
                ButtonPressedListener = albumFrag;
                transaction.replace(container, albumFrag);
                break;
        }

        transaction.commit();
    }

    /**
     * logic is handles in the fragments. this function is simply callback code
     */
    @Override
    public void onBackPressed() {
        ButtonPressedListener.onBackPressed();
    }

    // fragments

    public static class ImageFragment extends Fragment implements OnButtonPressedListener {
        public static final String TAG = "ImageFragment";

        private DBImageAccessor mDBAccessor;
        private List<Image> mProjectedImages;
        private FastItemAdapter<ImportImageItem> mAdapter;
        private SelectExtension<ImportImageItem> mAdapterSelector;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            this.mProjectedImages = MediaFetcher.requestImages(CurrentBucket.getName());
            this.mDBAccessor = DBImageAccessor.getInstance();

            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

            // recycler view adapter configs
            this.mAdapter = new FastItemAdapter<>();
            mAdapter.setHasStableIds(true);

            // selection
            this.mAdapterSelector = new SelectExtension<>();
            mAdapterSelector.init(mAdapter);

            // attach adapter
            GalleryRecyclerView.setAdapter(mAdapter);

            // selecting images
            mAdapter.withOnClickListener(new OnClickListener<ImportImageItem>() {
                @Override
                public boolean onClick(@Nullable View view, @NonNull IAdapter<ImportImageItem> adapter, @NonNull ImportImageItem item, int position) {
                    mAdapterSelector.toggleSelection(position);
                    return false;
                }
            });

            // previewing images
            mAdapter.withOnLongClickListener(new OnLongClickListener<ImportImageItem>() {
                @Override
                public boolean onLongClick(@NonNull View view, @NonNull IAdapter<ImportImageItem> adapter, @NonNull ImportImageItem item, int position) {
                    new ImagePreviewDialog(Activity, item.getImage().getCurrentUri()).showDialog();
                    return false;
                }
            });

            mAdapterSelector.withSelectionListener(new ISelectionListener<ImportImageItem>() {
                @Override
                public void onSelectionChanged(@Nullable ImportImageItem item, boolean selected) {
                    int count = mAdapterSelector.getSelectedItems().size();
                    // update the action bar title
                    Activity.getSupportActionBar().setSubtitle(
                            (count > 0) ? count + " / " + mProjectedImages.size() + " Selected" : mProjectedImages.size() + " Images"
                    );
                }
            });

            // create the list of image items from the images
            List<ImportImageItem> importImageItems = ImportImageItem.ItemBuilder.buildImages(mProjectedImages);
            mAdapter.add(importImageItems);
            
            // attach to fragment
            return GalleryRecyclerView;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            ActivityMenuInflater.inflate(R.menu.menu_image_import, menu);
            Filter.filterMenu(menu, R.color.white);
            // action titles
            Activity.getSupportActionBar().setTitle(CurrentBucket.getName());
            Activity.getSupportActionBar().setSubtitle(CurrentBucket.getSize() + " Images");
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    switchFragments(AlbumsFragment.TAG);
                    return true;

                case R.id.menu_done:
                    importSelectedImages();
                    return true;

                case R.id.menu_select_all:
                    selectAll();
                    return true;
            }
            return false;
        }

        private void importSelectedImages() {
            if (!mAdapterSelector.getSelectedItems().isEmpty()) {
                // store images and write data to db
                // todo progress bar
                final ProgressDialog load = new ProgressDialog(Activity);
                load.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                load.setCancelable(false);
                load.setProgress(0);
                load.setSecondaryProgress(0);
                load.setMessage("Importing Images");
                load.show();
                // save on new thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // create final array of selected images
                        final List<Image> selectedImages = new ArrayList<>();
                        for (ImportImageItem item : mAdapterSelector.getSelectedItems()) {
                            selectedImages.add(item.getImage());
                        }
                        load.setMax(selectedImages.size());
                        // start the writable process in the db
                        mDBAccessor.openWritable();
                        for (Image image : selectedImages) {
                            // add to db
                            mDBAccessor.save(image);
                            // increment progress
                            load.incrementProgressBy(1);
                            load.incrementSecondaryProgressBy(1);
                        }
                        // finalize
                        mDBAccessor.close();
                        load.dismiss(); // remove dialog
                    }
                }).start();
                // remove selections
                deselectAll();
            } else {
                Toast.makeText(Activity, "No Items Selected", Toast.LENGTH_SHORT).show();
            }
        }

        private void selectAll() {
            for (int i = 0; i < mAdapter.getItemCount(); i++) {
                if (!mAdapter.getItem(i).isSelected()) {
                    mAdapterSelector.select(i);
                }
            }
        }

        private void deselectAll() {
            for (int i = 0; i < mAdapter.getItemCount(); i++) {
                if (mAdapter.getItem(i).isSelected()) {
                    mAdapterSelector.deselect(i);
                }
            }
        }

        // back button listener

        @Override
        public void onBackPressed() {
            // first deselect all images if there are any selected images
            if (!mAdapterSelector.getSelectedItems().isEmpty()) {
                deselectAll();
                return;
            }

            // go back to albums
            switchFragments(AlbumsFragment.TAG);
        }
    }

    public static class AlbumsFragment extends Fragment implements OnButtonPressedListener {
        private static final String TAG = "AlbumsFragment";

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            // recycler view adapter configs
            FastItemAdapter<BucketItem> adapter = new FastItemAdapter<>();
            adapter.setHasStableIds(true);

            // attach to adapter
            GalleryRecyclerView.setAdapter(adapter);

            // selecting albums
            adapter.withOnClickListener(new OnClickListener<BucketItem>() {
                @Override
                public boolean onClick(@Nullable View v, @NonNull IAdapter<BucketItem> adapter, @NonNull BucketItem item, int position) {
                    CurrentBucket = item.getBucket();
                    switchFragments(ImageFragment.TAG);
                    return false;
                }
            });

            // create the list of image items from the images
            List<BucketItem> bucketItems = new BucketItem.Builder().buildItems(BucketList);
            adapter.add(bucketItems);
            
            // attach to fragment
            return GalleryRecyclerView;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            menu.clear();
            // action title
            Activity.getSupportActionBar().setTitle("Albums");
            Activity.getSupportActionBar().setSubtitle("Select album to import");
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == android.R.id.home) {
                Intent i = new Intent(Activity, MenuActivity.class);
                Activity.startActivity(i);
                Activity.finish();
                return true;
            }
            return false;
        }

        // back button listener

        @Override
        public void onBackPressed() {
            Intent i = new Intent(Activity, MenuActivity.class);
            startActivity(i);
            Activity.finish();
        }
    }

    /**
     * handles result of EXTERNAL STORAGE permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permit granted
                onCreateProtocol(); // continue create normally
            } else {
                // permit denied
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        }
    }
}
