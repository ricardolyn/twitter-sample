package com.ricardo.twitterpic.core.models;

import java.util.List;



public class SearchResponse {
    private final List<Tweet> statuses;

    public SearchResponse(List<Tweet> statuses) {
        this.statuses = statuses;
    }

    public List<Tweet> getStatuses() {
        return statuses;
    }
}
