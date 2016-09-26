package com.ricardo.twitterpic.core;

public interface Store {

    void put(String key, String value);

    String getString(String key);

    boolean has(String key);

    void delete(String key);
}
