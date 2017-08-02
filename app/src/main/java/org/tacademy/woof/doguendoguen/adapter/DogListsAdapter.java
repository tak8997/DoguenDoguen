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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonObject;

import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.app.base.search.PostDetailActivity;
import org.tacademy.woof.doguendoguen.model.PostList;
import org.tacademy.woof.doguendoguen.rest.RestClient;
import org.tacademy.woof.doguendoguen.rest.user.UserService;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Tak on 2017. 5. 29..
 */

public class DogListsAdapter extends RecyclerView.Adapter<DogListsAdapter.ViewHolder>{
    private ArrayList<PostList> postLists = new ArrayList<>();
    private Context context;
    private String userId;


    public DogListsAdapter(Context context, String userId) {
        this.context = context;
        this.userId = userId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dog_post_item, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PostList postList = postLists.get(position);

        if(postList != null) {
            Log.d("dogList", ""+postList.favorite + ", " + postList.title);
            Glide.with(DoguenDoguenApplication.getContext())
                    .load(postList.petImageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.dog_sample)
                    .into(holder.postImage);
            holder.postTitle.setText(postList.title);
            holder.postUserName.setText(postList.username);
            if(postList.favorite == 1) {
                Log.d("dogList", "wishSelected");
                holder.wish.setImageResource(R.drawable.heart_wish_bigger);
            }

            holder.postImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("DogListsAdapter", postList.postId + ", " + userId);
                    Intent intent = new Intent(DoguenDoguenApplication.getContext(), PostDetailActivity.class);
                    intent.putExtra("ishWish", postList.favorite);
                    intent.putExtra("postId", postList.postId);
                    intent.putExtra("userId", userId);
                    context.startActivity(intent);
                }
            });
            holder.wish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(postList.favorite == 1) {    //하트가 찍혀있다면 제거
                        holder.wish.setImageResource(R.drawable.heart);
                        Log.d("dogList", postList.favorite + "wish remove");
                        removeWish(position);
                    } else if(postList.favorite == 0) { //하트가 안찍혀있다면 추가
                        holder.wish.setImageResource(R.drawable.heart_wish_bigger);
                        Log.d("dogList", postList.favorite + "wish add");
                        addWish(position);
                    }

                }
            });
        }
    }

    private void removeWish(int position) {
        UserService userService = RestClient.createService(UserService.class);
        Observable<JsonObject> removeWishService = userService.registerWishList(postLists.get(position).postId);

        removeWishService.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jsonObject -> {
                    String message = jsonObject.get("message").getAsString();

                    if(message.equals("delete"))
                        Toast.makeText(context, "위시리스트 에서 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                }, Throwable::printStackTrace);
    }

    public void addWish(final int position) {
        UserService userService = RestClient.createService(UserService.class);
        Observable<JsonObject> addWishService = userService.registerWishList(postLists.get(position).postId);

        addWishService.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jsonObject -> {
                    String message = jsonObject.get("message").getAsString();

                    if(message.equals("add"))
                        Toast.makeText(context, "위시리스트에 추가하셨습니다.", Toast.LENGTH_SHORT).show();
                }, Throwable::printStackTrace);
    }

    @Override
    public int getItemCount() {
        return postLists != null ? postLists.size() : 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage;
        TextView postTitle;
        TextView postUserName;
        ImageView wish;

        public ViewHolder(View itemView) {
            super(itemView);

            postImage = (ImageView) itemView.findViewById(R.id.post_image);
            postTitle = (TextView) itemView.findViewById(R.id.post_title);
            postUserName = (TextView) itemView.findViewById(R.id.post_user_name);
            wish = (ImageView) itemView.findViewById(R.id.wish);
        }
    }

    public void addPost(PostList postList) {
        this.postLists.add(postList);
    }

    public void removeAll() {
        postLists.clear();
    }
}
