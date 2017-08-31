package org.tacademy.woof.doguendoguen.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tak on 2017. 6. 18..
 */

public class ChattingRoom {
    @SerializedName("participant_id")
    @Expose
    public int participantId;

    @SerializedName("sent_time")
    @Expose
    public String sendTime;

    @SerializedName("sender_time")
    @Expose
    public String senderName;

    @SerializedName("sender_thumbnail")
    @Expose
    public String senderThumbnail;

    @SerializedName("content")
    @Expose
    public String content;

    @SerializedName("unread_count")
    @Expose
    public int unreadCount;

    public ChattingRoom(int participantId, String sendTime, String senderName, String senderThumbnail, String content, int unreadCount) {
        this.participantId = participantId;
        this.sendTime = sendTime;
        this.senderName = senderName;
        this.senderThumbnail = senderThumbnail;
        this.content = content;
        this.unreadCount = unreadCount;
    }
}
