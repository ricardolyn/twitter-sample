package com.ricardo.twitterpic.ui.auth;

import com.ricardo.twitterpic.core.Store;
import com.ricardo.twitterpic.core.interactors.AuthInteractor;
import com.ricardo.twitterpic.core.interactors.Interactor;
import com.ricardo.twitterpic.core.models.Token;
import com.ricardo.twitterpic.core.network.Fetcher;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by Ricardo Silva (ricardo@monadtek.com)
 * Monad Tek Ltd. 2016
 */
public class AuthPresenterTest {

    public static final String KEY = "key";
    @Mock
    Store store;
    @Mock
    AuthInteractor interactor;
    @Mock
    AuthReadyUi view;
    @Mock
    Fetcher fetcher;

    AuthPresenter target;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        target = new AuthPresenter(store, interactor, fetcher);
    }

    @Test
    public void testNewAuth() throws Exception {
        //given
        target.init(view);
        when(store.has(anyString())).thenReturn(false);

        //when
        target.load();

        //then
        verify(interactor).authToken(any(Interactor.Listener.class));
    }

    @Test
    public void testOldAuth() throws Exception {
        //given
        target.init(view);
        when(store.has(anyString())).thenReturn(true);
        when(store.getString(anyString())).thenReturn(KEY);

        //when
        target.load();

        //then
        verifyZeroInteractions(interactor);
        fetcher.switchToBearerAuth(KEY);
        verify(view).onAuthReady();
    }

    @Test
    public void testReceivedAuth() throws Exception {
        //given
        target.init(view);
        Token token = new Token(KEY);

        //when
        target.onSuccess(token);

        //then
        fetcher.switchToBearerAuth(KEY);
        store.put(anyString(), eq(KEY));
        verify(view).onAuthReady();
    }
}