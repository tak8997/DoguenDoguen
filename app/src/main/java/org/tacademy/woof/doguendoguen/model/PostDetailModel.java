package org.tacademy.woof.doguendoguen.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Tak on 2017. 6. 6..
 */

public class PostDetailModel {

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
    public int dogSize;

    @SerializedName("introduction")
    public String postIntro;

    @SerializedName("condition")
    public String postCondition;

    @SerializedName("fur")
    public int dogColor;

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

    public PostDetailModel(int postId, int userId, String dogType, String dogGender, String dogAge, String region1, String region2, String dogPrice, int dogSize, String postIntro, String postCondition, int dogColor, String bloodImageUrl, String dogImageUrl, String postTitle, int isDistributed, int vaccinKennel, int vaccinCorona, int vacinnDHPPL, String username, List<ParentDogImage> parentDogImage, List<DogImage> dogImage, int favoriteNum) {
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
}
