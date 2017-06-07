package org.tacademy.woof.doguendoguen.app.base.search;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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
import org.tacademy.woof.doguendoguen.adapter.DogListsAdapter;
import org.tacademy.woof.doguendoguen.model.PostListModel;
import org.tacademy.woof.doguendoguen.rest.RestGenerator;
import org.tacademy.woof.doguendoguen.rest.post.PostService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";

    @BindView(R.id.dog_type) TextView dogType;
    @BindView(R.id.dog_gender) TextView dogGender;
    @BindView(R.id.dog_age) TextView dogAge;
    @BindView(R.id.dog_regions) TextView dogRegion;
    @BindView(R.id.dog_lists) RecyclerView dogListsView;
    @BindView(R.id.dog_emergency) RecyclerView dogEmergencyView;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout refreshLayout;

    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }
    }

    DogListsAdapter dogAdapter;
    DogEmergencyAdapter dogEmergencyAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        dogType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, SearchDogTypeFragment.newInstance());
                fragmentTransaction.commit();

//                if (drawer.isDrawerOpen(GravityCompat.END)) {
//                    drawer.closeDrawer(GravityCompat.END);
//                } else {
//                    drawer.openDrawer(GravityCompat.END);
//                }
            }
        });

        dogGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dogAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dogRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        //분양이 시급한 강아지들에 대한 글을 가로로 보여줌.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DoguenDoguenApplication.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        dogEmergencyAdapter = new DogEmergencyAdapter(getContext());

        dogEmergencyView.setLayoutManager(linearLayoutManager);
        dogEmergencyView.setAdapter(dogEmergencyAdapter);


        //분양글 전체리스트 세로로 보여줌
        dogAdapter = new DogListsAdapter();

        dogListsView.setLayoutManager(new LinearLayoutManager(DoguenDoguenApplication.getContext()));
        dogListsView.setAdapter(dogAdapter);


        //10개씩 데이터를 더 받아옴
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getPostService();
                    }
                }, 2000);
            }
        });
        refreshLayout.setColorSchemeColors(Color.YELLOW, Color.RED, Color.GREEN);

        return view;
    }

    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPostService();
        getUrgentPostService();
    }

    private void getUrgentPostService() {
        PostService postService = RestGenerator.createService(PostService.class);
        Call<List<PostListModel>> getPostListCall = postService.getUrgentPosts();

        getPostListCall.enqueue(new Callback<List<PostListModel>>() {
            @Override
            public void onResponse(Call<List<PostListModel>> call, Response<List<PostListModel>> response) {
                if(response.isSuccessful()) {
                    List<PostListModel> urgentPostLists = response.body();
                    Log.d(TAG, urgentPostLists.toString());

                    dogEmergencyAdapter.addPost(urgentPostLists);
                    dogEmergencyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<PostListModel>> call, Throwable t) {

            }
        });
    }

    private void getPostService() {
        PostService postService = RestGenerator.createService(PostService.class);
        Call<List<PostListModel>> getPostListCall = postService.getPosts(0, "", "", "", "", "");

        getPostListCall.enqueue(new Callback<List<PostListModel>>() {
            @Override
            public void onResponse(Call<List<PostListModel>> call, Response<List<PostListModel>> response) {
                if(response.isSuccessful()) {
                    List<PostListModel> postLists = response.body();
                    Log.d(TAG, postLists.toString());

                    dogAdapter.addPost((ArrayList<PostListModel>) postLists);
                    dogAdapter.notifyDataSetChanged();

                    refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<PostListModel>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }


    // 분양이 시급한 분양견들을 받아옴.
    private static class DogEmergencyAdapter extends RecyclerView.Adapter<DogEmergencyAdapter.ViewHolder> {
        private Context context;
        private ArrayList<PostListModel> urgentPostLists;
        private PostListModel postList;

        public DogEmergencyAdapter(Context context) {
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dog_emergency_item, parent, false);

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DoguenDoguenApplication.getContext(), PostDetailActivity.class);
                    intent.putExtra("postId", postList.postId);
                    context.startActivity(intent);
                }
            });

            return new ViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            postList = urgentPostLists.get(position);

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
            return urgentPostLists != null ? urgentPostLists.size() : 0;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder{
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

        public void addPost(List<PostListModel> urgentPostLists) {
            this.urgentPostLists = (ArrayList<PostListModel>) urgentPostLists;
        }
    }
}
