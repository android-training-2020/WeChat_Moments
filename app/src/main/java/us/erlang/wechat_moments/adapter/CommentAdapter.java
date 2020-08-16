package us.erlang.wechat_moments.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Objects;

import us.erlang.wechat_moments.R;
import us.erlang.wechat_moments.model.Comment;

public class CommentAdapter extends BaseAdapter {
    Comment[] comments;
    public CommentAdapter(Comment[] comments) {
        this.comments = comments;
    }

    @Override
    public int getCount() {
        if (Objects.nonNull(comments)) {
            return comments.length;
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View commentView;
        if (view == null) {
            commentView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_item, null);
            if (Objects.nonNull(comments[position])) {
                TextView nickView = commentView.findViewById(R.id.nick);
                TextView contentView = commentView.findViewById(R.id.content);
                nickView.setText(comments[position].getSender().getNick());
                contentView.setText(" : " + comments[position].getContent());
            }
        } else {
            commentView = (View) view;
        }

        return commentView;
    }
}
