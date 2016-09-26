package com.ricardo.twitterpic.core.models;

public class Media {
    private final String media_url;
    private final MediaSizes sizes;

    public Media(String media_url, MediaSizes sizes) {
        this.media_url = media_url;
        this.sizes = sizes;
    }

    public String getThumbMediaUrl() {
        return media_url + ":thumb";
    }

    public String getMediaUrl() {
        return media_url;
    }

    public MediaSizes getSizes() {
        return sizes;
    }
}

