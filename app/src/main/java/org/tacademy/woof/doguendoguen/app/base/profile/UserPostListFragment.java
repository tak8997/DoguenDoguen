package org.tacademy.woof.doguendoguen.app.base.profile;


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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.adapter.UserPostAdapter;
import org.tacademy.woof.doguendoguen.app.base.search.PostDetailActivity;
import org.tacademy.woof.doguendoguen.model.UserModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static org.tacademy.woof.doguendoguen.adapter.UserPostAdapter.READ_USER_POST;

public class UserPostListFragment extends Fragment {
    private static final String TAG = "UserPostListFragment";
    private static final String USER = "user";

    public UserPostListFragment() {
    }

    public static UserPostListFragment newInstance(UserModel user) {
        UserPostListFragment fragment = new UserPostListFragment();
        Bundle args = new Bundle();
        args.putParcelable(USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable(USER);
        }
    }
    UserModel user;

    MyPostAdapter userPostAdapter;
    @BindView(R.id.recycler_view) RecyclerView myListRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_post_list, container, false);
        ButterKnife.bind(this, view);

        myListRecyclerView.setLayoutManager(new LinearLayoutManager(DoguenDoguenApplication.getContext()));
        userPostAdapter = new MyPostAdapter();
        userPostAdapter.addUserPost(user);
        userPostAdapter.notifyDataSetChanged();
        myListRecyclerView.setAdapter(userPostAdapter);

        return view;
    }

    @OnClick({R.id.add_post_layout})
    public void onAddPostClicked() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.container, PostRegist_25_Fragment.newInstance(null));
        ft.commit();
    }

    private class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.ViewHolder> {
        private UserModel userModel;

        public MyPostAdapter() {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_post_item, null, false);

            return new ViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(MyPostAdapter.ViewHolder holder, final int position) {
            if (userModel.userPostList.size() != 0) {
                String postTitle = userModel.userPostList.get(position).postTitle;
                String postImageUrl = userModel.userPostList.get(position).postImageUrl;

                holder.userPostTitle.setText(postTitle);
                Glide.with(DoguenDoguenApplication.getContext())
                        .load(postImageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.userPostImage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userId = String.valueOf(userModel.userId);
                        Intent intent = new Intent(DoguenDoguenApplication.getContext(), PostDetailActivity.class);
                        intent.putExtra("userId", userId);        //string으로 변환해서 보내줘야함.
                        intent.putExtra("postId", userModel.userPostList.get(position).postId);
                        intent.putExtra("position", position);
                        intent.putExtra("myList", 1);   //내가 쓴 글 myList ->1
                        startActivityForResult(intent, 105);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == 105) {
            if(data != null ) {
                int position = data.getIntExtra("position", 0);
                Log.d("position", "position: " + position );
                userPostAdapter.removeUserPost(position);
                userPostAdapter.notifyDataSetChanged();
            }
        }
    }

    @OnClick(R.id.back)
    public void onBackClicked() {
        getFragmentManager().popBackStack();
    }
}
