package com.gerardogandeaga.cyberlock.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gerardogandeaga.cyberlock.GlideApp;
import com.gerardogandeaga.cyberlock.MediaFetcher;
import com.gerardogandeaga.cyberlock.R;
import com.gerardogandeaga.cyberlock.objects.Bucket;
import com.gerardogandeaga.cyberlock.objects.Image;
import com.gerardogandeaga.cyberlock.util.Filter;
import com.gerardogandeaga.cyberlock.util.Scale;

import net.idik.lib.slimadapter.SlimAdapter;
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

        // recycler view
        GalleryRecyclerView = new RecyclerView(this);
        GalleryRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        GalleryRecyclerView.setLayoutManager(GalleryLayoutManager);

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

    // fragments

    public static class ImagesFragment extends Fragment {
        public static final String TAG = "imagesFragment";

        private SlimAdapter mSlimAdapter;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

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
            double displayWidth = Scale.convertPixelsToDp(Scale.getScreenWidth(getActivity()));
            final int numColumns =  (int) displayWidth / 120;

            // create slim adapter and logic

            GalleryLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return mSlimAdapter.getItem(position) instanceof Image ? 1 : numColumns;
                }
            });

            GalleryRecyclerView.setAdapter(null);
            mSlimAdapter = SlimAdapter.create()
                    .register(R.layout.item_image, new SlimInjector<Image>() {
                        @Override
                        public void onInject(final Image data, @NonNull IViewInjector injector) {
                            injector
                                    .with(R.id.imageview, new IViewInjector.Action() {
                                        @Override
                                        public void action(View view) {
                                            GlideApp.with(view.getContext())
                                                    .load(data.getUri())
                                                    .fitCenter()
                                                    .into((ImageView) view);
                                        }
                                    })
                                    .clicked(R.id.imageview, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(v.getContext(), "Image Uri : " + data.getUri(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    })
                    .attachTo(GalleryRecyclerView);
            mSlimAdapter.updateData(ImageArrayList);
        }
    }

    public static class AlbumsFragment extends Fragment {
        private static final String TAG = "albumsFragment";

        private SlimAdapter mSlimAdapter;
        private RecyclerView mRecyclerView;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            initRecyclerView();
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            return mRecyclerView;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }

        /**
         * initializes grid view with properties to best display photos
         */
        private void initRecyclerView() {
            double displayWidth = Scale.convertPixelsToDp(Scale.getScreenWidth(getActivity()));
            final int numColumns =  (int) displayWidth / 100;

            // create slim adapter and logic


            GalleryLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return mSlimAdapter.getItem(position) instanceof Image ? 1 : numColumns;
                }
            });

            GalleryRecyclerView.setAdapter(null);
            mSlimAdapter = SlimAdapter.create()
                    .register(R.layout.item_album, new SlimInjector<Bucket>() {
                        @Override
                        public void onInject(final Bucket data, @NonNull IViewInjector injector) {
                            injector
                                    .with(R.id.imageview, new IViewInjector.Action() {
                                        @Override
                                        public void action(View view) {
                                            GlideApp.with(view.getContext())
                                                    .load(data.getCoverImageUri())
                                                    .fitCenter()
                                                    .into((ImageView) view);
                                        }
                                    })
                                    .clicked(R.id.imageview, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(v.getContext(), "Name : " + data.getName(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    })
                    .attachTo(GalleryRecyclerView);
            mSlimAdapter.updateData(ImageArrayList);
        }

        /**
         * adapter that fetches ImageArrayList from the sd card
         */
        class ImageAdapter extends BaseAdapter {

            @Override
            public int getCount() {
                return BucketArrayList.size();
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
            public View getView(final int position, View convertView, ViewGroup parent) {
                Bucket bucket = BucketArrayList.get(position);
                // custom views
                LinearLayout container;
                ImageView cover;
                TextView name;

                if (convertView == null) {
                    int metrics = ((GridView) parent).getColumnWidth();

                    container = new LinearLayout(getActivity());
                    cover = new ImageView(getActivity());
                    name = new TextView(getActivity());

                    // linear layout
                    container.setOrientation(LinearLayout.VERTICAL);
                    // image view
                    cover.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    cover.setLayoutParams(new GridView.LayoutParams(metrics, metrics));
                    // text view
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        name.setTextAppearance(R.style.MyText_List_AlbumName);
                    }
                    name.setPadding(2, 2, 2, 2);
                    name.setMaxLines(1);
                    name.setEllipsize(TextUtils.TruncateAt.END);
                    name.setLayoutParams(new GridView.LayoutParams(metrics, ViewGroup.LayoutParams.WRAP_CONTENT));

                    // add views to container
                    container.addView(cover);
                    container.addView(name);

                    // final container size adjustments
                    container.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                } else {
                    container = (LinearLayout) convertView;
                    cover = (ImageView) container.getChildAt(0);
                    name = (TextView) container.getChildAt(1);
                }

                name.setText(bucket.getName());

                // load image into image view
                GlideApp.with(getActivity())
                        .load(bucket.getCoverImageUri())
                        .fitCenter()
                        .into(cover);

                return container;
            }
        }
    }
}
