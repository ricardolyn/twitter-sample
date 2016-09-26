/*
 * Copyright (C) Ricardo Silva - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ricardo Silva <ricardo@lyncode.com>, 2015
 */

package com.ricardo.twitterpic.core.network;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ricardo.twitterpic.BuildConfig;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Fetcher {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static Fetcher sFetcher;

    private final Retrofit mRetrofit;

    private boolean useBearer;
    private String auth;

    private Fetcher() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new HeadersInterceptor())
                .addInterceptor(logging)
                .build();

        final Gson gson = new GsonBuilder()
                .create();

        this.auth = createBasicAuthToken();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(Url.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build();
    }

    public static Fetcher get() {
        if (sFetcher == null) {
            sFetcher = new Fetcher();
        }
        return sFetcher;
    }

    public <T> T api(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }

    public void switchToBearerAuth(String bearer) {
        this.useBearer = true;
        this.auth = bearer.trim();
    }

    private static String createBasicAuthToken() {
        final String decoded = BuildConfig.CONSUMER_KEY + ":" + BuildConfig.CONSUMER_SECRET;
        try {
            byte[] data = decoded.getBytes("UTF-8");
            return Base64.encodeToString(data, Base64.NO_WRAP).trim();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void attachAuth(Request.Builder builder) {
        if (!useBearer) {
            builder.addHeader(AUTHORIZATION_HEADER, "Basic " + auth);
        } else {
            builder.addHeader(AUTHORIZATION_HEADER, "Bearer " + auth);
        }
    }

    public class HeadersInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request.Builder requestBuilder = chain.request().newBuilder();
            attachAuth(requestBuilder);
            return chain.proceed(requestBuilder.build());
        }

    }
}
