package com.ricardo.twitterpic.core.models;

public class MediaSize {
    private final int w;
    private final int h;

    public MediaSize(int w, int h) {
        this.w = w;
        this.h = h;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }
}
