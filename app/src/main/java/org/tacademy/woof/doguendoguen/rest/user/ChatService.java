package org.tacademy.woof.doguendoguen.rest.user;

import org.tacademy.woof.doguendoguen.model.ChattingRoom;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by Tak on 2017. 7. 28..
 */

public interface ChatService {
    @GET("/chats")
    Observable<List<ChattingRoom>> getChatRoom();
}
