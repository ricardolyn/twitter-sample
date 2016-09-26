package com.ricardo.twitterpic.core.interactors;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Ricardo Silva (ricardo@monadtek.com)
 * Monad Tek Ltd. 2016
 */

public interface Interactor {

    interface Listener<T> {

        void onSuccess(T t);

        void onFailure();

        void onError(ErrorData errorData);
    }

    class Callback<T> implements retrofit2.Callback<T> {

        private final Listener<T> listener;
        private final ErrorParser errorParser;

        public Callback(Listener<T> listener, ErrorParser errorParser) {
            this.listener = listener;
            this.errorParser = errorParser;
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            listener.onFailure();
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            boolean isError = !response.isSuccessful();

            if (isError) { // FIXME we shouldn't care about error parsing here
                ErrorData errorData = errorParser.parseErrorFrom(response);
                listener.onError(errorData);
            } else {
                listener.onSuccess(response.body());
            }
        }

    }
}
