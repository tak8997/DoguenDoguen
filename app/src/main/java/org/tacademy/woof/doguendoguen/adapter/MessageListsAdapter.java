package org.tacademy.woof.doguendoguen.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.app.base.message.MessageDetailActivity;
import org.tacademy.woof.doguendoguen.model.ChattingRoomList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tak on 2017. 5. 31..
 */

public class MessageListsAdapter extends RecyclerView.Adapter<MessageListsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ChattingRoomList> chattingRoomLists;

    public MessageListsAdapter(Context context, List<ChattingRoomList> chattingRoomLists) {
        this.context = context;
        this.chattingRoomLists = (ArrayList<ChattingRoomList>) chattingRoomLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mesage_item, null, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MessageListsAdapter.ViewHolder holder, final int position) {
        holder.userImage.setImageResource(R.drawable.dog_sample);
        holder.userName.setText("유저 이름");
        holder.messageTime.setText("시간");
        holder.messageContent.setText("메시지 내용입니다");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoguenDoguenApplication.getContext(), MessageDetailActivity.class);
                intent.putExtra("roomId", chattingRoomLists.get(position).roomId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userName;
        TextView messageTime;
        TextView messageContent;

        public ViewHolder(View itemView) {
            super(itemView);

            userImage = (ImageView) itemView.findViewById(R.id.message_user_image);
            userName = (TextView) itemView.findViewById(R.id.message_user_name);
            messageTime = (TextView) itemView.findViewById(R.id.message_time);
            messageContent = (TextView) itemView.findViewById(R.id.message_content);
        }
    }

    public void addMessageList() {

    }
}
