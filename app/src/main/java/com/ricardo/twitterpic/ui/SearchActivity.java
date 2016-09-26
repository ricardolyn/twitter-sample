package com.ricardo.twitterpic.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ricardo.twitterpic.R;
import com.ricardo.twitterpic.core.SharedPrefStore;
import com.ricardo.twitterpic.core.Store;
import com.ricardo.twitterpic.core.models.Media;
import com.ricardo.twitterpic.core.models.Tweet;
import com.ricardo.twitterpic.core.models.User;
import com.ricardo.twitterpic.ui.auth.AuthPresenter;
import com.ricardo.twitterpic.ui.auth.AuthReadyUi;
import com.ricardo.twitterpic.ui.common.GridAutofitLayoutManager;
import com.ricardo.twitterpic.ui.common.LoadingFeature;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

public class SearchActivity extends AppCompatActivity implements AuthReadyUi, ImagesSearchUi, ShareImageNavigatorHandler {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.bt_search)
    Button btSearch;
    @BindView(R.id.rv_images)
    RecyclerView rvImages;

    private LoadingFeature loadingFeature;
    private AuthPresenter authPresenter;
    private ImagesSearchPresenter imagesSearchPresenter;
    private SearchListAdapter searchListAdapter;
    private GridAutofitLayoutManager layoutManager;
    private PicassoTarget picassoTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        ButterKnife.setDebug(true);
        ButterKnife.bind(this);

        Store store = SharedPrefStore.create(this);
        loadingFeature = LoadingFeature.get(this);
        authPresenter = AuthPresenter.create(store);
        imagesSearchPresenter = ImagesSearchPresenter.create();

        // set init view state
        prepareViews();
        rvImages.setVerticalScrollBarEnabled(true);
        rvImages.setItemAnimator(new LandingAnimator());

        searchListAdapter = new SearchListAdapter(imagesSearchPresenter, this);
        rvImages.setAdapter(new AlphaInAnimationAdapter(searchListAdapter));

        // init presenters
        authPresenter.init(this);
        imagesSearchPresenter.init(this);

        // init layout manager for grid
        layoutManager = new GridAutofitLayoutManager(this, imagesSearchPresenter.getItemMaxWidth());
        layoutManager.setAutoMeasureEnabled(true);
        rvImages.setLayoutManager(layoutManager);

        // load
        authPresenter.load();
        imagesSearchPresenter.restore(savedInstanceState != null);
    }

    private void prepareViews() {
        btSearch.setEnabled(false);
        etSearch.setEnabled(false);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onSearch();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loadingFeature.release();
    }

    @Override
    public void setLoading(boolean enabled) {
        loadingFeature.setLoading(enabled);
    }

    @OnClick(R.id.bt_search)
    public void onSearch() {
        hideKeyboardFrom(this, etSearch);
        imagesSearchPresenter.search(etSearch.getText().toString());
    }

    @Override
    public void onAuthReady() {
        btSearch.setEnabled(true);
        etSearch.setEnabled(true);
    }

    @Override
    public void onAuthFailed() {
        Toast.makeText(this, getString(R.string.error_auth), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTweetImagesReady(int count, int maxWidth) {
        searchListAdapter.notifyDataSetChanged();
        layoutManager.setColumnWidth(maxWidth);
    }

    @Override
    public void onFailedImageSearch() {
        Toast.makeText(this, getString(R.string.error_image_search), Toast.LENGTH_SHORT).show();
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void openMediaPreview(final Tweet tweet, final View view) {
        if (picassoTarget != null && !picassoTarget.isDone) {
            // ignore until finishes the current one
            return;
        }

        //trick loading
        final ImageView imageView = (ImageView) view;
        picassoTarget = new PicassoTarget(this, tweet, imageView);

        final Media media = tweet.getEntities().getMedia().get(0);
        Picasso.with(this).load(media.getMediaUrl()).into(picassoTarget);
    }

    /**
     * Used to load the Full image on Picasso before opening the Preview Activity
     * Some code adds delays to trick the loading feeling with animation
     */
    private static class PicassoTarget implements Target {
        private static final int DELAY_PICASSO_THUMB_LOAD = 150;

        private final WeakReference<Activity> activityRef;
        private final WeakReference<ImageView> imageViewRef;
        private final Tweet tweet;

        private boolean isDone;

        private PicassoTarget(Activity activity, Tweet tweet, ImageView imageView) {
            this.activityRef = new WeakReference<>(activity);
            this.tweet = tweet;
            this.imageViewRef = new WeakReference<>(imageView);
        }

        private Media getMedia() {
            return tweet.getEntities().getMedia().get(0);
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            final Activity activity = activityRef.get();
            final ImageView imageView = imageViewRef.get();
            if (activity == null || imageView == null) {
                return;
            }

            final User user = tweet.getUser();
            startIntent(activity, user.getScreenName(), user.getProfileImageUrl(), getMedia(), imageView);
            loadThumbDelayed(imageView);
            isDone = true;
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            final Activity activity = activityRef.get();
            final ImageView imageView = imageViewRef.get();
            if (activity == null || imageView == null) {
                return;
            }

            if (runnable != null) {
                imageView.removeCallbacks(runnable);
            }

            loadThumb(activity, getMedia(), imageView);
            isDone = true;
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            final ImageView imageView = imageViewRef.get();
            if (imageView == null) {
                return;
            }
            imageView.setImageResource(R.drawable.progress_animation);
        }

        //Utility methods

        private static void startIntent(Activity activity, String handle, String profileUrl, Media media, ImageView imageView) {
            Intent intent = new Intent(activity, ImagePreviewActivity.class);
            intent.putExtra(ImagePreviewActivity.PARAM_IMAGE_URL, media.getMediaUrl());
            intent.putExtra(ImagePreviewActivity.PARAM_PROFILE_IMAGE_URL, profileUrl);
            intent.putExtra(ImagePreviewActivity.PARAM_HANDLE, handle);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(activity, imageView, activity.getString(R.string.activity_image_trans));
                activity.startActivity(intent, options.toBundle());
            } else {
                activity.startActivity(intent);
            }
        }

        private void loadThumbDelayed(ImageView imageView) {
            // trick animation open activity
            imageView.postDelayed(runnable, DELAY_PICASSO_THUMB_LOAD);
        }

        private void loadThumb(Activity activity, Media media, ImageView imageView) {
            Picasso.with(activity).load(media.getThumbMediaUrl()).noFade().into(imageView);
        }

        private Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Activity activity = activityRef.get();
                final ImageView imageView = imageViewRef.get();
                if (activity == null || imageView == null) {
                    return;
                }
                loadThumb(activity, getMedia(), imageView);
            }
        };
    }
}
