package us.erlang.wechat_moments.repository;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import us.erlang.wechat_moments.utils.HttpUtil;

public class FeedRepository {
    private HttpUtil httpUtil;

    public FeedRepository(HttpUtil httpUtil) {
        this.httpUtil = httpUtil;
    }

    public Observable loadFeeds() {
        return Observable.create(new ObservableOnSubscribe<String>(){
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                httpUtil.request("https://twc-android-bootcamp.github.io/fake-data/data/weixin/tweets.json", emitter);
            }
        });
    }
}
