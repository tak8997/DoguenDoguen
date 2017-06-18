package org.tacademy.woof.doguendoguen.app.base.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.adapter.MessageListsAdapter;

import java.net.URISyntaxException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.data;

/**
 * Created by Tak on 2017. 6. 5..
 */

public class MessageDetailActivity extends AppCompatActivity{
    @BindView(R.id.message_toolbar) Toolbar toolbar;
    @BindView(R.id.message_detail_recyclerview) RecyclerView messageRecyclerView;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    String roomId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        ButterKnife.bind(this);
        mSocket.connect();

        Intent intent = getIntent();
        if(intent != null) {
            roomId = intent.getExtras().getString("roomId");
            Log.d("roomId", roomId);
        }


        setSupportToolBar();

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("다은");
        arrayList.add("연수");
        arrayList.add("다은");
        arrayList.add("연수");
        arrayList.add("다은");
        arrayList.add("연수");
        
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MessageDetailAdapter messageDetailAdapter = new MessageDetailAdapter(this);
        messageRecyclerView.setAdapter(messageDetailAdapter);
        messageDetailAdapter.addItem(arrayList);
        messageDetailAdapter.notifyDataSetChanged();

        mSocket.on("enterRoomResults", chattingList);
    }
    private Emitter.Listener chattingList = new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            JSONObject chatList = (JSONObject) args[0];

            int participantId;
            int userId;
            JSONArray messages;
            try {
                participantId = chatList.getInt("participant_id");
                userId = chatList.getInt("user_id");
                messages = chatList.getJSONArray("messages");
            } catch (JSONException e) {
                return;
            }
        }
    };
    @Override
    public void onStart() {
        super.onStart();
        mSocket.emit("enterRoom", roomId);
    }

    private void setSupportToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.left_arrow_message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.home) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MessageDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ArrayList<String> arrayList;

        private static final int VIEW_TYPE_OTHER_USER = 0;
        private static final int VIEW_TYPE_USER = 1;

        public MessageDetailAdapter(Context context) {
        }

        @Override
        public int getItemViewType(int position) {
            int location = position % 2;

            switch (location) {
                case VIEW_TYPE_OTHER_USER:
                    return VIEW_TYPE_OTHER_USER;
                case VIEW_TYPE_USER:
                    return VIEW_TYPE_USER;
            }

            return super.getItemViewType(position);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view;
            RecyclerView.ViewHolder viewHolder = null;

            switch (viewType) {
                case VIEW_TYPE_OTHER_USER:
                    view = layoutInflater.inflate(R.layout.message_other_user_item, parent, false);
                    viewHolder = new OtherUserViewHolder(view);
                    break;
                case VIEW_TYPE_USER:
                    view = layoutInflater.inflate(R.layout.message_user_item, parent, false);
                    viewHolder = new UserViewHolder(view);
                    break;
            }


            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            String user = arrayList.get(position);
            switch (getItemViewType(position)) {
                case VIEW_TYPE_OTHER_USER:
                    OtherUserViewHolder otherUserHolder = (OtherUserViewHolder) holder;
                    otherUserHolder.otherUser.setText(user);
                    break;
                case VIEW_TYPE_USER:
                    UserViewHolder userHolder = (UserViewHolder) holder;
                    userHolder.user.setText(user);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public void addItem(ArrayList<String> arrayList) {
            this.arrayList = arrayList;
        }
    }
}
