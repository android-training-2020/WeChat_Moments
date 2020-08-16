package us.erlang.wechat_moments.utils;

import java.io.IOException;

import io.reactivex.ObservableEmitter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {
    public void request(String url, ObservableEmitter emitter) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        Response response;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                emitter.onNext(response.body().string());
                emitter.onComplete();
            } else if (!response.isSuccessful()) {
                emitter.onError(new Exception("Failed to request " + url));
            }
        } catch (IOException e) {
            e.printStackTrace();
            emitter.onError(e);
        }
    }
}
