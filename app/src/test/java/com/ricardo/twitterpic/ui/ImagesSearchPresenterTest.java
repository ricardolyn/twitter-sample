package com.ricardo.twitterpic.ui;

import com.ricardo.twitterpic.core.interactors.Interactor;
import com.ricardo.twitterpic.core.interactors.TweetInteractor;
import com.ricardo.twitterpic.core.models.Media;
import com.ricardo.twitterpic.core.models.MediaSize;
import com.ricardo.twitterpic.core.models.MediaSizes;
import com.ricardo.twitterpic.core.models.Tweet;
import com.ricardo.twitterpic.core.models.TweetEntities;
import com.ricardo.twitterpic.core.models.User;
import com.ricardo.twitterpic.core.respository.Repository;
import com.ricardo.twitterpic.ui.mvp.UIView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Ricardo Silva (ricardo@monadtek.com)
 * Monad Tek Ltd. 2016
 */
public class ImagesSearchPresenterTest {

    public static final String TEXT = "text";
    public static final String URL = "URL";
    public static final int THUMB_WIDTH = 100;
    public static final int THUMB_WIDTH_2 = 110;
    public static final int THUMB_HEIGHT = 120;
    @Mock
    TweetInteractor interactor;
    @Mock
    Repository<List<Tweet>> repository;
    @Mock
    ImagesSearchUi view;
    @Mock
    UIView<Tweet> item;

    List<Tweet> tweetList = new ArrayList<>();

    ImagesSearchPresenter target;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        tweetList.add(createTweet(THUMB_WIDTH, THUMB_HEIGHT));
        tweetList.add(createTweet(THUMB_WIDTH_2, THUMB_HEIGHT));

        when(repository.retrieve()).thenReturn(tweetList);

        target = new ImagesSearchPresenter(interactor, repository);
    }

    private Tweet createTweet(int thumbWidth, int thumbHeight) {
        MediaSize mediaSize = new MediaSize(thumbWidth, thumbHeight);
        MediaSizes mediaSizes = new MediaSizes(mediaSize);
        Media media = new Media(URL, mediaSizes);
        List<Media> listMedia = new ArrayList<>();
        listMedia.add(media);
        TweetEntities tweetEntities = new TweetEntities(listMedia);
        User user = new User("name", "url_profile");
        return new Tweet(user, tweetEntities);
    }

    @Test
    public void testSearch() throws Exception {
        //given
        target.init(view);

        //when
        target.search(TEXT);

        //then
        verify(repository).clear();
        verify(interactor).searchImageTweets(eq(TEXT), any(Interactor.Listener.class));
    }

    @Test
    public void testRestore() throws Exception {
        //given
        target.init(view);
        when(repository.isEmpty()).thenReturn(false);

        //when
        target.restore(true);

        //then
        verify(repository, times(2)).retrieve();
        verify(view).onTweetImagesReady(anyInt(), anyInt());
    }

    @Test
    public void testLoadOnView() throws Exception {
        //given
        target.init(view);

        //when
        target.onSuccess(tweetList);

        //then
        verify(repository).persist(tweetList);
        assertEquals(tweetList.size(), target.getListCount());
        verify(view).onTweetImagesReady(tweetList.size(), THUMB_WIDTH_2);
    }

    @Test
    public void testBindItem() throws Exception {
        //given
        target.init(view);

        //when
        target.bindListItem(item, 0);

        //then
        verify(item).setModel(tweetList.get(0));
    }
}