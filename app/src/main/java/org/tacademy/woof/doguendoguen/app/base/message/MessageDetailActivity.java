package org.tacademy.woof.doguendoguen.app.base.message;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.adapter.MessageListsAdapter;
import org.tacademy.woof.doguendoguen.app.base.profile.PostEdit_25_Dialog;
import org.tacademy.woof.doguendoguen.app.base.search.PostDeleteDialogFragment;
import org.tacademy.woof.doguendoguen.app.base.search.PostDetailActivity;
import org.tacademy.woof.doguendoguen.app.base.search.PostReportDialogFragment;
import org.tacademy.woof.doguendoguen.app.base.search.ReportReasonDialogFragment;
import org.tacademy.woof.doguendoguen.app.home.BaseActivity;
import org.tacademy.woof.doguendoguen.model.Message;
import org.tacademy.woof.doguendoguen.rest.RestService;
import org.tacademy.woof.doguendoguen.rest.post.PostService;
import org.tacademy.woof.doguendoguen.util.ConvertPxToDpUtil;
import org.tacademy.woof.doguendoguen.util.SharedPreferencesUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.data;
import static android.R.id.list;
import static android.media.CamcorderProfile.get;
import static org.tacademy.woof.doguendoguen.R.id.report;
import static org.tacademy.woof.doguendoguen.R.id.user;

/**
 * Created by Tak on 2017. 6. 5..
 */

public class MessageDetailActivity extends BaseActivity {
//    @BindView(R.id.message_toolbar) Toolbar toolbar;
    @BindView(R.id.message_detail_recyclerview) RecyclerView messageRecyclerView;
    @BindView(R.id.send) TextView send;
    @BindView(R.id.edit_message) EditText editMessage;
//    @BindView(R.id.overflow_dots) ImageView overflowMenu;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://13.124.26.143:3000/");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    MessageDetailAdapter messageDetailAdapter;
    String roomId;
    int otherUserId;
    int userId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        ButterKnife.bind(this);
        mSocket.connect();

        userId = Integer.parseInt(SharedPreferencesUtil.getInstance().getUserId());

        Log.d("msg", userId + " msg");
        Intent intent = getIntent();
        if(intent != null) {
            if(intent.getExtras().getString("root").equals("MessageListsAdapter")) {
                roomId = intent.getExtras().getString("roomId");    //MessageListsAdapter로 부터 받아옴.
                Log.d("roomId", roomId);

                mSocket.on("enterRoomResults", chattingList);
                mSocket.on("sendMessageResults", newMessage);
                mSocket.emit("enterRoom", roomId);
            } else {                                                ////PostDetailActivity로 부터도 받아옴
//                mSocket.emit("showList", "안녕하세요. 푸들 입양받고 싶습니다." );
                otherUserId = intent.getExtras().getInt("userId");   //Int

                mSocket.emit("enterRoom", otherUserId);
                mSocket.on("sendMessageResults", newMessage);
            }
        }

        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageDetailAdapter = new MessageDetailAdapter(this);
        messageRecyclerView.scrollToPosition(messageDetailAdapter.getItemCount() - 1);
        messageRecyclerView.setAdapter(messageDetailAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    String msg;
    @OnClick(R.id.send)
    public void onMessageSendClicked() {
        Log.d("MessageSend!", "AA");
        msg = editMessage.getText().toString().trim();
        if (TextUtils.isEmpty(msg)) {
            return;
        }

        JSONObject sendJson = null;
        try {
            sendJson = new JSONObject();
            sendJson.put("user_id", userId);
            sendJson.put("content", msg);
            sendJson.put("room_id", roomId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "aaa", Toast.LENGTH_SHORT).show();
        editMessage.setText("");

        mSocket.emit("sendMessage", sendJson);
    }


    //PostDetailActivity, MessegeListsAdapter 로 부터. 새로운 메시지를 받아옴.
    private Emitter.Listener newMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int senderId;
                    String senderThumbnail;
                    String senderName;
                    String content;
                    String side;

                    Message message;

                    try {
                        JSONObject msgObject = (JSONObject) args[0];
                        JSONObject msg = msgObject.getJSONObject("newMessage");

                        senderId = msg.getInt("sender_id");
                        senderThumbnail = msg.getString("sender_thumbnail");
                        senderName = msg.getString("senderName");
                        content = msg.getString("content");
                        side = msg.getString("side");

                        message = new Message(senderId, senderThumbnail, senderName, content, side);
                        Log.d("MessageDetail", "senderId, thumbmail, name, content : " + senderId + senderName);

                        messageDetailAdapter.addMessage(message);
                        messageDetailAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };


    private Emitter.Listener chattingList = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    int senderId;
                    String senderName = null;
                    String senderThumbnail;
                    String content;
                    String side;

                    Message message;

                    try {
                        JSONObject roomInfos = (JSONObject) args[0];
                        JSONObject roomInfo = roomInfos.getJSONObject("roomInfos");
                        int participandId = roomInfo.getInt("participant_id");
                        int userId = roomInfo.getInt("user_id");
                        JSONArray messages = roomInfo.getJSONArray("messages");

                        Log.d("MessageDetailActivity", "participant id, user id : " + participandId + ", " + userId);
                         for(int i=0; i<messages.length(); i++) {
                            senderId = messages.getJSONObject(i).getInt("sender_id");
                            senderThumbnail = messages.getJSONObject(i).getString("sender_thumbnail");
                            senderName = messages.getJSONObject(i).getString("sender_name");
                            content = messages.getJSONObject(i).getString("content");
                            side = messages.getJSONObject(i).getString("side");

                            message = new Message(senderId, senderThumbnail, senderName, content, side);
                            Log.d("MessageDetail", "senderId, thumbmail, name, content : " + senderId + senderName);

                            messageDetailAdapter.addMessage(message);
                            messageDetailAdapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

        mSocket.disconnect();
        mSocket.off("enterRoomResults", chattingList);
        mSocket.off("sendMessageResults", newMessage);
    }


    private class MessageDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ArrayList<Message> messages = new ArrayList<>();

        private static final int VIEW_TYPE_OTHER_USER = 0;
        private static final int VIEW_TYPE_USER = 1;

        public MessageDetailAdapter(Context context) {
        }

        @Override
        public int getItemViewType(int position) {
//            int location = position % 2;
            Message msg = messages.get(position);

            switch (msg.side) {
                case "left":
                    return VIEW_TYPE_OTHER_USER;
                case "right":
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
                case VIEW_TYPE_OTHER_USER:  //상대방 왼쪽쪽
                    view = layoutInflater.inflate(R.layout.message_other_user_item, parent, false);
                    viewHolder = new OtherUserViewHolder(view);
                    break;
                case VIEW_TYPE_USER:    //본인 오른쪽
                    view = layoutInflater.inflate(R.layout.message_user_item, parent, false);
                    viewHolder = new UserViewHolder(view);
                    break;
            }

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Message msg = messages.get(position);


            switch (getItemViewType(position)) {
                case VIEW_TYPE_OTHER_USER://상대방 왼쪽
                    OtherUserViewHolder otherUserHolder = (OtherUserViewHolder) holder;
                    otherUserHolder.otherUserName.setText(msg.senderName);
                    otherUserHolder.otherUserMessage.setText(msg.content);
                    Glide.with(DoguenDoguenApplication.getContext())
                            .load(msg.senderThumbnail)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(otherUserHolder.otherUserImage);
                    break;
                case VIEW_TYPE_USER:    //본인 오른쪽            key:"side" value: "left" "right"
                    UserViewHolder userHolder = (UserViewHolder) holder;
                    userHolder.userMessage.setText(msg.content);
                    Glide.with(DoguenDoguenApplication.getContext())
                            .load(msg.senderThumbnail)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(userHolder.userImage);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        public void addMessage(Message message) {
            messages.add(message);
        }
    }

    @BindView(R.id.overflow_dots) ImageView overflowMenu;
    boolean isSelected = false;
    PopupWindow reportPopup;
    @OnClick({R.id.back, R.id.overflow_dots})
    public void onToobarIconClicked(View view){
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.overflow_dots:
                floatDialog();
                break;
        }

    }

    private void floatDialog() {
        if(isSelected == false) {
            View view = getLayoutInflater().inflate(R.layout.report_layout, null);
            view.setBackgroundColor(Color.WHITE);
//                    view.setPadding();

            reportPopup = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            reportPopup.showAtLocation(view, Gravity.TOP, 450, (int) ConvertPxToDpUtil.convertDpToPixel(75, MessageDetailActivity.this));
            reportPopup.setOutsideTouchable(true);
            reportPopup.showAsDropDown(view);

            view.findViewById(report).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reportPopup.dismiss();

                    final PostReportDialogFragment postReportDialogFragment = new PostReportDialogFragment();
                    postReportDialogFragment.show(getSupportFragmentManager(), "postReport");
                    postReportDialogFragment.setOnAdapterItemClickListener(new PostReportDialogFragment.OnAdapterItemClickLIstener() {
                        @Override
                        public void onAdapterItemClick(String answer) {
                            if(answer.equals("yes")) {
                                final ReportReasonDialogFragment reportReasonDialogFragment = new ReportReasonDialogFragment();
                                reportReasonDialogFragment.show(getSupportFragmentManager(), "reportReason");
                                reportReasonDialogFragment.setOnAdapterItemClickListener(new ReportReasonDialogFragment.OnAdapterItemClickLIstener() {
                                    @Override
                                    public void onAdapterItemClick(String answer) {
                                        if (answer.equals("yes"))
                                            Toast.makeText(MessageDetailActivity.this, "김다은님을 신고하였습니다.", Toast.LENGTH_SHORT).show();
                                        else
                                            reportReasonDialogFragment.dismiss();
                                    }
                                });
                            } else {
                                postReportDialogFragment.dismiss();
                            }
                        }
                    });
                }
            });
            view.findViewById(R.id.block).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            isSelected = true;
        } else {
            reportPopup.dismiss();
            isSelected = false;
        }
    }
}
