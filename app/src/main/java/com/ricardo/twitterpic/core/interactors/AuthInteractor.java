package com.ricardo.twitterpic.core.interactors;

import com.ricardo.twitterpic.core.models.Token;

public interface AuthInteractor extends Interactor {
    void authToken(Listener<Token> listener);
}
