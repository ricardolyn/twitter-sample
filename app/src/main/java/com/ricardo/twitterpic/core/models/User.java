package com.ricardo.twitterpic.core.models;

/**
 * Created by Ricardo Silva (ricardo@monadtek.com)
 * Monad Tek Ltd. 2016
 */

public class User {
    private final String screen_name;
    private final String profile_image_url;

    public User(String screen_name, String profile_image_url) {
        this.screen_name = screen_name;
        this.profile_image_url = profile_image_url;
    }

    public String getScreenName() {
        return screen_name;
    }

    public String getProfileImageUrl() {
        return profile_image_url;
    }
}
