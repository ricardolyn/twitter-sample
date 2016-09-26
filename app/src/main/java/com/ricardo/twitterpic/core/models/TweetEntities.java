package com.ricardo.twitterpic.core.models;

import java.util.List;

public class TweetEntities {
    private final List<Media> media;

    public TweetEntities(List<Media> media) {
        this.media = media;
    }

    public List<Media> getMedia() {
        return media;
    }
}
