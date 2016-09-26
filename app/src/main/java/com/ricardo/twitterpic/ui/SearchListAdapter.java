package com.ricardo.twitterpic.ui;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ricardo.twitterpic.BR;
import com.ricardo.twitterpic.R;
import com.ricardo.twitterpic.core.models.Media;
import com.ricardo.twitterpic.core.models.Tweet;
import com.ricardo.twitterpic.ui.mvp.UIView;
import com.squareup.picasso.Picasso;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.BindingViewHolder> {

    private final ImageSearchBinder mListBinder;
    private ShareImageNavigatorHandler shareImageNavigatorHandler;

    public SearchListAdapter(ImageSearchBinder listBinder, ShareImageNavigatorHandler shareImageNavigatorHandler) {
        mListBinder = listBinder;
        this.shareImageNavigatorHandler = shareImageNavigatorHandler;
    }

    @Override
    public SearchListAdapter.BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.listitem_media, parent, false);
        return new BindingViewHolder(viewDataBinding, shareImageNavigatorHandler);
    }

    @Override
    public void onBindViewHolder(SearchListAdapter.BindingViewHolder holder, int position) {
        mListBinder.bindListItem(holder, position);
    }

    @Override
    public int getItemCount() {
        return mListBinder.getListCount();
    }

    @BindingAdapter({"android:src"})
    public static void loadImage(ImageView view, String url) {
        if (url == null) {
            view.setImageResource(0);
        } else {
            Picasso.with(view.getContext())
                    .load(url)
                    .placeholder(R.drawable.progress_animation)
                    .into(view);
        }
    }

    public static class BindingViewHolder extends RecyclerView.ViewHolder implements UIView<Tweet> {

        private final ShareImageNavigatorHandler shareImageNavigatorHandler;

        public BindingViewHolder(ViewDataBinding viewDataBinding, ShareImageNavigatorHandler shareImageNavigatorHandler) {
            super(viewDataBinding.getRoot());
            this.shareImageNavigatorHandler = shareImageNavigatorHandler;
        }

        public ViewDataBinding getBinding() {
            return DataBindingUtil.getBinding(itemView);
        }

        @Override
        public void setModel(Tweet model) {
            final ViewDataBinding binding = getBinding();
            final Media media = model.getEntities().getMedia().get(0);
            binding.setVariable(BR.media, media);
            binding.setVariable(BR.tweet, model);
            binding.setVariable(BR.navigator, shareImageNavigatorHandler);
            binding.executePendingBindings();
        }

    }

}
