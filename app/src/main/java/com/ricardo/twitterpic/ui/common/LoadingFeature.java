
package com.ricardo.twitterpic.ui.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import com.ricardo.twitterpic.ui.mvp.Feature;

import java.lang.ref.WeakReference;

public class LoadingFeature implements Feature {

    private final WeakReference<Activity> mActivityRef;
    private ProgressDialog progress;

    private LoadingFeature(final Activity activity) {
        this.mActivityRef = new WeakReference<>(activity);
    }

    public synchronized static LoadingFeature get(final Activity activity) {
        return new LoadingFeature(activity);
    }

    public void setLoading(final boolean enabled) {
        if (enabled) {
            showLoadingDialog();
        } else {
            dismissLoadingDialog();
        }
    }

    private void showLoadingDialog() {
        final Activity activity = mActivityRef.get();
        if (progress == null && activity != null) {
            progress = new ProgressDialog(activity);
            progress.setMessage("Loading...");
            progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    final Activity activity = mActivityRef.get();
                    if (activity != null) {
                        activity.finish();
                    }
                }
            });
        }
        progress.show();
    }

    private void dismissLoadingDialog() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }

    @Override
    public void release() {
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }
}
