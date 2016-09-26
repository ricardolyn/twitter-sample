package com.ricardo.twitterpic.core.network;

import com.ricardo.twitterpic.core.models.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TweetsAPI {

    @GET(Url.TWEETS_SEARCH)
    Call<SearchResponse> search(@Query("q") String search, @Query("count") int count);
}
