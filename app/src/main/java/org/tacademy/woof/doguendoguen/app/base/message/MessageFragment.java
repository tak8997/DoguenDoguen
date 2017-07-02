package org.tacademy.woof.doguendoguen.app.base.message;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.adapter.MessageListsAdapter;
import org.tacademy.woof.doguendoguen.model.ChattingRoom;
import org.tacademy.woof.doguendoguen.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static android.content.ContentValues.TAG;

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

    private Socket mSocket;
    private Boolean isConnected = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MessageFragment", "onCreate");
        if (getArguments() != null) {
        }

        mSocket = DoguenDoguenApplication.getSocket();
        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("showListResults", chattingRoomList);
        mSocket.connect();
    }
    MessageListsAdapter messageListsAdapter;
    int userId;

    @BindView(R.id.message_recyclerview) RecyclerView messageRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);

        messageListsAdapter= new MessageListsAdapter(getContext());

        userId = Integer.parseInt(SharedPreferencesUtil.getInstance().getUserId());
        Log.d("MessageFragment", userId + " ");
        Log.d("MessageFragment", "onCreateView");

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messageRecyclerView.setLayoutManager(new LinearLayoutManager(DoguenDoguenApplication.getContext()));
        messageRecyclerView.setAdapter(messageListsAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (userId != 0)
            mSocket.emit("showList");
    }


    ChattingRoom chattingRoom;
    private Emitter.Listener chattingRoomList = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d("Message" , "onMessage" + args + "," + args[0]);

            final List<ChattingRoom> chattingRooms = new ArrayList<>();

             getActivity().runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     int participantId;
                     String sendTime;
                     String senderName;
                     String senderThumbnail;
                     String content;
                     int unreadCount;

                     try {
                         JSONObject room = (JSONObject) args[0];
                         JSONArray roomList = room.getJSONArray("roomList");

                         Log.d("aasdf" , roomList.toString() );
                         for (int i=0; i<roomList.length(); i++) {
                             participantId = roomList.getJSONObject(i).getInt("participant_id");
                             sendTime = roomList.getJSONObject(i).getString("sent_time");
                             senderName = roomList.getJSONObject(i).getString("sender_name");
                             senderThumbnail = roomList.getJSONObject(i).getString("sender_thumbnail");
                             content = roomList.getJSONObject(i).getString("content");
                             unreadCount = roomList.getJSONObject(i).getInt("unread_count");

                             chattingRoom = new ChattingRoom(participantId, sendTime, senderName, senderThumbnail, content, unreadCount);
                             chattingRooms.add(chattingRoom);

                             messageListsAdapter.addMessageList(chattingRooms);
                             messageListsAdapter.notifyDataSetChanged();

                             Log.d("aasdf" , participantId + " asdf" );
                         }

//                         addChattinRommList(chattingRooms);
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                 }
             });//쓰레드 end
        }
    };

    private void addChattinRommList(List<ChattingRoom> chattingRooms) {

    }

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "서버와 연결 실패", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("MessageFragment", "connected");
                        Toast.makeText(getActivity().getApplicationContext(), "연결되었습니다", Toast.LENGTH_LONG).show();
                }
            });
        }
    };
    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "diconnected");
                    isConnected = false;
                    Toast.makeText(DoguenDoguenApplication.getContext(),
                            "인터넷 연결상태를 확인해주세요", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("showListResults", chattingRoomList);
    }
}
