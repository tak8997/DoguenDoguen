package org.tacademy.woof.doguendoguen.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tak on 2017. 6. 6..
 */

public class ParentDogImage {
    @SerializedName("image_id")
    @Expose
    public int imageId;

    @SerializedName("image")
    @Expose
    public String parentDogImageUrl;

    public ParentDogImage(int imageId, String parentDogImageUrl) {
        this.imageId = imageId;
        this.parentDogImageUrl = parentDogImageUrl;
    }
}
