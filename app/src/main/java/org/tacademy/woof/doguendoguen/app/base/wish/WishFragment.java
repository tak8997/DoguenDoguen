package org.tacademy.woof.doguendoguen.app.base.wish;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
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
import org.tacademy.woof.doguendoguen.app.sign.LoginFragment;
import org.tacademy.woof.doguendoguen.model.PostList;
import org.tacademy.woof.doguendoguen.rest.RestService;
import org.tacademy.woof.doguendoguen.rest.user.UserService;
import org.tacademy.woof.doguendoguen.util.SharedPreferencesUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishFragment extends Fragment {
    private static final String USER_ID = "userId";
    private static final String TAG = "WishFragment";

    public WishFragment() {
    }

    public static WishFragment newInstance() {
        WishFragment fragment = new WishFragment();
        Bundle args = new Bundle();
//        args.getString(USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            userId = getArguments().getString(USER_ID);
        }
    }

    private String userId;
    private WishListAdapter wishAdapter;
    private List<PostList> postList;

    @BindView(R.id.wish_recyclerview) RecyclerView wishLists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wish, container, false);
        ButterKnife.bind(this, view);

        //userId를 기준으로유저 프로필을 가져온다
        userId = SharedPreferencesUtil.getInstance().getUserId();
        Log.d(TAG, "userId: " + userId);

        if(userId == null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, LoginFragment.newInstance());
            fragmentTransaction.commit();
        }

        wishLists.setLayoutManager(new LinearLayoutManager(DoguenDoguenApplication.getContext()));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(userId != null) {
            UserService userService = RestService.createService(UserService.class);
            Call<List<PostList>> postListService = userService.getWishList(Integer.parseInt(userId));
            postListService.enqueue(new Callback<List<PostList>>() {
                @Override
                public void onResponse(Call<List<PostList>> call, Response<List<PostList>> response) {
                    Log.d(TAG, "getWishList");
                    postList = response.body();


                    wishAdapter = new WishListAdapter(getContext(), userId);
                    wishLists.setAdapter(wishAdapter);

                    for(int i=0; i<postList.size(); i++) {
                        wishAdapter.addWishPost(postList.get(i));
                    }
                    wishAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<List<PostList>> call, Throwable t) {
                    Log.d(TAG, t.getMessage());
                }
            });

        }
    }


    private class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ListViewHolder> {
        private Context context;
        private String userId;
        private ArrayList<PostList> wishLists = new ArrayList<>();
        private PostList post;

        public WishListAdapter(Context context, String userId) {
            this.context = context;
            this.userId = userId;
        }

        @Override
        public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dog_post_item, parent, false);

            return new ListViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(final ListViewHolder holder, final int position) {
            post = wishLists.get(position);

            if(post != null ){
                Glide.with(DoguenDoguenApplication.getContext())
                        .load(post.petImageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.dog_sample)
                        .into(holder.postImage);
                holder.postTitle.setText(post.title);
                holder.postUserName.setText(post.username);
                if(post.favorite == 1) {
                    Log.d("wishList", "a");
                    holder.wish.setImageResource(R.drawable.heart_wish_bigger);
                }

                //item click
                holder.postImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("DogListsAdapter", post.postId + ", " + userId);
                        Intent intent = new Intent(DoguenDoguenApplication.getContext(), PostDetailActivity.class);
                        intent.putExtra("isWish", post.favorite);
                        intent.putExtra("postId", post.postId);
                        intent.putExtra("userId", userId);
                        context.startActivity(intent);
                    }
                });
                holder.wish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(post.favorite == 1) {    //제거작업
                            holder.wish.setImageResource(R.drawable.heart);

                            removeWish(position);
                        }
                    }
                });
            }
        }

        private void removeWish(final int position) {
            UserService userService = RestService.createService(UserService.class);
            Call<ResponseBody> registerWishService = userService.registerWishList(wishLists.get(position).postId, Integer.parseInt(userId));
            registerWishService.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    JSONObject jsonObject ;
                    String message ;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        message = jsonObject.getString("message");

                        Log.d("WishLists", response.body().string() +", " + message );
                        if(message.equals("delete")) {
                            Log.d("WishLists", "remove");
                            wishLists.remove(position);
                            updateDataList();

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

        private void updateDataList() {
            this.notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return wishLists != null ? wishLists.size() : 0;
        }

        class ListViewHolder extends RecyclerView.ViewHolder {
            ImageView postImage;
            TextView postTitle;
            TextView postUserName;
            ImageView wish;

            public ListViewHolder(View itemView) {
                super(itemView);

                postImage = (ImageView) itemView.findViewById(R.id.post_image);
                postTitle = (TextView) itemView.findViewById(R.id.post_title);
                postUserName = (TextView) itemView.findViewById(R.id.post_user_name);
                wish = (ImageView) itemView.findViewById(R.id.wish);
            }
        }

        public void addWishPost(PostList postList) {
            wishLists.add(0, postList);
        }
    }
}
