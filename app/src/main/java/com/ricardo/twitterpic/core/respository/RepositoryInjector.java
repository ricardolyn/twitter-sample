package com.ricardo.twitterpic.core.respository;

import com.ricardo.twitterpic.core.models.Tweet;

import java.util.List;

public enum RepositoryInjector {

    INSTANCE;

    private final Repository<List<Tweet>> tweetsRepo;

    RepositoryInjector() {
        tweetsRepo = new BaseRepository<>();
    }

    public Repository<List<Tweet>> tweets() {
        return tweetsRepo;
    }
}
