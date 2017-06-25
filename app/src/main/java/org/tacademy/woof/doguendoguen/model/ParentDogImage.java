package org.tacademy.woof.doguendoguen.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tak on 2017. 6. 6..
 */

public class ParentDogImage implements Parcelable {
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

    protected ParentDogImage(Parcel in) {
        imageId = in.readInt();
        parentDogImageUrl = in.readString();
    }

    public static final Creator<ParentDogImage> CREATOR = new Creator<ParentDogImage>() {
        @Override
        public ParentDogImage createFromParcel(Parcel in) {
            return new ParentDogImage(in);
        }

        @Override
        public ParentDogImage[] newArray(int size) {
            return new ParentDogImage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imageId);
        dest.writeString(parentDogImageUrl);
    }
}
