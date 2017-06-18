package org.tacademy.woof.doguendoguen.app.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.app.home.HomeActivity;
import org.tacademy.woof.doguendoguen.util.SharedPreferencesUtil;

/**
 * Created by Tak on 2017. 6. 13..
 */

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_TIME_OUT = 1500;

    //카카오 로그인 했을 경우, 그 즉시 토큰값을 받아와서 서버에 내려줌. 그러면 user_id를 내려준다. user_id를 파일에 저장하고 있음.
    private String userId;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(userId == null) {
                    // SharedPreference에 저장된 토큰값이 없으면 일단 그냥 로그인
                    startHomeActivity();
                } else {
                    // SharedPreference에 저장된 user_id값이 있으면 서버로 user_id를 보내서 해당 사용자에 대한 정보를 받아온다?
                    startHomeActivity();
                }

            }
        }, SPLASH_TIME_OUT);
    }

    private void init() {
        userId = SharedPreferencesUtil.getInstance().getUserId();
    }

    private void startHomeActivity() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
        finish();
    }
}
