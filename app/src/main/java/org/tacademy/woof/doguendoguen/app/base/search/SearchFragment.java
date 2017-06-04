package org.tacademy.woof.doguendoguen.app.base.search;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.dog_type) TextView typeTv;
    @BindView(R.id.dog_gender) TextView genderTv;
    @BindView(R.id.dog_age) TextView ageTv;
    @BindView(R.id.dog_regions) TextView regionTv;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        typeTv.setOnClickListener(new View.OnClickListener() {
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

        genderTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ageTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        regionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        //분양이 시급한 강아지들에 대한 글을 가로로 보여줌.
        RecyclerView dogEmergency = (RecyclerView) view.findViewById(R.id.dog_emergency);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DoguenDoguenApplication.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        dogEmergency.setLayoutManager(linearLayoutManager);
        dogEmergency.setAdapter(new DogEmergencyAdapter(getContext()));


        //분양글 전체리스트 세로로 보여줌
        RecyclerView dogLists = (RecyclerView) view.findViewById(R.id.dog_lists);
        dogLists.setLayoutManager(new LinearLayoutManager(DoguenDoguenApplication.getContext()));
        dogAdapter = new DogListsAdapter();
        dogLists.setAdapter(dogAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PostService postService = RestGenerator.createService(PostService.class);
        Call<List<PostListModel>> postListModel = postService.getPosts(0, null, null, null, null, null);

        postListModel.enqueue(new Callback<List<PostListModel>>() {
            @Override
            public void onResponse(Call<List<PostListModel>> call, Response<List<PostListModel>> response) {
                if(response.isSuccessful()) {
                    List<PostListModel> postLists = response.body();
                    Log.d(TAG, postLists.toString());

                    dogAdapter.addPostList((ArrayList<PostListModel>) postLists);
                    dogAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<PostListModel>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }


    private static class DogEmergencyAdapter extends RecyclerView.Adapter<DogEmergencyAdapter.ViewHolder> {
        Context context;

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
                    context.startActivity(intent);
                }
            });

            return new ViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.postImage.setImageResource(R.drawable.dog_sample);
            holder.postTitle.setText("가정에서 태어난 건강한 웰시코기 친구들");
            holder.postUserName.setText("코기엄마");
        }

        @Override
        public int getItemCount() {
            return 1;
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
    }
}
