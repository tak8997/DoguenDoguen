package org.tacademy.woof.doguendoguen.app.base.profile;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rey.material.widget.Switch;

import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.model.UserModel;
import org.tacademy.woof.doguendoguen.rest.RestService;
import org.tacademy.woof.doguendoguen.rest.user.UserService;
import org.tacademy.woof.doguendoguen.util.SharedPreferencesUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileFragment extends Fragment {
    private static final String TAG ="UserProfileFragment";
    private static final String USER_ID = "userId";

    public UserProfileFragment() {
    }

    public static UserProfileFragment newInstance() {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }
    String userId;
    UserModel user;

    @BindView(R.id.profile_title) TextView userName;
    @BindView(R.id.profile_sub_title) TextView userProfileChange;
    @BindView(R.id.profile_user_image) ImageView userImage;
    @BindView(R.id.post_regist) TextView postRegist;
    @BindView(R.id.user_post_list) TextView userPostList;
    @BindView(R.id.chatting_alarm) TextView chattingAlarm;
    @BindView(R.id.chatting_alarm_switch) Switch alarmSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ButterKnife.bind(this, view);

        //userId를 기준으로유저 프로필을 가져온다
        userId = SharedPreferencesUtil.getInstance().getUserId();
        Log.d(TAG, "userId: " + userId);

        userImage.setEnabled(false);
        postRegist.setEnabled(false);
        userPostList.setEnabled(false);
        chattingAlarm.setEnabled(false);
        alarmSwitch.setEnabled(false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //일단은 21으로, userId값 넣어줘야함
//        userId = "21";
        if(userId != null) {
            getUserService();
        }
    }

    private void getUserService() {
        UserService userService = RestService.createService(UserService.class);
        Call<UserModel> getUser = userService.getUser(Integer.parseInt(userId));
        getUser.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if(response.isSuccessful()) {
                    user = response.body();

                    if(user != null) {
                        Log.d(TAG, "userId: " + user.userId);

                        String name = user.userName;
                        String userImageThumbUrl = user.userImageThumbUrl;

                        userName.setText(name);
                        userProfileChange.setText("프로필 확인 및 수정");
                        Glide.with(DoguenDoguenApplication.getContext())
                                .load(userImageThumbUrl)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.user_login_off)
                                .into(userImage);

                        postRegist.setTextColor(Color.parseColor("#3E3A39"));
                        userPostList.setTextColor(Color.parseColor("#3E3A39"));
                        chattingAlarm.setTextColor(Color.parseColor("#3E3A39"));

                        userImage.setEnabled(true);
                        postRegist.setEnabled(true);
                        userPostList.setEnabled(true);
                        chattingAlarm.setEnabled(true);
                        alarmSwitch.setEnabled(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });

    }

    @OnClick({R.id.profile_user_image, R.id.post_regist, R.id.user_post_list, R.id.logout})
    public void onMyPageClicked(View view) {
        switch (view.getId()) {
            //유저 프로필 확인
            case R.id.profile_user_image:
                Intent intent = new Intent(DoguenDoguenApplication.getContext(), UserProfileDetailActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                break;
            //분양글 등록하기
            case R.id.post_regist:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, PostRegist_25_Fragment.newInstance(null));
                fragmentTransaction.commit();
                break;
            //내가 쓴 글 목록
            case R.id.user_post_list:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.container, UserPostListFragment.newInstance(user));
                ft.commit();
                break;
            case R.id.logout:
                if (userId != null) {
                    SharedPreferencesUtil.getInstance().clear();
                    Toast.makeText(DoguenDoguenApplication.getContext(), "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "userId:" + SharedPreferencesUtil.getInstance().getUserId());

                    userName.setText(R.string.profile_title);
                    userProfileChange.setText(R.string.profile_sub_title);
                    userImage.setImageResource(R.drawable.user_login_off);

                    postRegist.setTextColor(Color.parseColor("#C8C9CA"));
                    userPostList.setTextColor(Color.parseColor("#C8C9CA"));
                    chattingAlarm.setTextColor(Color.parseColor("#C8C9CA"));
                }
                break;
        }
    }



}
