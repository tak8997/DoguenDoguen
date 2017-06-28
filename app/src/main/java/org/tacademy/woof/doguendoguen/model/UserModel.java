package org.tacademy.woof.doguendoguen.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Tak on 2017. 6. 14..
 */

public class UserModel implements Parcelable {
    //10개?
    @SerializedName("user_id")
    public int userId;

    @SerializedName("username")
    public String userName;

    @SerializedName("gender")
    public String userGender;

    @SerializedName("lifestyle")
    public String userApartType;

    @SerializedName("region")
    public String userRegion;

    @SerializedName("other_pets")
    public int userPetOwn;

    @SerializedName("family_size")
    public String userFamilySize;

    @SerializedName("profile_image")
    public String userImageUrl;

    @SerializedName("profile_thumbnail")
    public String userImageThumbUrl;

    @SerializedName("mylist")
    public List<MyPostModel> userPostList;

    public UserModel(int userId, String userName, String userGender, String userApartType, String userRegion, int userPetOwn, String userFamilySize, String userImageUrl, String userImageThumbUrl, List<MyPostModel> userPostList) {
        this.userId = userId;
        this.userName = userName;
        this.userGender = userGender;
        this.userApartType = userApartType;
        this.userRegion = userRegion;
        this.userPetOwn = userPetOwn;
        this.userFamilySize = userFamilySize;
        this.userImageUrl = userImageUrl;
        this.userImageThumbUrl = userImageThumbUrl;
        this.userPostList = userPostList;
    }

    protected UserModel(Parcel in) {
        userId = in.readInt();
        userName = in.readString();
        userGender = in.readString();
        userApartType = in.readString();
        userRegion = in.readString();
        userPetOwn = in.readInt();
        userFamilySize = in.readString();
        userImageUrl = in.readString();
        userImageThumbUrl = in.readString();
        userPostList = in.createTypedArrayList(MyPostModel.CREATOR);
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(userName);
        dest.writeString(userGender);
        dest.writeString(userApartType);
        dest.writeString(userRegion);
        dest.writeInt(userPetOwn);
        dest.writeString(userFamilySize);
        dest.writeString(userImageUrl);
        dest.writeString(userImageThumbUrl);
        dest.writeTypedList(userPostList);
    }
}
