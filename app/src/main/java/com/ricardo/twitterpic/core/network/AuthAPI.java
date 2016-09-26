package com.ricardo.twitterpic.core.network;

import com.ricardo.twitterpic.core.models.Token;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthAPI {

    @FormUrlEncoded
    @POST(Url.OAUTH_TOKEN)
    Call<Token> authToken(@Field("grant_type") String grantType);
}
