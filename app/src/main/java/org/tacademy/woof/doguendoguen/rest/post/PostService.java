package org.tacademy.woof.doguendoguen.rest.post;

import com.google.gson.JsonObject;

import org.tacademy.woof.doguendoguen.model.PostDetailModel;
import org.tacademy.woof.doguendoguen.model.PostList;
import org.tacademy.woof.doguendoguen.model.PostListModel;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Tak on 2017. 5. 29..
 */

public interface PostService {
    //게시글 등록 18개
    @Multipart
    @POST("/doglists")
    Call<ResponseBody> registerPost(@Part MultipartBody.Part dogImage, @Part MultipartBody.Part parentDogImage, @Part MultipartBody.Part hierarchyImage,
                                    @Part("user_id") RequestBody id, @Part("title") RequestBody title, @Part("spiece") RequestBody type, @Part("gender") RequestBody gender,
                                    @Part("age") RequestBody age, @Part("region1") RequestBody city, @Part("region2") RequestBody district, @Part("price") RequestBody price,
                                    @Part("fur") RequestBody color, @Part("size") RequestBody size, @Part("DHPPL") RequestBody dhppl, @Part("corona") RequestBody corrona, @Part("kennel") RequestBody kennel,
                                    @Part("introduction") RequestBody introduction, @Part("condition") RequestBody condtion);

    //게시글 삭제
    @DELETE("/doglists/{parcel_id}")
    Observable<JsonObject> deletePost(@Path("parcel_id") int postId);

    //게시글 수정 21개
    @Multipart
    @PUT("/doglists/{parcel_id}")
    Call<ResponseBody> updatePost(@Part("pet_image_id") RequestBody imageId, @Part("parent_image_id") RequestBody parentId,
            @Path("parcel_id") int postId, @Part MultipartBody.Part dogImage, @Part MultipartBody.Part parentDogImage, @Part MultipartBody.Part hierarchyImage,
                            @Part("user_id") RequestBody id, @Part("title") RequestBody title, @Part("spiece") RequestBody type, @Part("gender") RequestBody gender,
                            @Part("age") RequestBody age, @Part("region1") RequestBody city, @Part("region2") RequestBody district, @Part("price") RequestBody price,
                            @Part("fur") RequestBody color, @Part("size") RequestBody size, @Part("DHPPL") RequestBody dhppl, @Part("corona") RequestBody corrona, @Part("kennel") RequestBody kennel,
                            @Part("introduction") RequestBody introduction, @Part("condition") RequestBody condtion
                            );

    //조건에 따라 (전체)글 가져오기
    @GET("/doglists")
    Observable<PostListModel> getPosts(@Query("page") int page, @Query("spiece") String dogType,
                                       @Query("gender") String gender, @Query("region1") String region1, @Query("region2") String region2,
                                       @Query("age") String age);

    //가장 시급한 분양 글 6개
    @GET("/doglists/emergency")
    Observable<List<PostList>> getUrgentPosts();

    //분양글 1개 상세보기
    @GET("/doglists/{parcel_id}")
    Observable<PostDetailModel> getPost(@Path("parcel_id") int parcel_id);
}
