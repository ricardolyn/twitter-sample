package com.ricardo.twitterpic.ui;

import com.ricardo.twitterpic.core.interactors.DefaultTweetInteractor;
import com.ricardo.twitterpic.core.interactors.ErrorData;
import com.ricardo.twitterpic.core.interactors.Interactor;
import com.ricardo.twitterpic.core.interactors.TweetInteractor;
import com.ricardo.twitterpic.core.models.Media;
import com.ricardo.twitterpic.core.models.Tweet;
import com.ricardo.twitterpic.core.respository.Repository;
import com.ricardo.twitterpic.core.respository.RepositoryInjector;
import com.ricardo.twitterpic.ui.mvp.Presenter;
import com.ricardo.twitterpic.ui.mvp.UIView;

import java.util.List;

public class ImagesSearchPresenter implements Presenter<ImagesSearchUi>, ImageSearchBinder, Interactor.Listener<List<Tweet>> {

    private final TweetInteractor tweetInteractor;
    private final Repository<List<Tweet>> repository;
    private ImagesSearchUi view;

    public static ImagesSearchPresenter create() {
        return new ImagesSearchPresenter(DefaultTweetInteractor.create(), RepositoryInjector.INSTANCE.tweets());
    }

    ImagesSearchPresenter(TweetInteractor tweetInteractor, Repository<List<Tweet>> repository) {
        this.tweetInteractor = tweetInteractor;
        this.repository = repository;
    }

    @Override
    public void init(ImagesSearchUi view) {
        this.view = view;
    }

    public void search(String search) {
        view.setLoading(true);
        repository.clear();
        tweetInteractor.searchImageTweets(search, this);
    }

    public void restore(boolean isRestored) {
        if (!repository.isEmpty() && isRestored) {
            setTweetsOnView();
        } else {
            repository.clear();
        }
    }

    @Override
    public void bindListItem(UIView<Tweet> item, int position) {
        item.setModel(repository.retrieve().get(position));
    }

    @Override
    public int getListCount() {
        if (repository.isEmpty()) {
            return 0;
        }

        return repository.retrieve().size();
    }

    @Override
    public void onSuccess(List<Tweet> tweets) {
        repository.persist(tweets);
        view.setLoading(false);
        setTweetsOnView();
    }

    private void setTweetsOnView() {
        view.onTweetImagesReady(repository.retrieve().size(), getItemMaxWidth());
    }

    public int getItemMaxWidth() {
        if (repository.isEmpty()) {
            return -1;
        }

        int max = 0;
        for (Tweet tweet : repository.retrieve()) {
            final List<Media> media = tweet.getEntities().getMedia();
            if (media != null && !media.isEmpty()) {
                max = Math.max(max, media.get(0).getSizes().getThumb().getW());
            }
        }
        return max;
    }

    @Override
    public void onFailure() {
        view.setLoading(false);
        view.onFailedImageSearch();
    }

    @Override
    public void onError(ErrorData errorData) {
        view.setLoading(false);
        view.onFailedImageSearch();
    }

}
