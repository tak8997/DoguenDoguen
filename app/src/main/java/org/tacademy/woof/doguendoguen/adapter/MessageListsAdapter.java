package org.tacademy.woof.doguendoguen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.tacademy.woof.doguendoguen.R;

/**
 * Created by Tak on 2017. 5. 31..
 */

public class MessageListsAdapter extends RecyclerView.Adapter<MessageListsAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mesage_item, null, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MessageListsAdapter.ViewHolder holder, int position) {
        holder.userImage.setImageResource(R.drawable.girls_eneration_hyoyeon);
        holder.userName.setText("유저 이름");
        holder.messageTime.setText("시간");
        holder.messageContent.setText("메시지 내용입니다");
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