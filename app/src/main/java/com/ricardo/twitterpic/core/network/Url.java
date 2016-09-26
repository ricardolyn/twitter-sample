package com.ricardo.twitterpic.core.network;

import com.ricardo.twitterpic.BuildConfig;

public class Url {
    public static final String BASE_URL = "https://" + BuildConfig.HOST;

    public static final String OAUTH_TOKEN = BASE_URL + "/oauth2/token";
    public static final String TWEETS_SEARCH = BASE_URL + "/1.1/search/tweets.json";
}