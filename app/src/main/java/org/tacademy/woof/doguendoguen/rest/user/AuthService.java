package org.tacademy.woof.doguendoguen.rest.user;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Tak on 2017. 7. 17..
 */

public interface AuthService {
    //로그인
    @POST("/login")
    @FormUrlEncoded
    Observable<JsonObject> login(@Field("email") String email, @Field("password") String password);

    //회원가입
    @POST("/signup")
    @FormUrlEncoded
    Observable<JsonObject> signUp(@Field("email") String email, @Field("password") String password, @Field("checking_password") String pwdCheck);
}
