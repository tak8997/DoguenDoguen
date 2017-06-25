package org.tacademy.woof.doguendoguen.model;

/**
 * Created by Tak on 2017. 6. 18..
 */

public class Message {
    public int senderId;
    public String senderThumbnail;
    public String senderName;
    public String content;
    public String side; //배치 관련.

    public Message(int senderId, String senderThumbnail, String senderName, String content, String side) {
        this.senderId = senderId;
        this.senderThumbnail = senderThumbnail;
        this.senderName = senderName;
        this.content = content;
        this.side = side;
    }
}
