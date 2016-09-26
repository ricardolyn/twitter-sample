package com.ricardo.twitterpic.core.models;

public class Token {

    private final String access_token;

    public Token(String access_token) {
        this.access_token = access_token;
    }

    public String getAccessToken() {
        return access_token;
    }
}
