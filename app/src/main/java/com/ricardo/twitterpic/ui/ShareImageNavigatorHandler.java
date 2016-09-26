package com.ricardo.twitterpic.ui;

import android.view.View;

import com.ricardo.twitterpic.core.models.Tweet;

public interface ShareImageNavigatorHandler {

    void openMediaPreview(Tweet tweet, View imageView);
}
