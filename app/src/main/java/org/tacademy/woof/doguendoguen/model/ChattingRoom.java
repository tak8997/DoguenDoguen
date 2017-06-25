package org.tacademy.woof.doguendoguen.model;

/**
 * Created by Tak on 2017. 6. 18..
 */

public class ChattingRoom {
    public String roomId;
    public String sendTime;
    public String senderName;
    public String senderThumbnail;
    public String content;
    public int unreadCount;

    public ChattingRoom(String roomId, String sendTime, String senderName, String senderThumbnail, String content, int unreadCount) {
        this.roomId = roomId;
        this.sendTime = sendTime;
        this.senderName = senderName;
        this.senderThumbnail = senderThumbnail;
        this.content = content;
        this.unreadCount = unreadCount;
    }
}
