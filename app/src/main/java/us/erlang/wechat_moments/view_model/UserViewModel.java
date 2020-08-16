package us.erlang.wechat_moments.view_model;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import us.erlang.wechat_moments.model.User;
import us.erlang.wechat_moments.repository.UserRepository;

public class UserViewModel extends ViewModel {
    private MutableLiveData<User> user;
    private UserRepository userRepository;

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private LiveData<User> getUser() {
        if (Objects.isNull(user)) {
            user = new MutableLiveData<>();
        }
        return user;
    }

    public void observeUser(LifecycleOwner lifecycleOwner, androidx.lifecycle.Observer<User> observer) {
        this.getUser().observe(lifecycleOwner, observer);
    }

    public void loadUserInfo() {
        userRepository.loadUserInfo()
                .subscribeOn(Schedulers.io())
                .map(new Function<String, User>() {
                    @Override
                    public User apply(String str) throws Exception {
                        java.lang.reflect.Type type = new TypeToken<User>() {
                        }.getType();
                        return new Gson().fromJson(str, type);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<User>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(User userInfo) {
                        user.postValue(userInfo);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("UserViewModel", "Failed to load user info", e);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
