package com.ricardo.twitterpic.ui.auth;

import com.ricardo.twitterpic.core.Store;
import com.ricardo.twitterpic.core.interactors.AuthInteractor;
import com.ricardo.twitterpic.core.interactors.DefaultAuthInteractor;
import com.ricardo.twitterpic.core.interactors.ErrorData;
import com.ricardo.twitterpic.core.interactors.Interactor;
import com.ricardo.twitterpic.core.models.Token;
import com.ricardo.twitterpic.core.network.Fetcher;
import com.ricardo.twitterpic.ui.mvp.Presenter;

public class AuthPresenter implements Presenter<AuthReadyUi>, Interactor.Listener<Token> {

    private static final String BEARER_AUTH = "BEARER_AUTH";

    private final Store store;
    private final AuthInteractor authInteractor;
    private final Fetcher fetcher;

    private AuthReadyUi view;

    public static AuthPresenter create(Store store) {
        return new AuthPresenter(store, DefaultAuthInteractor.create(), Fetcher.get());
    }

    AuthPresenter(Store store, AuthInteractor authInteractor, Fetcher fetcher) {
        this.store = store;
        this.authInteractor = authInteractor;
        this.fetcher = fetcher;
    }

    @Override
    public void init(AuthReadyUi view) {
        this.view = view;

        load();
    }

    private void load() {
        if (store.has(BEARER_AUTH)) {
            fetcher.switchToBearerAuth(store.getString(BEARER_AUTH));
            view.onAuthReady();
        } else {
            view.setLoading(true);
            authInteractor.authToken(this);
        }
    }

    @Override
    public void onSuccess(Token token) {
        store.put(BEARER_AUTH, token.getAccessToken());
        fetcher.switchToBearerAuth(token.getAccessToken());
        view.setLoading(false);
        view.onAuthReady();
    }

    @Override
    public void onFailure() {
        view.setLoading(false);
        view.onAuthFailed();
    }

    @Override
    public void onError(ErrorData errorData) {
        view.setLoading(false);
        view.onAuthFailed();
    }
}
