package org.tacademy.woof.doguendoguen.model.command;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tak on 2017. 5. 29..
 */

public class PostCommand {
    @SerializedName("user_token")
    String userToken;

    @SerializedName("title")
    String title;

    @SerializedName("spiece")
    String spiece;

    @SerializedName("gender")
    String gender;

    @SerializedName("age")
    String age;

    @SerializedName("region")
    String region;

    @SerializedName("price")
    String price;

    @SerializedName("size")
    String size;

    @SerializedName("content")
    String content;

    @SerializedName("fur")
    String fur;

    @SerializedName("vaccination")
    String vaccination;

    @SerializedName("condition")
    String condition;

    @SerializedName("pet_image")
    String petImage;

    @SerializedName("parent_pet")   //파일
    String parentPet;

    @SerializedName("lineage")      //파일
    String lineage;

    public PostCommand(String userToken, String title, String spiece, String gender, String age, String region, String price, String size, String content, String fur, String vaccination, String condition, String petImage, String parentPet, String lineage) {
        this.userToken = userToken;
        this.title = title;
        this.spiece = spiece;
        this.gender = gender;
        this.age = age;
        this.region = region;
        this.price = price;
        this.size = size;
        this.content = content;
        this.fur = fur;
        this.vaccination = vaccination;
        this.condition = condition;
        this.petImage = petImage;
        this.parentPet = parentPet;
        this.lineage = lineage;
    }
}