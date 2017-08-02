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
import com.google.gson.JsonObject;

import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.app.base.search.PostDetailActivity;
import org.tacademy.woof.doguendoguen.app.sign.LoginFragment;
import org.tacademy.woof.doguendoguen.model.PostList;
import org.tacademy.woof.doguendoguen.rest.RestClient;
import org.tacademy.woof.doguendoguen.rest.user.UserService;
import org.tacademy.woof.doguendoguen.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

public class WishFragment extends Fragment {
    private static final String TAG = "WishFragment";
    private final int REQUEST_POST_DETAIL = 1000;

    public WishFragment() {
    }

    public static WishFragment newInstance() {
        WishFragment fragment = new WishFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        userId = SharedPreferencesUtil.getInstance().getUserId();
        Log.d(this.getClass().getSimpleName(), "onCreateView" + ", userId: " + userId);
    }

    private String userId;
    private WishListAdapter wishAdapter;
    private List<PostList> postList;

    @BindView(R.id.wish_recyclerview) RecyclerView wishLists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wish, container, false);
        ButterKnife.bind(this, view);

        //로그인 되어있지 않으면
        if(userId == null) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.container, LoginFragment.newInstance());
            ft.commit();
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");

        wishLists.setLayoutManager(new LinearLayoutManager(DoguenDoguenApplication.getContext()));
        wishAdapter = new WishListAdapter(getContext(), userId);
        wishLists.setAdapter(wishAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(userId != null) {
            UserService userService = RestClient.createService(UserService.class);
            Observable<List<PostList>> postListService = userService.getWishList();

            postListService.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(postLists -> {
                        postList = postLists;

                        for(int i=0; i<postList.size(); i++)
                            wishAdapter.addWishPost(postList.get(i));

                        wishAdapter.notifyDataSetChanged();
                    }, Throwable::printStackTrace);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUEST_POST_DETAIL) {
            Log.d("WishFragment", "isDeleted");

            int isDeleted = data.getIntExtra("isDeleted", 0);
            int deletePos = data.getIntExtra("position", -1);

            if(isDeleted == 1 && deletePos != -1) {
                wishAdapter.removeWishPost(deletePos);
            }
        }
    }

    private class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ListViewHolder> {
        private Context context;
        private String userId;
        private ArrayList<PostList> wishLists = new ArrayList<>();

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
            final PostList post = wishLists.get(position);

            if(post != null ){
                Glide.with(DoguenDoguenApplication.getContext())
                        .load(post.petImageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
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
                        intent.putExtra("position", position);
                        intent.putExtra("isWish", post.favorite);
                        intent.putExtra("myList", 0);   //WishFragment->0
                        intent.putExtra("postId", post.postId);
                        intent.putExtra("userId", userId);
                        startActivityForResult(intent, REQUEST_POST_DETAIL);
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
            UserService userService = RestClient.createService(UserService.class);
            Observable<JsonObject> registerWishService = userService.registerWishList(wishLists.get(position).postId);

            registerWishService.subscribeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(jsonObject->{
                        String message = jsonObject.get("message").getAsString();

                        if(message.equals("delete")) {
                            Log.d("WishLists", "remove");
                            wishLists.remove(position);
                            updateDataList();

                            Toast.makeText(context, "위시리스트 에서 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }, Throwable::printStackTrace);
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
        public void removeWishPost(int position) {
            wishLists.remove(position);
            updateDataList();
        }
    }
}
