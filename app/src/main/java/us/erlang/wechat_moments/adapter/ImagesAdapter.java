package us.erlang.wechat_moments.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Objects;

import us.erlang.wechat_moments.R;

public class ImagesAdapter extends BaseAdapter {
    String[] images;
    public ImagesAdapter(String[] images) {
        this.images = images;
    }

    @Override
    public int getCount() {
        if (Objects.nonNull(images)) {
            return images.length;
        } else {
            return 0;
        }
    }

    @Override
    public String getItem(int i) {
        return images[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View imageItemView;
        if (view == null) {
            imageItemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_item, null);
            if (Objects.nonNull(images[position])) {
                ImageView imageView = imageItemView.findViewById(R.id.image);
                Glide.with(imageView).load(images[position]).into(imageView);
            }
        } else {
            imageItemView = (View) view;
        }

        return imageItemView;
    }
}
