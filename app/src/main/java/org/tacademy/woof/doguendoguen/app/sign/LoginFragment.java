package org.tacademy.woof.doguendoguen.app.sign;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.app.base.wish.WishFragment;
import org.tacademy.woof.doguendoguen.util.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginFragment extends DialogFragment {
    private static final String TAG = "LoginFragment";

    private static final int NEXT_SIGN_UP = 200;

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.LoginDialogTheme);
        if (getArguments() != null) {
        }
    }

    @BindView(R.id.kakao_login) Button kakaoLogin;
    @BindView(R.id.heart) ImageView heart;

    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        kakaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferencesUtil.getInstance().clear();

                //받아왔다고 가정
                userId = "21";
                SharedPreferencesUtil.getInstance().setUserId(userId);
                Log.d(TAG, "userId: " + userId);

                //userId를 받아왔으면 해당 사용자의 위시목록을 띄운다.
                if(userId != null)
                    exitLoginFragment();

//                Intent intent = new Intent(DoguenDoguenApplication.getContext(), SignUpActivity.class);
//                startActivityForResult(intent, NEXT_SIGN_UP);
            }
        });

        return view;
    }

    private void exitLoginFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, WishFragment.newInstance()); //사용자 위시리시트 뿌려줌
        ft.remove(this);    //현재 로그인 프래그먼트 종료
        ft.commit();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(resultCode == Activity.RESULT_OK && requestCode == NEXT_SIGN_UP) {
//            userId = data.getExtras().getString("userId");
//            SharedPreferencesUtil.getInstance().setUserId(userId);
//            Log.d(TAG, "userId: " + userId);
//
//            //userId를 받아왔으면 해당 사용자의 위시목록을 띄운다.
//            if(userId != null) {
//                exitLoginFragment();
//            }
//        }
//    }
}
