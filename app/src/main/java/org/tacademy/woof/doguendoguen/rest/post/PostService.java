package org.tacademy.woof.doguendoguen.rest.post;

import org.tacademy.woof.doguendoguen.model.PostListModel;
import org.tacademy.woof.doguendoguen.model.PostModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Tak on 2017. 5. 29..
 */

public interface PostService {
    //게시글 등록
    @Multipart
    @POST("/doglists")
    Call<PostModel> registerPost(@Part MultipartBody.Part imageFile, @Part("body") RequestBody body);

    //조건에 따라 (전체)글 가져오기
    @GET("/doglists")
    Call<List<PostListModel>> getPosts(@Query("page") int page, @Query("spiece") String spiece,
                                      @Query("gender") String gender, @Query("region1") String region1, @Query("region2") String region2,
                                       @Query("age") String age);

    @GET("/doglists")
    Call<List<PostListModel>> getPosts(@Query("page") int page);
}
