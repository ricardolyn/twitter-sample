package com.ricardo.twitterpic.core;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefStore implements Store {

    private final SharedPreferences sharedPreferences;

    public static SharedPrefStore create(Context context) {
        return new SharedPrefStore(context.getApplicationContext().getSharedPreferences(SharedPrefStore.class.getName(), Context.MODE_PRIVATE));
    }

    public SharedPrefStore(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void put(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    @Override
    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    @Override
    public boolean has(String key) {
        return sharedPreferences.contains(key);
    }

    @Override
    public void delete(String key) {
        sharedPreferences.edit().remove(key).apply();
    }
}
