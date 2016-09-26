package com.ricardo.twitterpic.ui;

import com.ricardo.twitterpic.ui.mvp.UIContainer;

public interface ImagesSearchUi extends UIContainer {
    void onTweetImagesReady(int count, int maxWidth);

    void onFailedImageSearch();
}
