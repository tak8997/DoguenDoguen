package org.tacademy.woof.doguendoguen.app.base.search;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;
import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.adapter.DogListsAdapter;
import org.tacademy.woof.doguendoguen.app.base.SearchDogTypeActivity;
import org.tacademy.woof.doguendoguen.model.PostList;
import org.tacademy.woof.doguendoguen.model.PostListModel;
import org.tacademy.woof.doguendoguen.rest.RestService;
import org.tacademy.woof.doguendoguen.rest.post.PostService;
import org.tacademy.woof.doguendoguen.rest.user.UserService;
import org.tacademy.woof.doguendoguen.util.SharedPreferencesUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static org.tacademy.woof.doguendoguen.R.id.toolbar;

public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";
    private static final int SEARCH_DOG_TYPE = 100;
    private static final String USER_ID = "userId";

    public SearchFragment() {
        this.setHasOptionsMenu(true);
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
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
    String userId;

    DogListsAdapter dogAdapter;
    DogEmergencyAdapter dogEmergencyAdapter;

    @BindView(R.id.dog_type) TextView dogType;
    @BindView(R.id.dog_gender) TextView dogGender;
    @BindView(R.id.dog_age) TextView dogAge;
    @BindView(R.id.dog_regions) TextView dogRegion;
    @BindView(R.id.dog_lists) RecyclerView dogListsView;
    @BindView(R.id.dog_emergency) RecyclerView dogEmergencyView;
    @BindView(R.id.emergency_list) LinearLayout emergencyList;
//    @BindView(R.id.refresh_layout) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.toolbar) Toolbar navToolbar;
    @BindView(R.id.nav_result) TextView navResult;

    StringBuffer strBuffer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);


        strBuffer = new StringBuffer();

        userId = SharedPreferencesUtil.getInstance().getUserId();
        Log.d(TAG, "userId: " + userId);

        //분양이 시급한 강아지들에 대한 글을 가로로 보여줌.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DoguenDoguenApplication.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        dogEmergencyAdapter = new DogEmergencyAdapter(getContext(), userId);
        dogEmergencyView.setLayoutManager(linearLayoutManager);
        dogEmergencyView.setAdapter(dogEmergencyAdapter);

        //분양글 전체리스트 세로로 보여줌
        dogAdapter = new DogListsAdapter(getContext(), userId);
        final LinearLayoutManager linearManager = new LinearLayoutManager(DoguenDoguenApplication.getContext());
        dogListsView.setLayoutManager(linearManager);
        dogListsView.setAdapter(dogAdapter);
        dogListsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy > 0) //check for scroll down
                {
                    Log.v("Refresh", "scroll down");
                    visibleItemCount = linearManager.getChildCount();
                    totalItemCount = linearManager.getItemCount();
                    pastVisiblesItems = linearManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if (linearManager.findLastCompletelyVisibleItemPosition()==linearManager.getItemCount()-1) {
                            Log.v("Refresh", "Last Item Wow !");

                            getPostService(isCondtion, curPage, conDogType, conDogGender, conDogRegion1, conDogRegion2, conDogAge);
                        }
//                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
//                        {
//                            loading = false;
//                            Log.v("Refresh", "Last Item Wow !");
//                            //Do pagination.. i.e. fetch new data
//                        }
                    }
                }
            }
        });

        return view;
    }
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;

    @OnClick({R.id.dog_type, R.id.dog_gender, R.id.dog_age, R.id.dog_regions})
    public void onCondtionClicked(View view) {
        switch (view.getId()) {
            case R.id.dog_type:
                Intent intent = new Intent(DoguenDoguenApplication.getContext(), SearchDogTypeActivity.class);
                startActivityForResult(intent, SEARCH_DOG_TYPE);
                break;
            case R.id.dog_gender:
                GenderSearchDialogFragment genderSearchDialog = new GenderSearchDialogFragment();
                genderSearchDialog.show(getFragmentManager(), "genderSearch");
                genderSearchDialog.setOnAdapterItemClickListener(new GenderSearchDialogFragment.OnAdapterItemClickLIstener() {
                    @Override
                    public void onAdapterItemClick(String gender) {
                        dogGender.setText(gender);
                        conDogGender = gender;
                        strBuffer.append(gender + " / ");
                        navResult.setText(strBuffer.toString());
                        isCondtion = true;
                        getPostService(isCondtion, curPage, conDogType, conDogGender, conDogRegion1, conDogRegion2, conDogAge);
                    }
                });
                break;
            case R.id.dog_age:
                AgeSearchDialogFragment ageSearchDialog = new AgeSearchDialogFragment();
                ageSearchDialog.show(getFragmentManager(), "ageSearch");
                ageSearchDialog.setOnAdapterItemClickListener(new AgeSearchDialogFragment.OnAdapterItemClickLIstener() {
                    @Override
                    public void onAdapterItemClick(String age) {
                        dogAge.setText(age);
                        conDogAge = age;
                        strBuffer.append(age + " / ");
                        navResult.setText(strBuffer.toString());
                        isCondtion = true;
                        getPostService(isCondtion, curPage, conDogType, conDogGender, conDogRegion1, conDogRegion2, conDogAge);
                    }
                });
                break;
            case R.id.dog_regions:
                RegionSearchDialogFragment regionSearchDialog = new RegionSearchDialogFragment();
                regionSearchDialog.show(getFragmentManager(), "regionSearch");
                regionSearchDialog.setOnAdapterItemClickListener(new RegionSearchDialogFragment.OnAdapterItemClickLIstener() {
                    @Override
                    public void onAdapterItemClick(String city, String district) {
                        dogRegion.setText(city + " " + district);
                        conDogRegion1 = city;
                        conDogRegion2 = district;
                        strBuffer.append(city + " / " + district +" / ");
                        navResult.setText(strBuffer.toString());
                        isCondtion = true;
                        getPostService(isCondtion, curPage, conDogType, conDogGender, conDogRegion1, conDogRegion2, conDogAge);
                    }
                });
                break;
        }
    }

    Handler handler = new Handler(Looper.getMainLooper());

    //조건 6개(아무 조건 안줬을때)
    int curPage = 0;
    String conDogType ="0";
    String conDogGender ="0";
    String conDogRegion1 ="0";
    String conDogRegion2 ="0";
    String conDogAge ="0";

    boolean isCondtion = false;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPostService(isCondtion, curPage, conDogType, conDogGender, conDogRegion1, conDogRegion2, conDogAge); //검색조건 지정했는지 여부 맨앞의 0,1로 구별
        getUrgentPostService();
    }

    private void getUrgentPostService() {
        PostService postService = RestService.createService(PostService.class);
        Call<List<PostList>> getPostListCall = postService.getUrgentPosts();

        getPostListCall.enqueue(new Callback<List<PostList>>() {
            @Override
            public void onResponse(Call<List<PostList>> call, Response<List<PostList>> response) {
                if(response.isSuccessful()) {
                    List<PostList> urgentPostLists = response.body();
                    Log.d(TAG, urgentPostLists.toString());

                    for(int i=0; i<urgentPostLists.size(); i++) {
                        Log.d("emergencyPostList" , "favorite: " + urgentPostLists.get(i).favorite );
                    }

                    dogEmergencyAdapter.addPost(urgentPostLists);
                    dogEmergencyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<PostList>> call, Throwable t) {

            }
        });
    }

    private void getPostService(final boolean isCondition, int page, String dogType, String dogGender, String region1, String region2, String age) {
        PostService postService = RestService.createService(PostService.class);
        Call<PostListModel> getPostListCall = postService.getPosts(page, dogType, dogGender, region1, region2, age);

        getPostListCall.enqueue(new Callback<PostListModel>() {
            @Override
            public void onResponse(Call<PostListModel> call, Response<PostListModel> response) {
                if(response.isSuccessful()) {
                    PostListModel postListModel = response.body();

                    if(postListModel.postLists.size() != 0) {
                        for (int i=0; i<postListModel.postLists.size(); i++) {
                            Log.d(TAG, "onResponse, postId: " + postListModel.postLists.get(i).postId
                            + " page: " + postListModel.page + "post count : " + postListModel.postCount + ", favorit:" +postListModel.postLists.get(i).favorite);
                            dogAdapter.addPost(postListModel.postLists.get(i));
                        }
                        dogAdapter.notifyDataSetChanged();
                        curPage = postListModel.page;

//                        refreshLayout.setRefreshing(false);

                        if(isCondition == true)
                            emergencyList.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<PostListModel> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == SEARCH_DOG_TYPE) {
            String type = data.getStringExtra(SearchDogTypeActivity.DOGTYPE);
            conDogType = type;
            strBuffer.append(type + " / ");
            navResult.setText(strBuffer.toString());
            isCondtion = true;

            dogType.setText(type);
            getPostService(isCondtion, curPage, conDogType, conDogGender, conDogRegion1, conDogRegion2, conDogAge);
        }
    }


    // 분양이 시급한 분양견들을 받아옴.
    private class DogEmergencyAdapter extends RecyclerView.Adapter<DogEmergencyAdapter.ViewHolder> {
        boolean isWishSelected = false;
        private Context context;
        private ArrayList<PostList> urgentPostLists;
        private PostList postList;
        private String userId;

        public DogEmergencyAdapter(Context context, String userId) {
            this.userId = userId;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dog_emergency_item, parent, false);

            return new ViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            postList = urgentPostLists.get(position);

            Glide.with(DoguenDoguenApplication.getContext())
                    .load(postList.petImageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.dog_sample)
                    .into(holder.postImage);
            holder.postTitle.setText(postList.title);
            holder.postUserName.setText(postList.username);
            if(postList.favorite == 1) {
                Log.d("emergencyWishList", "a");
                holder.postWish.setImageResource(R.drawable.heart_wish_bigger);
            }

            holder.postImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("DogEmergencyPostLists", postList.postId + ", " + userId);
                    Intent intent = new Intent(DoguenDoguenApplication.getContext(), PostDetailActivity.class);
                    intent.putExtra("isWish", postList.favorite);
                    intent.putExtra("postId", postList.postId);//int
                    intent.putExtra("userId", userId);//string
                    startActivity(intent);
                }
            });
            holder.postWish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(postList.favorite == 1 ) {   //하트가 찍혔다면 제거
                        holder.postWish.setImageResource(R.drawable.heart);

                        removeWish(position);
                    } else if (postList.favorite == 0) {    //하트가 안찍혔다면 추가
                        holder.postWish.setImageResource(R.drawable.heart_wish_bigger);

                        addWish(position);
                    }

                }
            });
        }

        private void addWish(int position) {
            UserService userService = RestService.createService(UserService.class);
            Call<ResponseBody> addWishService = userService.registerWishList(urgentPostLists.get(position).postId, Integer.parseInt(userId));
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

        private void removeWish(int position) {
            UserService userService = RestService.createService(UserService.class);
            Call<ResponseBody> removeWishService = userService.registerWishList(urgentPostLists.get(position).postId, Integer.parseInt(userId));
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

        private void updateDataList() {
            this.notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return urgentPostLists != null ? urgentPostLists.size() : 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            ImageView postImage;
            TextView postTitle;
            TextView postUserName;
            ImageView postWish;

            public ViewHolder(View itemView) {
                super(itemView);

                postImage = (ImageView) itemView.findViewById(R.id.post_image);
                postTitle = (TextView) itemView.findViewById(R.id.post_title);
                postUserName = (TextView) itemView.findViewById(R.id.post_user_name);
                postWish = (ImageView) itemView.findViewById(R.id.wish);
            }
        }

        public void addPost(List<PostList> urgentPostLists) {
            this.urgentPostLists = (ArrayList<PostList>) urgentPostLists;
        }
    }
}
