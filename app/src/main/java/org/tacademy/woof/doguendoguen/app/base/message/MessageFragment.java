package org.tacademy.woof.doguendoguen.app.base.message;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.adapter.MessageListsAdapter;
import org.tacademy.woof.doguendoguen.model.ChattingRoom;
import org.tacademy.woof.doguendoguen.util.SharedPreferencesUtil;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageFragment extends Fragment {
    private static final String USER_ID = "userId";

    public MessageFragment() {
    }

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

//    private Socket mSocket;{
//        try {
//            mSocket = IO.socket("http://13.124.26.143:3000");
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

//        messageListsAdapter= new MessageListsAdapter(getContext());
//        chattingRooms = new ArrayList<>();
//        mSocket.connect();
    }
//    MessageListsAdapter messageListsAdapter;
//    int userId;
//
//    @BindView(R.id.message_recyclerview) RecyclerView messageRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);

//        userId = Integer.parseInt(SharedPreferencesUtil.getInstance().getUserId());
//        Log.d("MessageFragment", userId + " ");
//
//        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
//        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
//        mSocket.on("showListResults", chattingRoomList);
//
//        if (userId != 0)
//            mSocket.emit("showList", userId);
//
//        messageRecyclerView.setLayoutManager(new LinearLayoutManager(DoguenDoguenApplication.getContext()));
//        messageRecyclerView.setAdapter(messageListsAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

//
//    List<ChattingRoom> chattingRooms;
//    ChattingRoom chattingRoom;
//    private Emitter.Listener chattingRoomList = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            Log.d("Message" , "onMessage" + args + "," + args[0]);
//
//             getActivity().runOnUiThread(new Runnable() {
//                 @Override
//                 public void run() {
//                     String roomId;
//                     String sendTime;
//                     String senderName;
//                     String senderThumbnail;
//                     String content;
//                     int unreadCount;
//
//                     try {
//                         JSONObject room = (JSONObject) args[0];
//                         JSONArray roomList = room.getJSONArray("roomList");
//
//                         for (int i=0; i<roomList.length(); i++) {
//                             roomId = roomList.getJSONObject(i).getString("room_id");
//                             sendTime = roomList.getJSONObject(i).getString("sent_time");
//                             senderName = roomList.getJSONObject(i).getString("sender_name");
//                             senderThumbnail = roomList.getJSONObject(i).getString("sender_thumbnail");
//                             content = roomList.getJSONObject(i).getString("content");
//                             unreadCount = roomList.getJSONObject(i).getInt("unread_count");
//
//                             chattingRoom = new ChattingRoom(roomId, sendTime, senderName, senderThumbnail, content, unreadCount);
//                             chattingRooms.add(chattingRoom);
//
//                             Log.d("aasdf" , roomId + " asdf" );
//                         }
//                         addChattinRommList(chattingRooms);
//                     } catch (JSONException e) {
//                         e.printStackTrace();
//                     }
//                 }
//             });//쓰레드 end
//        }
//    };
//
//    private Emitter.Listener onConnectError = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(getActivity().getApplicationContext(),
//                            "서버와 연결 실패", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    };
//
//    private void addChattinRommList(List<ChattingRoom> chattingRooms) {
//        messageListsAdapter.addMessageList(chattingRooms);
//        messageListsAdapter.notifyDataSetChanged();
//
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        mSocket.disconnect();
//        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
//        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
//        mSocket.off("showListResults", chattingRoomList);
//    }
}
