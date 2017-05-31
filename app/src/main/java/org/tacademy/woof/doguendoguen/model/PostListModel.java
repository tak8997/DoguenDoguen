package org.tacademy.woof.doguendoguen.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tak on 2017. 5. 29..
 */

public class PostListModel {
    @SerializedName("parcel_Id")
    public int postId;

    @SerializedName("title")
    public String title;

    @SerializedName("pet_thumbnail")
    public String petImageUrl;

    @SerializedName("username")
    public String username;

    @SerializedName("favorite")
    public int favorite;

    public PostListModel(int postId, String title, String petImageUrl, String username, int favorite) {
        this.postId = postId;
        this.title = title;
        this.petImageUrl = petImageUrl;
        this.username = username;
        this.favorite = favorite;
    }
}
