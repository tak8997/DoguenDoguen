package org.tacademy.woof.doguendoguen.app.base.profile;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import org.tacademy.woof.doguendoguen.model.MyPostModel;
import org.tacademy.woof.doguendoguen.model.UserModel;
import org.tacademy.woof.doguendoguen.rest.RestClient;
import org.tacademy.woof.doguendoguen.rest.user.UserService;
import org.tacademy.woof.doguendoguen.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

public class UserProfileFragment extends Fragment {
    private static final String TAG ="UserProfileFragment";
    private static final String USER_ID = "userId";

    private static final int USER_PROFILE_LOGOUT = 200;

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
    @BindView(R.id.logout) TextView logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ButterKnife.bind(this, view);

        //userId를 기준으로유저 프로필을 가져온다
        List<MyPostModel> list = new ArrayList<>();
        list.add(new MyPostModel(1, "분양", ""));
        user = new UserModel("", 22, "탁", "남", "2인실",
                "경기", 0, "분당","4인", "", list);
        userId = SharedPreferencesUtil.getInstance().getUserId();
        Log.d(TAG, "userId: " + userId);

        getActivity().findViewById(R.id.appbar).setVisibility(View.VISIBLE);

        userImage.setEnabled(false);
        postRegist.setEnabled(true);
        userPostList.setEnabled(true);
        chattingAlarm.setEnabled(false);
        alarmSwitch.setEnabled(false);
        logout.setEnabled(true);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(userId != null)
            getUserService();
    }

    @BindView(R.id.post_img) ImageView registPost;
    @BindView(R.id.my_post_list) ImageView myPostList;
    private void getUserService() {
        UserService userService = RestClient.createService(UserService.class);
        Observable<UserModel> getUser = userService.getUser(0); //내 아이디 조회는 0

        getUser.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userModel -> {
                    user = userModel;
                    Log.d(TAG, "onSuccess, userId: " + user.userId);

                    String name = user.userName;
                    String userImageThumbUrl = user.userImageThumbUrl;

                    userName.setText(name);
                    userProfileChange.setText("프로필 확인 및 수정");

                    Glide.with(DoguenDoguenApplication.getContext())
                            .load(userImageThumbUrl)
                            .dontTransform()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(userImage);

                    registPost.setImageResource(R.drawable.regist_on);
                    myPostList.setImageResource(R.drawable.my_post_off);

                    postRegist.setTextColor(Color.parseColor("#3E3A39"));
                    userPostList.setTextColor(Color.parseColor("#3E3A39"));
                    chattingAlarm.setTextColor(Color.parseColor("#3E3A39"));

                    userImage.setEnabled(true);
                    postRegist.setEnabled(true);
                    userPostList.setEnabled(true);
                    chattingAlarm.setEnabled(true);
                    alarmSwitch.setEnabled(true);
                }, Throwable::printStackTrace);
    }

    @OnClick({R.id.profile_user_image, R.id.post_regist, R.id.user_post_list, R.id.question, R.id.agreement, R.id.logout})
    public void onMyPageClicked(View view) {
        switch (view.getId()) {
            //유저 프로필 확인
            case R.id.profile_user_image:
            case R.id.profile_title:
            case R.id.profile_sub_title:
                Intent intent = new Intent(DoguenDoguenApplication.getContext(), UserProfileDetailActivity.class);
                intent.putExtra("user", user);
                startActivityForResult(intent, USER_PROFILE_LOGOUT);
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
            //자주 묻는 질문
            case R.id.question:
                FragmentManager fragManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragTransaction = fragManager.beginTransaction();
                fragTransaction.addToBackStack(null);
                fragTransaction.replace(R.id.container, QuestionFragment.newInstance());
                fragTransaction.commit();
                break;
            //서비스 이용약관
            case R.id.agreement:
                FragmentManager fManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fTransaction = fManager.beginTransaction();
                fTransaction.addToBackStack(null);
                fTransaction.replace(R.id.container, AgreementFragment.newInstance());
                fTransaction.commit();
                break;
            //로그아웃
            case R.id.logout:
                if (userId != null)
                    logout();
                break;
        }
    }

    private void logout() {
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == USER_PROFILE_LOGOUT)
            logout();
    }


}
