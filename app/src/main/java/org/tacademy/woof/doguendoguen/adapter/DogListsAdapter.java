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

import org.json.JSONException;
import org.json.JSONObject;
import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.app.base.search.PostDetailActivity;
import org.tacademy.woof.doguendoguen.model.PostList;
import org.tacademy.woof.doguendoguen.rest.RestClient;
import org.tacademy.woof.doguendoguen.rest.user.UserService;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tak on 2017. 5. 29..
 */

public class DogListsAdapter extends RecyclerView.Adapter<DogListsAdapter.ViewHolder>{
    boolean isWishSelected = false;
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
        Call<ResponseBody> removeWishService = userService.registerWishList(postLists.get(position).postId, Integer.parseInt(userId));
        removeWishService.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject jsonObject ;
                String message ;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    message = jsonObject.getString("message");

                    Log.d("WishLists", response.body().string() +", " + message );
                    if(message.equals("delete")) {
                        Log.d("DogLists", "remove");

                        Toast.makeText(context, "위시리스트 에서 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void addWish(final int position) {
        UserService userService = RestClient.createService(UserService.class);
        Call<ResponseBody> addWishService = userService.registerWishList(postLists.get(position).postId, Integer.parseInt(userId));
        addWishService.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject jsonObject ;
                String message ;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    message = jsonObject.getString("message");

                    Log.d("DogLists", response.body().string() +", " + message );
                    if(message.equals("add")) {
                        Log.d("DogLists", "add");
                        Toast.makeText(context, "위시리스트에 추가하셨습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
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
