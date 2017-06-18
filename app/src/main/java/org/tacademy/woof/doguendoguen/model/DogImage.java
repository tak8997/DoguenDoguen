package org.tacademy.woof.doguendoguen.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tak on 2017. 6. 6..
 */

public class DogImage implements Parcelable {
    @SerializedName("image_id")
    public int imageId;

    @SerializedName("image")
    public String dogImageUrl;

    public DogImage(int imageId, String dogImageUrl) {
        this.imageId = imageId;
        this.dogImageUrl = dogImageUrl;
    }

    protected DogImage(Parcel in) {
        imageId = in.readInt();
        dogImageUrl = in.readString();
    }

    public static final Creator<DogImage> CREATOR = new Creator<DogImage>() {
        @Override
        public DogImage createFromParcel(Parcel in) {
            return new DogImage(in);
        }

        @Override
        public DogImage[] newArray(int size) {
            return new DogImage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imageId);
        dest.writeString(dogImageUrl);
    }
}
