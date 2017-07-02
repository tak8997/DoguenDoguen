package org.tacademy.woof.doguendoguen.model;

/**
 * Created by Tak on 2017. 6. 18..
 */

public class ChattingRoom {
    public int participantId;
    public String sendTime;
    public String senderName;
    public String senderThumbnail;
    public String content;
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
