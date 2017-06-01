package org.tacademy.woof.doguendoguen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.tacademy.woof.doguendoguen.R;

import java.util.zip.Inflater;

/**
 * Created by Tak on 2017. 6. 1..
 */

public class UserPostAdapter extends RecyclerView.Adapter<UserPostAdapter.ViewHolder> {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_post_item, null, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(UserPostAdapter.ViewHolder holder, int position) {
        holder.userPostImage.setImageResource(R.drawable.girls_eneration_taeyeon);
        holder.userPostTitle.setText("새로운 가족을 기다립니다");
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userPostImage;
        TextView userPostTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            userPostImage = (ImageView) itemView.findViewById(R.id.user_post_image);
            userPostTitle = (TextView) itemView.findViewById(R.id.user_post_title);
        }
    }

    public void addUserPost() {

    }
}








