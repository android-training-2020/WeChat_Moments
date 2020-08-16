package us.erlang.wechat_moments.view_model;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import us.erlang.wechat_moments.model.Feed;
import us.erlang.wechat_moments.repository.FeedRepository;

public class FeedsViewModel extends ViewModel {

    private MutableLiveData<List<Feed>> feeds;
    private FeedRepository feedRepository;

    public void setFeedRepository(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    private LiveData<List<Feed>> getFeeds() {
        if (Objects.isNull(feeds)) {
            feeds = new MutableLiveData<List<Feed>>();
        }
        return feeds;
    }

    public void observeFeeds(LifecycleOwner lifecycleOwner, androidx.lifecycle.Observer<List<Feed>> observer) {
        this.getFeeds().observe(lifecycleOwner, observer);
    }

    public void loadFeeds() {
        feedRepository.loadFeeds()
                .subscribeOn(Schedulers.io())
                .map(new Function<String, List<Feed>>() {
                    public List<Feed> apply(String str) throws Exception {
                        Type collectionType = new TypeToken<List<Feed>>() {
                        }.getType();
                        return new Gson().fromJson(str, collectionType);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<List<Feed>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Feed> list) {
                        list = list.stream().filter((Feed feed) ->
                                (Objects.nonNull(feed.getContent()) || Objects.nonNull(feed.getImages()))
                        ).collect(Collectors.toList());
                        feeds.postValue(list);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("FeedsViewModel", "Failed to load feeds", e);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
