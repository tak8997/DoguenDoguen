package org.tacademy.woof.doguendoguen.model.command;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tak on 2017. 5. 29..
 */

public class PostCommand {
    //18개
    @SerializedName("user_id")
    int userId;

    @SerializedName("title")
    String title;

    @SerializedName("spiece")
    String spiece;

    @SerializedName("gender")
    String gender;

    @SerializedName("age")
    String age;

    @SerializedName("region1")
    String region1;

    @SerializedName("region2")
    String region2;

    @SerializedName("price")
    String price;

    @SerializedName("condition")
    String condition;

    @SerializedName("introduction")
    String introduction;

    @SerializedName("size")
    String size;

    @SerializedName("fur")
    String fur;

    @SerializedName("kennel")
    int kennel;

    @SerializedName("corona")
    int corona;

    @SerializedName("dhppl")
    int dhppl;

    @SerializedName("pet_image")    //파일 리스트
    String petImage;

    @SerializedName("parent_pet")   //파일 리스트
    String parentPet;

    @SerializedName("lineage")      //파일
    String lineage;

    public PostCommand(int userId, String title, String spiece, String gender, String age, String region1, String region2, String price, String condition, String introduction, String size, String fur, int kennel, int corona, int dhppl) {
        this.userId = userId;
        this.title = title;
        this.spiece = spiece;
        this.gender = gender;
        this.age = age;
        this.region1 = region1;
        this.region2 = region2;
        this.price = price;
        this.condition = condition;
        this.introduction = introduction;
        this.size = size;
        this.fur = fur;
        this.kennel = kennel;
        this.corona = corona;
        this.dhppl = dhppl;
//        this.petImage = petImage;
//        this.parentPet = parentPet;
//        this.lineage = lineage;
    }
}