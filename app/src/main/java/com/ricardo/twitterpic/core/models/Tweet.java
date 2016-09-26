package com.ricardo.twitterpic.core.models;

public class Tweet {

    private final User user;
    private final TweetEntities entities;

    public Tweet(User user, TweetEntities entities) {
        this.user = user;
        this.entities = entities;
    }

    public TweetEntities getEntities() {
        return entities;
    }

    public User getUser() {
        return user;
    }
}

