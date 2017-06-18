package org.tacademy.woof.doguendoguen.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tak on 2017. 6. 16..
 */

public class MyPostModel implements Parcelable {
    @SerializedName("parcel_id")
    public int postId;

    @SerializedName("title")
    public String postTitle;

    @SerializedName("pet_thumbnail")
    public String postImageUrl;

    public MyPostModel(int postId, String postTitle, String postImageUrl) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postImageUrl = postImageUrl;
    }

    protected MyPostModel(Parcel in) {
        postId = in.readInt();
        postTitle = in.readString();
        postImageUrl = in.readString();
    }

    public static final Creator<MyPostModel> CREATOR = new Creator<MyPostModel>() {
        @Override
        public MyPostModel createFromParcel(Parcel in) {
            return new MyPostModel(in);
        }

        @Override
        public MyPostModel[] newArray(int size) {
            return new MyPostModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(postId);
        dest.writeString(postTitle);
        dest.writeString(postImageUrl);
    }
}
