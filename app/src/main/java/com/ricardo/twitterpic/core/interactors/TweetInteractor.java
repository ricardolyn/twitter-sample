package com.ricardo.twitterpic.core.interactors;

import com.ricardo.twitterpic.core.models.Tweet;

import java.util.List;

public interface TweetInteractor extends Interactor {
    void searchImageTweets(String search, Listener<List<Tweet>> listener);
}
