package org.tacademy.woof.doguendoguen.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tak on 2017. 6. 6..
 */

public class DogImage {
    @SerializedName("image_id")
    public int imageId;

    @SerializedName("image")
    public String dogImageUrl;

    public DogImage(int imageId, String dogImageUrl) {
        this.imageId = imageId;
        this.dogImageUrl = dogImageUrl;
    }
}
