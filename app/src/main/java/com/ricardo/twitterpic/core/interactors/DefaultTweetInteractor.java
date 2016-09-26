package com.ricardo.twitterpic.core.interactors;

import android.support.annotation.NonNull;

import com.ricardo.twitterpic.core.models.Media;
import com.ricardo.twitterpic.core.models.SearchResponse;
import com.ricardo.twitterpic.core.models.Tweet;
import com.ricardo.twitterpic.core.network.Fetcher;
import com.ricardo.twitterpic.core.network.TweetsAPI;

import java.util.Iterator;
import java.util.List;

import retrofit2.Call;

public class DefaultTweetInteractor implements TweetInteractor {

    private static final int MAX_ITEMS = 50;

    private final ErrorParser errorParser;
    private final TweetsAPI tweetsAPI;

    public static TweetInteractor create() {
        return new DefaultTweetInteractor(new ErrorParser(), Fetcher.get().api(TweetsAPI.class));
    }

    public DefaultTweetInteractor(ErrorParser errorParser, TweetsAPI tweetsAPI) {
        this.errorParser = errorParser;
        this.tweetsAPI = tweetsAPI;
    }

    @Override
    public void searchImageTweets(String search, Listener<List<Tweet>> listener) {
        Call<SearchResponse> call = tweetsAPI.search(searchWithFilter(search), MAX_ITEMS);
        call.enqueue(new Callback<>(new SearchResponseListenerDecorator(listener), errorParser));
    }

    @NonNull
    private static String searchWithFilter(String search) {
        return search + " filter:media";
    }

    private final class SearchResponseListenerDecorator implements Listener<SearchResponse> {

        private final Listener<List<Tweet>> listener;

        private SearchResponseListenerDecorator(Listener<List<Tweet>> listener) {
            this.listener = listener;
        }

        @Override
        public void onSuccess(SearchResponse searchResponse) {
            final List<Tweet> statuses = searchResponse.getStatuses();
            // N is small, if it increases exponentially should be moved to background thread
            Iterator<Tweet> iterator = statuses.iterator();
            while (iterator.hasNext()) {
                final Tweet tweet = iterator.next();
                final List<Media> media = tweet.getEntities().getMedia();
                if (media == null || media.isEmpty()) {
                    iterator.remove();
                }
            }
            listener.onSuccess(statuses);
        }

        @Override
        public void onFailure() {
            listener.onFailure();
        }

        @Override
        public void onError(ErrorData errorData) {
            listener.onError(errorData);
        }
    }
}
