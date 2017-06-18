package org.tacademy.woof.doguendoguen.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Tak on 2017. 6. 17..
 */

public class PostListModel {
    @SerializedName("page")
    public int page;

    @SerializedName("post_count")
    public String postCount;

    @SerializedName("has_next")
    public boolean hasNext;

    @SerializedName("result")
    public List<PostList> postLists;

    public PostListModel(int page, String postCount, boolean hasNext, List<PostList> postLists) {
        this.page = page;
        this.postCount = postCount;
        this.hasNext = hasNext;
        this.postLists = postLists;
    }
}
