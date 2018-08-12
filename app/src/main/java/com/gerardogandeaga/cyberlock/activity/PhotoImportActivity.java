package com.gerardogandeaga.cyberlock.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.gerardogandeaga.cyberlock.GlideApp;
import com.gerardogandeaga.cyberlock.MediaFetcher;
import com.gerardogandeaga.cyberlock.R;
import com.gerardogandeaga.cyberlock.decorations.PhotoItemDecoration;
import com.gerardogandeaga.cyberlock.objects.Bucket;
import com.gerardogandeaga.cyberlock.objects.Image;
import com.gerardogandeaga.cyberlock.util.Filter;
import com.gerardogandeaga.cyberlock.util.Scale;

import net.idik.lib.slimadapter.SlimAdapter;
import net.idik.lib.slimadapter.SlimAdapterEx;
import net.idik.lib.slimadapter.SlimInjector;
import net.idik.lib.slimadapter.viewinjector.IViewInjector;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author gerardogandeaga
 * created on 2018-07-28
 */
enum ImageImportState {
    ALBUM_VIEW,
    IMAGE_VIEW
}
public class PhotoImportActivity extends AppCompatActivity {
    // select all interface
    interface OnMenuItemClickedListener {
        void onAllSelected();
    }
    private OnMenuItemClickedListener mMenuItemClickedListener;

    private static AppCompatActivity Activity;
    private static FragmentManager Manager;

    private static ArrayList<Image> ImageArrayList;
    private static ArrayList<Bucket> BucketArrayList;
    private static ArrayList<Image> SelectedImages;

    private static ImageImportState State = ImageImportState.ALBUM_VIEW;

    private static GridLayoutManager GalleryLayoutManager;
    private static SlimAdapter GallerySlimAdapter;
    private static RecyclerView GalleryRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.container)
    FrameLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_only);
        ButterKnife.bind(this);

        Activity = this;

        // request image information
        ImageArrayList = MediaFetcher.requestImages(null);
        BucketArrayList = MediaFetcher.requestAlbums();

        // prepare fragment
        Manager = getFragmentManager();

        // initialize views
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // grid layout dimensions
        final double displayWidth = Scale.convertPixelsToDp(Scale.getScreenWidth(this));
        final int numColumns = (int) displayWidth / 120;
        final int widthColumns = (int) displayWidth / numColumns;
        GalleryLayoutManager = new GridLayoutManager(this, numColumns);
        GalleryLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                Object object = GallerySlimAdapter.getItem(position);
                if (object instanceof Bucket || object instanceof Image) {
                    return 1;
                } else {
                    return numColumns;
                }
            }
        });

        // recycler view
        GalleryRecyclerView = new RecyclerView(this);
        GalleryRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        GalleryRecyclerView.setLayoutManager(GalleryLayoutManager);
        GalleryRecyclerView.addItemDecoration(new PhotoItemDecoration(numColumns, 8));

        switchFragments(AlbumsFragment.TAG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_import, menu);
        Filter.filterMenu(menu, R.color.white);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                switch (State) {
                    case ALBUM_VIEW:
                        Toast.makeText(this, "back!", Toast.LENGTH_SHORT).show();
                        break;
                    case IMAGE_VIEW:
                        switchFragments(AlbumsFragment.TAG);
                        break;
                }
                return true;

            default: return false;
        }
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
                transaction.replace(container, new ImagesFragment());
                State = ImageImportState.IMAGE_VIEW;
                break;
            case AlbumsFragment.TAG:
                transaction.replace(container, new AlbumsFragment());
                State = ImageImportState.ALBUM_VIEW;
                Activity.getSupportActionBar().setTitle("Albums");
                Activity.getSupportActionBar().setSubtitle(null);
                break;
        }

        transaction.commit();
    }

    // image selections

    class Selection {
        ArrayList<Image> mSelectedImages;

        Selection() {
            mSelectedImages = new ArrayList<>();
        }

        void select(Image image) {
            if (!mSelectedImages.contains(image)) {
                // add new image to array
                mSelectedImages.add(image);
            }
        }

        void selectAll(ArrayList<Image> images) {
            mSelectedImages = images;
        }

        void deselect(Image image) {
            if (mSelectedImages.contains(image)) {
                mSelectedImages.remove(image);
            }
        }

        void deselectAll() {
            mSelectedImages = new ArrayList<>();
        }

        boolean isSelected(Image image) {
            return mSelectedImages.contains(image);
        }
    }

    // fragments

    public static class ImagesFragment extends Fragment {
        public static final String TAG = "imagesFragment";

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);

            Activity.getSupportActionBar().setSubtitle(ImageArrayList.size() + " Images");
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

        /**
         * initializes grid view with properties to best display photos
         */
        private void initRecyclerView() {
            // create slim adapter and logic
            GallerySlimAdapter = SlimAdapter.create()
                    .register(R.layout.item_image, new SlimInjector<Image>() {
                        @Override
                        public void onInject(@NonNull final Image data, @NonNull final IViewInjector injector) {
                            injector.with(R.id.imgImage, new IViewInjector.Action() {
                                        @Override
                                        public void action(View view) {
                                            GlideApp.with(view.getContext())
                                                    .load(data.getUri())
                                                    .fitCenter()
                                                    .into((ImageView) view);
                                        }
                                    })
                                    .clicked(R.id.imgImage, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(v.getContext(), "Image Uri : " + data.getUri(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    })
                    .attachTo(GalleryRecyclerView);
            GallerySlimAdapter.updateData(ImageArrayList);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == R.id.mnu_select_all) {
                Toast.makeText(getActivity(), "Hi", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    }

    public static class AlbumsFragment extends Fragment {
        private static final String TAG = "albumsFragment";

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

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

        /**
         * initializes grid view with properties to best display photos
         */
        private void initRecyclerView() {
            // create slim adapter and logic
            GallerySlimAdapter = SlimAdapter.create(SlimAdapterEx.class)
                    .register(R.layout.item_album, new SlimInjector<Bucket>() {
                        @Override
                        public void onInject(@NonNull final Bucket data, @NonNull IViewInjector injector) {
                            injector.text(R.id.tvTitle, data.getName())
                                    .with(R.id.imgImage, new IViewInjector.Action() {
                                        @Override
                                        public void action(View view) {
                                            GlideApp.with(view.getContext())
                                                    .load(data.getCoverImageUri())
                                                    .fitCenter()
                                                    .into((ImageView) view);
                                        }
                                    })
                                    .clicked(R.id.imgImage, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ImageArrayList = MediaFetcher.requestImages(data.getName());
                                            switchFragments(ImagesFragment.TAG);
                                        }
                                    });
                        }
                    })
                    .attachTo(GalleryRecyclerView);
            GallerySlimAdapter.updateData(BucketArrayList);
        }
    }
}
