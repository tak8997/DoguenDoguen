package org.tacademy.woof.doguendoguen.rest.user;

import com.google.gson.JsonObject;

import org.tacademy.woof.doguendoguen.model.PostList;
import org.tacademy.woof.doguendoguen.model.UserIdModel;
import org.tacademy.woof.doguendoguen.model.UserModel;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Tak on 2017. 6. 14..
 */

public interface UserService {
    //프로필 저장
    //반려동물 유무: 0->없음, 1->있음
    @Multipart
    @POST("/profiles")
    Call<UserIdModel> registerUser(@Part MultipartBody.Part userImage, @Part("username") RequestBody userName,
                                   @Part("gender") RequestBody userGender, @Part("lifestyle") RequestBody userLifestyle,
                                   @Part("region") RequestBody userRegion, @Part("other_pets") RequestBody userPetOwn,
                                   @Part("family_size") RequestBody userFamilyType);

    //프로필 조회
    @GET("/profiles/{user_id}")
    Observable<UserModel> getUser(@Path("user_id") int userId);

    //프로필 업데이트
    @Multipart
    @PUT("profiles/{user_id}")
    Call<ResponseBody> updateUser(@Path("user_id") int userId, @Part MultipartBody.Part userImage, @Part("username") RequestBody userName, @Part("gender") RequestBody userGender,
                                   @Part("lifestyle") RequestBody userLifestyle, @Part("region") RequestBody userRegion,
                                   @Part("other_pets") RequestBody userPetOwn, @Part("family_size") RequestBody userFamilySize);


    //사용자 관심글 목록 가져오기
    @GET("/favorites")
    Observable<List<PostList>> getWishList();

    //사용자 관심글 저장/해제
    @PUT("/favorites/{parcel_id}")
    Observable<JsonObject> registerWishList(@Path("parcel_id") int postId);

    //회원탈퇴 하기
    @POST("/signout/{user_id}")
    Call<ResponseBody> userDrop(@Path("user_id") int userId);
}
