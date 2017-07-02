package org.tacademy.woof.doguendoguen.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.app.base.message.MessageDetailActivity;
import org.tacademy.woof.doguendoguen.model.ChattingRoom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tak on 2017. 5. 31..
 */

public class MessageListsAdapter extends RecyclerView.Adapter<MessageListsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ChattingRoom> chattingRooms;

    public MessageListsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mesage_item, null, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MessageListsAdapter.ViewHolder holder, final int position) {
        if(chattingRooms.size() != 0) {
            ChattingRoom chattingRoom = chattingRooms.get(position);
            holder.userName.setText(chattingRoom.senderName);
            holder.messageTime.setText(chattingRoom.sendTime);
            holder.messageContent.setText(chattingRoom.content);
            Glide.with(DoguenDoguenApplication.getContext())
                    .load(chattingRoom.senderThumbnail)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.userImage);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DoguenDoguenApplication.getContext(), MessageDetailActivity.class);
                    intent.putExtra("root", "MessageListsAdapter");
                    intent.putExtra("participantId", chattingRooms.get(position).participantId);
                    context.startActivity(intent);
                    Log.d("adapter roomId : ", chattingRooms.get(position).participantId + " ");

                    }
            });
        }
    }

    @Override
    public int getItemCount() {
        return chattingRooms != null ? chattingRooms.size() : 0;
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

    public void addMessageList(List<ChattingRoom> chattingRooms) {
        this.chattingRooms = (ArrayList<ChattingRoom>) chattingRooms;
    }
}
