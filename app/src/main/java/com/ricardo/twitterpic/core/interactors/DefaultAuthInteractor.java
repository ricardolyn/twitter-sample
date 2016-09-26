package com.ricardo.twitterpic.core.interactors;

import com.ricardo.twitterpic.core.models.Token;
import com.ricardo.twitterpic.core.network.AuthAPI;
import com.ricardo.twitterpic.core.network.Fetcher;

import retrofit2.Call;

public class DefaultAuthInteractor implements AuthInteractor {

    private static final String GRANT_TYPE = "client_credentials";

    private final ErrorParser errorParser;
    private final AuthAPI authAPI;

    public static AuthInteractor create() {
        return new DefaultAuthInteractor(new ErrorParser(), Fetcher.get().api(AuthAPI.class));
    }

    public DefaultAuthInteractor(ErrorParser errorParser, AuthAPI authAPI) {
        this.errorParser = errorParser;
        this.authAPI = authAPI;
    }

    @Override
    public void authToken(Listener<Token> listener) {
        Call<Token> call = authAPI.authToken(GRANT_TYPE);
        call.enqueue(new Callback<>(listener, errorParser));
    }
}
