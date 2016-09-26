package com.ricardo.twitterpic.ui.auth;

import com.ricardo.twitterpic.ui.mvp.UIContainer;

public interface AuthReadyUi extends UIContainer {

    void onAuthReady();

    void onAuthFailed();
}
