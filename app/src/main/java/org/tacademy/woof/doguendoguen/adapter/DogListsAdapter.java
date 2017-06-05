package org.tacademy.woof.doguendoguen.adapter;

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
import org.tacademy.woof.doguendoguen.model.PostListModel;

import java.util.ArrayList;

/**
 * Created by Tak on 2017. 5. 29..
 */

public class DogListsAdapter extends RecyclerView.Adapter<DogListsAdapter.ViewHolder>{
    ArrayList<PostListModel> postLists;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dog_post_item, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PostListModel postList = postLists.get(position);

        Glide.with(DoguenDoguenApplication.getContext())
                .load(postList.petImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.dog_sample)
                .into(holder.postImage);

        holder.postTitle.setText(postList.title);
        holder.postUserName.setText(postList.username);
    }


    @Override
    public int getItemCount() {
        return postLists != null ? postLists.size() : 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage;
        TextView postTitle;
        TextView postUserName;

        public ViewHolder(View itemView) {
            super(itemView);

            postImage = (ImageView) itemView.findViewById(R.id.post_image);
            postTitle = (TextView) itemView.findViewById(R.id.post_title);
            postUserName = (TextView) itemView.findViewById(R.id.post_user_name);
        }
    }

    public void addPost(ArrayList<PostListModel> postLists) {
        this.postLists = postLists;
    }
}
