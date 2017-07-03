package org.tacademy.woof.doguendoguen.app.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import org.tacademy.woof.doguendoguen.AnimatedGifImageView;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.app.home.HomeActivity;
import org.tacademy.woof.doguendoguen.rest.RestClient;
import org.tacademy.woof.doguendoguen.rest.token.FcmService;
import org.tacademy.woof.doguendoguen.util.SharedPreferencesUtil;

import java.util.UUID;


/**
 * Created by Tak on 2017. 6. 13..
 */

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_TIME_OUT = 1500;

    //카카오 로그인 했을 경우, 그 즉시 토큰값을 받아와서 서버에 내려줌. 그러면 user_id를 내려준다. user_id를 파일에 저장하고 있음.
    private String userId;
    private String fcmToken;
    private String uuidValue;

    private AnimatedGifImageView animatedGifImageView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();

        animatedGifImageView = ((AnimatedGifImageView) findViewById(R.id.animatedGifImageView));
        animatedGifImageView.setAnimatedGif(R.raw.splash_gif_image,
                AnimatedGifImageView.TYPE.STREACH_TO_FIT);

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

        FcmService fcmService = RestClient.createService(FcmService.class);
//        Call<ResponseBody> getUserInfo = fcmService.putUserInfo(fcmToken, uuidValue);
//        getUserInfo.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if(response.isSuccessful()) {
//                    //TODO: 정상적으로 받아왔으면 파일로 저장
//                    SharedPreferencesUtil.getInstance().setFcmTokenKey(fcmToken);
//                    SharedPreferencesUtil.getInstance().setUuidKey(uuidValue);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });

    }

    private void init() {
        //FCM 토근을 얻어온다.(서버로 보내 등록한다)딱 한번만실행시키도록
        fcmToken = FirebaseInstanceId.getInstance().getToken();
        userId = SharedPreferencesUtil.getInstance().getUserId();
        uuidValue = UUID.randomUUID().toString().replace('-', 'A');
        Log.d("userId", "userId : " + userId);
    }

    private void startHomeActivity() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
        finish();
    }
}
