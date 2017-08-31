package org.tacademy.woof.doguendoguen.app.base.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonObject;

import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.adapter.DogListsAdapter;
import org.tacademy.woof.doguendoguen.app.base.SearchDogTypeActivity;
import org.tacademy.woof.doguendoguen.app.rxbus.Events;
import org.tacademy.woof.doguendoguen.app.rxbus.RxEventBus;
import org.tacademy.woof.doguendoguen.model.PostList;
import org.tacademy.woof.doguendoguen.model.PostListModel;
import org.tacademy.woof.doguendoguen.rest.RestClient;
import org.tacademy.woof.doguendoguen.rest.post.PostService;
import org.tacademy.woof.doguendoguen.rest.user.UserService;
import org.tacademy.woof.doguendoguen.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SearchFragment extends Fragment implements NestedScrollView.OnScrollChangeListener, AppBarLayout.OnOffsetChangedListener {
    private static final String TAG = "SearchFragment";
    private static final int SEARCH_DOG_TYPE = 100;
    private static final String USER_ID = "userId";

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
    private String userId;

    private DogListsAdapter dogAdapter;
    private DogEmergencyAdapter dogEmergencyAdapter;

    @BindView(R.id.dog_type) TextView dogType;
    @BindView(R.id.dog_gender) TextView dogGender;
    @BindView(R.id.dog_age) TextView dogAge;
    @BindView(R.id.dog_regions) TextView dogRegion;
    @BindView(R.id.dog_lists) RecyclerView dogListsView;
    @BindView(R.id.dog_emergency) RecyclerView dogEmergencyView;
    @BindView(R.id.emergency_list) LinearLayout emergencyList;

    @BindView(R.id.appbar) AppBarLayout appbar;
    @BindView(R.id.nav_result) TextView navResult;
    @BindView(R.id.toolar_layout) RelativeLayout toolbarLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.collapsing_layout) CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.scroll_view) NestedScrollView nestedScrollView;

    StringBuffer strBuffer;
    HashMap<String, String> hashMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        strBuffer = new StringBuffer();
        hashMap = new HashMap();

        userId = SharedPreferencesUtil.getInstance().getUserId();
        Log.d(TAG, "userId: " + userId);

        appbar.addOnOffsetChangedListener(this);
        nestedScrollView.setOnScrollChangeListener(this);

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
        dogListsView.setNestedScrollingEnabled(false);

        return view;
    }

    private CompositeDisposable disposables = new CompositeDisposable();
    @Override
    public void onStart() {
        super.onStart();

        disposables.add(RxEventBus.getInstance()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object -> {
                    if (object instanceof Events.GenderMsgEvents) {
                        String gender = GenderSearchDialogFragment.genderMsgEvents.getTag();
                        dogGender.setText(gender);
                        conDogGender = gender;

                        if(!hashMap.containsKey("gender")) {
                            hashMap.put("gender", gender);
                            strBuffer.append(hashMap.get("gender") + " / ");
                        }
                        navResult.setText(strBuffer.toString());

                        isCondtion = true;
                        getPostService(isCondtion, curPage, conDogType, conDogGender, conDogRegion1, conDogRegion2, conDogAge);
                    } else if(object instanceof Events.AgeMsgEvents) {
                        String age = AgeSearchDialogFragment.ageMsgEvents.getTag();
                        dogAge.setText(age);
                        conDogAge = age;

                        if(!hashMap.containsKey("age")) {
                            hashMap.put("age", age);
                            strBuffer.append(hashMap.get("age") + " / ");
                        }
                        navResult.setText(strBuffer.toString());

                        isCondtion = true;
                        getPostService(isCondtion, curPage, conDogType, conDogGender, conDogRegion1, conDogRegion2, conDogAge);
                    } else if(object instanceof Events.RegionMsgEvents) {
                        conDogRegion1 = RegionSearchDialogFragment.regionMsgEvents.getCityTag();    //도시
                        conDogRegion2 = RegionSearchDialogFragment.regionMsgEvents.getDistrictTag();    //지역구
                        dogRegion.setText(conDogRegion1 + " " + conDogRegion2);

                        if(!hashMap.containsKey("city") && !hashMap.containsKey("district")) {
                            hashMap.put("city", conDogRegion1);
                            hashMap.put("district", conDogRegion2);
                            strBuffer.append(hashMap.get("city") + " " + hashMap.get("district") + " / ");
                        }
                        navResult.setText(strBuffer.toString());

                        isCondtion = true;
                        getPostService(isCondtion, curPage, conDogType, conDogGender, conDogRegion1, conDogRegion2, conDogAge);
                    } else if(object instanceof Events.TypeMsgEvents) {
                        conDogType = SearchDogTypeActivity.typeMsgEvents.getTag();

                        if(!hashMap.containsKey("type")) {
                            hashMap.put("type", conDogType);
                            strBuffer.append(hashMap.get("type") + " / ");
                        }

                        navResult.setText(strBuffer.toString());

                        isCondtion = true;

                        Log.d("dogType", conDogType  + ", " + isCondtion);
                        dogType.setText(conDogType);
                        getPostService(isCondtion, curPage, conDogType, conDogGender, conDogRegion1, conDogRegion2, conDogAge);//false가 들어감
                    }
                }));

    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        disposables.clear();
    }

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
                break;
            case R.id.dog_age:
                AgeSearchDialogFragment ageSearchDialog = new AgeSearchDialogFragment();
                ageSearchDialog.show(getFragmentManager(), "ageSearch");
                break;
            case R.id.dog_regions:
                RegionSearchDialogFragment regionSearchDialog = new RegionSearchDialogFragment();
                regionSearchDialog.show(getFragmentManager(), "regionSearch");
                break;
        }
    }

    //조건 6개(아무 조건 안줬을때)
    int curPage = 0;
    String conDogType ="0";
    String conDogGender ="0";
    String conDogRegion1 ="0";
    String conDogRegion2 ="0";
    String conDogAge ="0";

    boolean isCondtion;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPostService(isCondtion, curPage, conDogType, conDogGender, conDogRegion1, conDogRegion2, conDogAge); //검색조건 지정했는지 여부 맨앞의 0,1로 구별
        getUrgentPostService();
    }

    private void getUrgentPostService() {
        PostService postService = RestClient.createService(PostService.class);
        Observable<List<PostList>> getPostListCall = postService.getUrgentPosts();

        getPostListCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(postLists -> {
                    List<PostList> urgentPostLists = postLists;
                    Log.d(TAG, urgentPostLists.toString());

                    dogEmergencyAdapter.addPost(urgentPostLists);
                    dogEmergencyAdapter.notifyDataSetChanged();
                }, Throwable::printStackTrace);
    }

    private void getPostService(final boolean isCondition, int page, String dogType, String dogGender, String region1, String region2, String age) {
        PostService postService = RestClient.createService(PostService.class);
        Observable<PostListModel> getPostListCall = postService.getPosts(page, dogType, dogGender, region1, region2, age);

        getPostListCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(postListModel -> {
                    dogAdapter.removeAll();
                    if(postListModel.postLists.size() != 0) {
                        for (int i = 0; i < postListModel.postLists.size(); i++) {
                            Log.d(TAG, "onResponse, postId: " + postListModel.postLists.get(i).postId );
                            dogAdapter.addPost(postListModel.postLists.get(i));
                        }
                    }

                    dogAdapter.notifyDataSetChanged();
                    curPage = postListModel.page;

                    if(isCondition == true) {
                        Log.d("emergencylist", "gone");
                        emergencyList.setVisibility(View.GONE);
                    }
                }, Throwable::printStackTrace);
//                .subscribe(this::handleResponse, this::errorResponse);
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (scrollY > oldScrollY) {
        }
        if (scrollY < oldScrollY) {
        }

        if (scrollY == 0) {
        }

        if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
            getPostService(isCondtion, curPage, conDogType, conDogGender, conDogRegion1, conDogRegion2, conDogAge);
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
            toolbarLayout.setVisibility(View.VISIBLE);
            float percentage = ((float)Math.abs(verticalOffset)/appBarLayout.getTotalScrollRange());
            toolbarLayout.setAlpha(percentage);
        }
        else {
            toolbarLayout.setVisibility(View.GONE);
        }
    }
    @OnClick(R.id.condition_hide)
    public void onUpArrowClicked() {
        appbar.setExpanded(false);
        toolbarLayout.setVisibility(View.VISIBLE);
    }

    // 분양이 시급한 분양견들을 받아옴.
    private class DogEmergencyAdapter extends RecyclerView.Adapter<DogEmergencyAdapter.ViewHolder> {
        private Context context;
        private ArrayList<PostList> urgentPostLists;
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
            final PostList postList = urgentPostLists.get(position);

            if(postList.favorite == 1) {
                Log.d("emergencyWishList", "a");
                holder.postWish.setImageResource(R.drawable.heart_wish_bigger);
            }
            Glide.with(DoguenDoguenApplication.getContext())
                    .load(postList.petImageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.postImage);
            holder.postTitle.setText(postList.title);
            holder.postUserName.setText(postList.username);


            holder.postImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("DogEmergencyPostLists", postList.postId + ", " + userId + " position : " + position);
                    Intent intent = new Intent(DoguenDoguenApplication.getContext(), PostDetailActivity.class);
                    Log.d("emergencyPostList", postList.postId+"");
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

                        Log.d("heart", "wish1");
                        removeWish(position);
                    } else if (postList.favorite == 0) {    //하트가 안찍혔다면 추가
                        holder.postWish.setImageResource(R.drawable.heart_wish_bigger);

                        Log.d("heart", "wish2");
                        addWish(position);
                    }

                }
            });
        }

        private void addWish(int position) {
            UserService userService = RestClient.createService(UserService.class);
            Observable<JsonObject> addWishService = userService.registerWishList(urgentPostLists.get(position).postId);

            addWishService.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(jsonObject -> {
                        String message = jsonObject.get("message").getAsString();

                        Log.d("DogLists", message);
                        if(message.equals("add"))
                            Toast.makeText(context, "위시리스트에 추가하셨습니다.", Toast.LENGTH_SHORT).show();
                    });
        }

        private void removeWish(int position) {
            UserService userService = RestClient.createService(UserService.class);
            Observable<JsonObject> removeWishService = userService.registerWishList(urgentPostLists.get(position).postId);

            removeWishService.observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(jsonObject -> {
                        String message = jsonObject.get("message").getAsString();

                        if(message.equals("delete"))
                            Toast.makeText(context, "위시리스트 에서 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                    });
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
