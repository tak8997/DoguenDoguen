package org.tacademy.woof.doguendoguen.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Tak on 2017. 6. 6..
 */

public class PostDetailModel implements Parcelable{

    @SerializedName("parcel_id")
    public int postId;

    @SerializedName("user_id")
    public int userId;

    @SerializedName("spiece")
    public String dogType;

    @SerializedName("gender")
    public String dogGender;

    @SerializedName("age")
    public String dogAge;

    @SerializedName("region1")
    public String region1;

    @SerializedName("region2")
    public String region2;

    @SerializedName("price")
    public String dogPrice;

    @SerializedName("size")
    public String dogSize;

    @SerializedName("introduction")
    public String postIntro;

    @SerializedName("condition")
    public String postCondition;

    @SerializedName("fur")
    public String dogColor;

    @SerializedName("lineage")
    public String bloodImageUrl;

    @SerializedName("pet_thumbnail")
    public String dogImageUrl;

    @SerializedName("title")
    public String postTitle;

    @SerializedName("is_parceled")
    public int isDistributed;

    @SerializedName("kennel")
    public int vaccinKennel;

    @SerializedName("corona")
    public int vaccinCorona;

    @SerializedName("DHPPL")
    public int vacinnDHPPL;

    @SerializedName("username")
    public String username;

    @SerializedName("parent_pet_images")
    public List<ParentDogImage> parentDogImage;

    @SerializedName("pet_images")
    public List<DogImage> dogImage;

    @SerializedName("favorite_number")
    public int favoriteNum;

    public PostDetailModel(int postId, int userId, String dogType, String dogGender, String dogAge, String region1, String region2, String dogPrice, String dogSize, String postIntro, String postCondition, String dogColor, String bloodImageUrl, String dogImageUrl, String postTitle, int isDistributed, int vaccinKennel, int vaccinCorona, int vacinnDHPPL, String username, List<ParentDogImage> parentDogImage, List<DogImage> dogImage, int favoriteNum) {
        this.postId = postId;
        this.userId = userId;
        this.dogType = dogType;
        this.dogGender = dogGender;
        this.dogAge = dogAge;
        this.region1 = region1;
        this.region2 = region2;
        this.dogPrice = dogPrice;
        this.dogSize = dogSize;
        this.postIntro = postIntro;
        this.postCondition = postCondition;
        this.dogColor = dogColor;
        this.bloodImageUrl = bloodImageUrl;
        this.dogImageUrl = dogImageUrl;
        this.postTitle = postTitle;
        this.isDistributed = isDistributed;
        this.vaccinKennel = vaccinKennel;
        this.vaccinCorona = vaccinCorona;
        this.vacinnDHPPL = vacinnDHPPL;
        this.username = username;
        this.parentDogImage = parentDogImage;
        this.dogImage = dogImage;
        this.favoriteNum = favoriteNum;
    }

    protected PostDetailModel(Parcel in) {
        postId = in.readInt();
        userId = in.readInt();
        dogType = in.readString();
        dogGender = in.readString();
        dogAge = in.readString();
        region1 = in.readString();
        region2 = in.readString();
        dogPrice = in.readString();
        dogSize = in.readString();
        postIntro = in.readString();
        postCondition = in.readString();
        dogColor = in.readString();
        bloodImageUrl = in.readString();
        dogImageUrl = in.readString();
        postTitle = in.readString();
        isDistributed = in.readInt();
        vaccinKennel = in.readInt();
        vaccinCorona = in.readInt();
        vacinnDHPPL = in.readInt();
        username = in.readString();
        dogImage = in.createTypedArrayList(DogImage.CREATOR);
        favoriteNum = in.readInt();
    }

    public static final Creator<PostDetailModel> CREATOR = new Creator<PostDetailModel>() {
        @Override
        public PostDetailModel createFromParcel(Parcel in) {
            return new PostDetailModel(in);
        }

        @Override
        public PostDetailModel[] newArray(int size) {
            return new PostDetailModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(postId);
        dest.writeInt(userId);
        dest.writeString(dogType);
        dest.writeString(dogGender);
        dest.writeString(dogAge);
        dest.writeString(region1);
        dest.writeString(region2);
        dest.writeString(dogPrice);
        dest.writeString(dogSize);
        dest.writeString(postIntro);
        dest.writeString(postCondition);
        dest.writeString(dogColor);
        dest.writeString(bloodImageUrl);
        dest.writeString(dogImageUrl);
        dest.writeString(postTitle);
        dest.writeInt(isDistributed);
        dest.writeInt(vaccinKennel);
        dest.writeInt(vaccinCorona);
        dest.writeInt(vacinnDHPPL);
        dest.writeString(username);
        dest.writeTypedList(dogImage);
        dest.writeInt(favoriteNum);
    }
}
