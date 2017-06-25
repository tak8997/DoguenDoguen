package org.tacademy.woof.doguendoguen.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tak on 2017. 6. 14..
 */

public class UserIdModel {
    @SerializedName("user_id")
    public int userId;

    @SerializedName("Message")
    public String message;

    public UserIdModel(int userId, String message) {
        this.userId = userId;
        this.message = message;
    }
}
