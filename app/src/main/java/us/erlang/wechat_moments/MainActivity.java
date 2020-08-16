package us.erlang.wechat_moments;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Objects;

import us.erlang.wechat_moments.model.Feed;
import us.erlang.wechat_moments.model.User;
import us.erlang.wechat_moments.adapter.FeedAdapter;
import us.erlang.wechat_moments.repository.FeedRepository;
import us.erlang.wechat_moments.repository.UserRepository;
import us.erlang.wechat_moments.utils.HttpUtil;
import us.erlang.wechat_moments.view_model.FeedsViewModel;
import us.erlang.wechat_moments.view_model.UserViewModel;

public class MainActivity extends AppCompatActivity {
    private HttpUtil httpUtil = new HttpUtil();
    private UserViewModel userViewModel;
    private FeedsViewModel feedsViewModel;
    ImageView profileImageView, avatarView;
    TextView nickTextView;
    private FeedAdapter feedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileImageView = findViewById(R.id.profile_image);
        avatarView = findViewById(R.id.avatar);
        nickTextView = findViewById(R.id.nick);

        userViewModel = getUserViewModel();
        userViewModel.observeUser(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (Objects.nonNull(user.getProfileImage())) {
                    Glide.with(profileImageView).load(user.getProfileImage()).into(profileImageView);
                }
                if (Objects.nonNull(user.getAvatar())) {
                    Glide.with(avatarView).load(user.getAvatar()).circleCrop().into(avatarView);
                }
                nickTextView.setText(user.getNick());
            }
        });
        userViewModel.loadUserInfo();

        RecyclerView recyclerView = findViewById(R.id.feeds_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        feedAdapter = new FeedAdapter();
        recyclerView.setAdapter(feedAdapter);
        loadMoreFeedsWhenPullingUp(recyclerView, layoutManager);

        feedsViewModel = getFeedsViewModel();
        feedsViewModel.observeFeeds(this, new Observer<List<Feed>>() {
            @Override
            public void onChanged(List<Feed> feeds) {
                feedAdapter.initFeeds(feeds);
            }
        });
        feedsViewModel.loadFeeds();

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.feeds_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                feedAdapter.reloadFeeds();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void loadMoreFeedsWhenPullingUp(RecyclerView recyclerView, LinearLayoutManager layoutManager) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if ((layoutManager.getChildCount() + layoutManager.findFirstVisibleItemPosition())
                            >= layoutManager.getItemCount()) {
                        recyclerView.post(new Runnable() {
                            public void run() {
                                feedAdapter.displayMoreFeeds();
                            }
                        });

                    }
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }


    private UserViewModel getUserViewModel() {
        UserRepository userRepository = new UserRepository(httpUtil);
        UserViewModel userViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(UserViewModel.class);
        userViewModel.setUserRepository(userRepository);
        return userViewModel;
    }

    private FeedsViewModel getFeedsViewModel() {
        FeedRepository feedRepository = new FeedRepository(httpUtil);
        FeedsViewModel feedsViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(FeedsViewModel.class);
        feedsViewModel.setFeedRepository(feedRepository);
        return feedsViewModel;
    }
}