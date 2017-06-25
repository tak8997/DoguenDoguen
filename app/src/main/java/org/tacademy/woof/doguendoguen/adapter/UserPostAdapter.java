package org.tacademy.woof.doguendoguen.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.app.base.profile.UserProfileDetailActivity;
import org.tacademy.woof.doguendoguen.app.base.search.PostDetailActivity;
import org.tacademy.woof.doguendoguen.model.UserModel;

/**
 * Created by Tak on 2017. 6. 1..
 */

public class UserPostAdapter extends RecyclerView.Adapter<UserPostAdapter.ViewHolder> {
    public static final int READ_USER_POST = 101;

    private UserModel userModel;
    UserProfileDetailActivity context;

    public UserPostAdapter(UserProfileDetailActivity context) {
        this.context = context;
    }

    public UserPostAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_user_post_item, null, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(UserPostAdapter.ViewHolder holder, final int position) {
        if(userModel.userPostList.size() != 0) {

            String postTitle = userModel.userPostList.get(position).postTitle;
            String postImageUrl = userModel.userPostList.get(position).postImageUrl;

            holder.userPostTitle.setText(postTitle);
            Glide.with(DoguenDoguenApplication.getContext())
                    .load(postImageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.userPostImage);

            final String uId = String.valueOf(userModel.userId);
            holder.userPostImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DoguenDoguenApplication.getContext(), PostDetailActivity.class);
                    intent.putExtra("userId", uId);
                    intent.putExtra("postId", userModel.userPostList.get(position).postId);
                    intent.putExtra("position", position);
                    intent.putExtra("myList", 1);   //내글 myList->1
                    context.startActivityForResult(intent, READ_USER_POST);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return userModel.userPostList.size();
    }

    public void removeUserPost(int position) {
        userModel.userPostList.remove(position);
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

    public void addUserPost(UserModel userModel) {
        this.userModel = userModel;
    }

}








