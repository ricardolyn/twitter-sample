package com.ricardo.twitterpic.ui.preview;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.ricardo.twitterpic.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImagePreviewActivity extends AppCompatActivity {

    public static final String PARAM_IMAGE_URL = "PARAM_IMAGE_URL";
    public static final String PARAM_HANDLE = "PARAM_HANDLE";
    public static final String PARAM_PROFILE_IMAGE_URL = "PARAM_PROFILE_IMAGE_URL";

    @BindView(R.id.tv_handle)
    TextView tvHandle;
    @BindView(R.id.iv_preview)
    ImageView imageView;
    @BindView(R.id.iv_profile)
    ImageView imageProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        ButterKnife.bind(this);

        final String url = getIntent().getStringExtra(PARAM_IMAGE_URL);
        final String handle = getIntent().getStringExtra(PARAM_HANDLE);
        final String profileUrl = getIntent().getStringExtra(PARAM_PROFILE_IMAGE_URL);
        tvHandle.setText("@" + handle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }

        Picasso.with(this).load(url).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                scheduleStartPostponedTransition(imageView);
                Picasso.with(ImagePreviewActivity.this).load(profileUrl).into(imageProfile);
            }

            @Override
            public void onError() {

            }
        });
    }

    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }

    @OnClick(R.id.activity_image_preview)
    public void onClickRoot() {
        finish();
    }

}
