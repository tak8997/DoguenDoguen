package org.tacademy.woof.doguendoguen.app.base.message;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.adapter.MessageListsAdapter;
import org.tacademy.woof.doguendoguen.model.ChattingRoomList;
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

    private Socket mSocket;{
        try {
            mSocket = IO.socket("http://13.124.26.143:3000/");
        } catch (URISyntaxException e) {}
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        mSocket.connect();
        mSocket.on("showListResults", chattingRoomList);
    }
    String userId;

    @BindView(R.id.message_recyclerview) RecyclerView messageRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);

        userId = SharedPreferencesUtil.getInstance().getUserId();

        messageRecyclerView.setLayoutManager(new LinearLayoutManager(DoguenDoguenApplication.getContext()));

        MessageListsAdapter messageListsAdapter = new MessageListsAdapter(getContext(), chattingRoomLists);
        messageListsAdapter.addMessageList();
        messageListsAdapter.notifyDataSetChanged();

        messageRecyclerView.setAdapter(messageListsAdapter);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (userId != null)
            mSocket.emit("showList", userId);
    }

    List<ChattingRoomList> chattingRoomLists;
    private Emitter.Listener chattingRoomList = new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            chattingRoomLists = new ArrayList<>();
            Log.d("message" , "onMessage" + args + "," + args[0] + ", " + args[0].toString());

            try {
                JSONArray chatList = new JSONArray(args);
                Log.d("asdf", "a" + chatList.getJSONObject(0).getString("room_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            try {
//                Log.d("asdf", "a" + chatList.getJSONObject(0).getString("room_id"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            String roomId;
//            String sendTime;
//            String senderName;
//            String senderThumbnail;
//            String content;
//            int unreadCount;
//            try {
////                JSONArray chatLists = chatList.getJSONArray("");
//                for(int i=0; i<chatList.length(); i++) {
//                    JSONObject json = chatList.getJSONObject(i);
//                    roomId = json.getString("room_id");
//                    sendTime = json.getString("sent_time");
//                    senderName = json.getString("sender_name");
//                    senderThumbnail = json.getString("sender_thumbmail");
//                    content = json.getString("content");
//                    unreadCount = json.getInt("unread_count");
//                    Log.d("message" , "romId: " + roomId + ", " + senderName);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("showListResults", chattingRoomList);
    }
}
