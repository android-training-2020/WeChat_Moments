package us.erlang.wechat_moments.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Objects;

import us.erlang.wechat_moments.R;
import us.erlang.wechat_moments.FeedViewHolder;
import us.erlang.wechat_moments.model.Comment;
import us.erlang.wechat_moments.model.Feed;
import us.erlang.wechat_moments.model.User;

public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {
    private List<Feed> feeds;
    private List<Feed> allFeeds;
    Context context;

    public void setFeeds(List<Feed> feeds) {
        this.feeds = feeds;
        this.notifyDataSetChanged();
    }

    public void setAllFeeds(List<Feed> feeds) {
        this.allFeeds = feeds;
    }

    public void initFeeds(List<Feed> feeds) {
        setAllFeeds(feeds);
        setFeeds(feeds.subList(0, Math.min(feeds.size(), 5)));
    }

    public void displayMoreFeeds() {
        int toIndex = Math.min(this.feeds.size() + 5, this.allFeeds.size());
        List<Feed> feeds = this.allFeeds.subList(0, toIndex);
        setFeeds(feeds);
    }

    public void reloadFeeds() {
        List<Feed> feeds = this.allFeeds.subList(0, Math.min(this.allFeeds.size(), 5));
        setFeeds(feeds);
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View feedItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item,
                parent, false);
        return new FeedViewHolder(feedItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        Feed currentFeed = feeds.get(position);
        String content = currentFeed.getContent();
        User sender = currentFeed.getSender();
        String[] images = currentFeed.getImages();
        Comment[] comments = currentFeed.getComments();

        if (Objects.nonNull(content)) {
            holder.contentView.setText(currentFeed.getContent());
        } else {
            holder.contentView.setVisibility(View.GONE);
        }

        if (Objects.nonNull(sender)) {
            holder.senderNickView.setText(sender.getNick());
            ImageView avatarView = holder.avatarView;
            Glide.with(avatarView).load(currentFeed.getSender().getAvatar()).into(avatarView);
        }

        if (Objects.nonNull(images)) {
            ImagesAdapter imagesAdapter = new ImagesAdapter(images);
            holder.imagesGridView.setAdapter(imagesAdapter);
        }

        if (Objects.nonNull(comments)) {
            holder.commentsView.setVisibility(View.VISIBLE);
            CommentAdapter commentAdapter = new CommentAdapter(comments);
            holder.commentsView.setAdapter(commentAdapter);
        } else {
            holder.commentsView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(Objects.nonNull(feeds)) {
            return feeds.size();
        }
        return 0;
    }
}
