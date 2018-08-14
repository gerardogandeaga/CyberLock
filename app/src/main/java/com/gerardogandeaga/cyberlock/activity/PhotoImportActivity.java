package com.gerardogandeaga.cyberlock.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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

import com.gerardogandeaga.cyberlock.MediaFetcher;
import com.gerardogandeaga.cyberlock.PermissionRequester;
import com.gerardogandeaga.cyberlock.R;
import com.gerardogandeaga.cyberlock.interfaces.OnButtonPressedListener;
import com.gerardogandeaga.cyberlock.lists.decorations.PhotoItemDecoration;
import com.gerardogandeaga.cyberlock.lists.items.AlbumItem;
import com.gerardogandeaga.cyberlock.lists.items.ImageItem;
import com.gerardogandeaga.cyberlock.objects.Bucket;
import com.gerardogandeaga.cyberlock.objects.Image;
import com.gerardogandeaga.cyberlock.util.Filter;
import com.gerardogandeaga.cyberlock.util.Scale;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.ISelectionListener;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.fastadapter.select.SelectExtension;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author gerardogandeaga
 * created on 2018-07-28
 */
public class PhotoImportActivity extends AppCompatActivity {
    private static final String TAG = "PhotoImportActivity";

    private static OnButtonPressedListener ButtonPressedListener;

    private static AppCompatActivity Activity;
    private static FragmentManager Manager;

    private static MenuInflater ActivityMenuInflater;

    // data
    private static ArrayList<Bucket> BucketArrayList;
    private static Bucket CurrentBucket;

    // views
    private static RecyclerView GalleryRecyclerView;
    @BindView(R.id.toolbar)   Toolbar mToolbar;
    @BindView(R.id.container) FrameLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // here we need to check if we have permission to read storage data. if not then we have to cancel any requests
        if (!PermissionRequester.canReadExternalStorage(this)) {
            // request permission
            PermissionRequester.requestReadExternalStorage(this);
        } else {
            setContentView(R.layout.activity_toolbar_only);
            ButterKnife.bind(this);

            // initialize views
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Activity = this;

            // request image information
            MediaFetcher.requestImages(null); // <- we call first to init the caches
            BucketArrayList = MediaFetcher.requestAlbums();

            // prepare fragment
            Manager = getFragmentManager();

            // grid layout dimensions
            final double displayWidth = Scale.convertPixelsToDp(Scale.getScreenWidth(this));
            final int numColumns = (int) displayWidth / 120;
            final int widthColumns = (int) displayWidth / numColumns;
            GridLayoutManager galleryLayoutManager = new GridLayoutManager(this, numColumns);

            // recycler view
            GalleryRecyclerView = new RecyclerView(this);
            GalleryRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            GalleryRecyclerView.setLayoutManager(galleryLayoutManager);
            GalleryRecyclerView.addItemDecoration(new PhotoItemDecoration(numColumns, 4));

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
            case ImagesFragment.TAG:
                ImagesFragment imageFrag = new ImagesFragment();
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

    @Override
    public void onBackPressed() {
        ButtonPressedListener.onBackPressed();
//        super.onBackPressed();
    }

    // fragments

    public static class ImagesFragment extends Fragment implements OnButtonPressedListener {
        public static final String TAG = "ImagesFragment";
        private ArrayList<Image> mViewingImages;
        private FastItemAdapter<ImageItem> mAdapter;
        private SelectExtension<ImageItem> mAdapterSelector;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            this.mViewingImages = MediaFetcher.requestImages(CurrentBucket.getName());
            initRecyclerView();

            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            return GalleryRecyclerView;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//            super.onCreateOptionsMenu(menu, inflater);
            ActivityMenuInflater.inflate(R.menu.menu_image_import, menu);
            Filter.filterMenu(menu, R.color.white);
            // action titles
            Activity.getSupportActionBar().setTitle(CurrentBucket.getName());
            Activity.getSupportActionBar().setSubtitle(mViewingImages.size() + " Images");
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    switchFragments(AlbumsFragment.TAG);
                    return true;

                case R.id.mnu_done:
                    importSelectedImages();
                    return true;

                case R.id.mnu_select_all:
                    selectAllImages();
                    return true;
            }
            return false;
        }

        /**
         * initializes grid view with properties to best display photos
         */
        private void initRecyclerView() {
            // recycler view adapter configs
            this.mAdapter = new FastItemAdapter<>();
            mAdapter.setHasStableIds(true);
            mAdapter.withSelectable(true);
            mAdapter.withMultiSelect(true);
            mAdapter.withSelectOnLongClick(true);
            // selection
            this.mAdapterSelector = new SelectExtension<>();
            mAdapterSelector.init(mAdapter);

            // attach adapter
            GalleryRecyclerView.setAdapter(mAdapter);

            // selecting images
            mAdapter.withOnClickListener(new OnClickListener<ImageItem>() {
                @Override
                public boolean onClick(@Nullable View view, @NonNull IAdapter<ImageItem> adapter, @NonNull ImageItem item, int position) {
                    mAdapterSelector.toggleSelection(position);
                    return true;
                }
            });

            mAdapterSelector.withSelectionListener(new ISelectionListener<ImageItem>() {
                @Override
                public void onSelectionChanged(@Nullable ImageItem item, boolean selected) {
                    int count = mAdapterSelector.getSelectedItems().size();
                    // update the action bar title
                    Activity.getSupportActionBar().setSubtitle(
                            (count > 0) ? count + " / " + mViewingImages.size() + " Selected" : mViewingImages.size() + " Images"
                    );
                }
            });

            // create the list of image items from the images
            ArrayList<ImageItem> imageItems = new ArrayList<>();
            for (Image image : mViewingImages) {
                imageItems.add(new ImageItem(image));
            }

            mAdapter.add(imageItems);
        }

        private void importSelectedImages() {
            if (!mAdapterSelector.getSelectedItems().isEmpty()) {
                ArrayList<Image> selectedImages = new ArrayList<>();
                for (ImageItem item : mAdapterSelector.getSelectedItems()) {
                    selectedImages.add(item.getImage());
                }
            }
        }

        private void selectAllImages() {
            for (int i = 0; i < mAdapter.getItemCount(); i++) {
                mAdapterSelector.select(i);
            }
        }

        // back button listener

        @Override
        public void onBackPressed() {
            // first deselect all images if there are any
            if (mAdapterSelector.getSelectedItems().isEmpty()) {
                for (int i = 0; i < mAdapter.getItemCount(); i++) {
                    mAdapterSelector.deselect(i);
                }
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
            initRecyclerView();
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            return GalleryRecyclerView;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//            super.onCreateOptionsMenu(menu, inflater);
            menu.clear();
            // action title
            Activity.getSupportActionBar().setTitle("Albums");
            Activity.getSupportActionBar().setSubtitle(null);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == android.R.id.home) {
                Toast.makeText(getActivity(), "Back", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        }

        /**
         * initializes grid view with properties to best display photos
         */
        private void initRecyclerView() {
            // recycler view adapter configs
            FastItemAdapter<AlbumItem> adapter = new FastItemAdapter<>();
            adapter.setHasStableIds(true);

            // attach to adapter
            GalleryRecyclerView.setAdapter(adapter);

            // selecting albums
            adapter.withOnClickListener(new OnClickListener<AlbumItem>() {
                @Override
                public boolean onClick(@Nullable View v, @NonNull IAdapter<AlbumItem> adapter, @NonNull AlbumItem item, int position) {
                    CurrentBucket = item.getBucket();
                    switchFragments(ImagesFragment.TAG);
                    return false;
                }
            });

            // create the list of image items from the images
            ArrayList<AlbumItem> albumItems = new ArrayList<>();
            for (Bucket bucket : BucketArrayList) {
                System.out.println(bucket);
                albumItems.add(new AlbumItem(bucket));
            }

            adapter.add(albumItems);
        }

        // back button listener

        @Override
        public void onBackPressed() {
            Toast.makeText(getActivity(), "Back!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permit granted
                recreate(); // recreate activity
            } else {
                // permit denied
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        }
    }
}
