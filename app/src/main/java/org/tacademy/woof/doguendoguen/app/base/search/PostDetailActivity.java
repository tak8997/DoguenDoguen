package org.tacademy.woof.doguendoguen.app.base.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;
import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.adapter.DogImageFragmentAdapter;
import org.tacademy.woof.doguendoguen.app.base.message.MessageDetailActivity;
import org.tacademy.woof.doguendoguen.app.base.profile.PostEdit_25_Dialog;
import org.tacademy.woof.doguendoguen.app.base.profile.PostRegist_25_Fragment;
import org.tacademy.woof.doguendoguen.app.home.BaseActivity;
import org.tacademy.woof.doguendoguen.app.sign.LoginFragment;
import org.tacademy.woof.doguendoguen.model.DogImage;
import org.tacademy.woof.doguendoguen.model.ParentDogImage;
import org.tacademy.woof.doguendoguen.model.PostDetailModel;
import org.tacademy.woof.doguendoguen.rest.RestService;
import org.tacademy.woof.doguendoguen.rest.post.PostService;
import org.tacademy.woof.doguendoguen.rest.user.UserService;
import org.tacademy.woof.doguendoguen.util.ConvertPxToDpUtil;
import org.tacademy.woof.doguendoguen.util.SharedPreferencesUtil;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.port;
import static android.view.View.GONE;
import static org.tacademy.woof.doguendoguen.R.id.edit_photo;
import static org.tacademy.woof.doguendoguen.R.id.pager;
import static org.tacademy.woof.doguendoguen.R.id.submenuarrow;

/**
 * Created by Tak on 2017. 6. 2..
 */

public class PostDetailActivity extends BaseActivity implements NestedScrollView.OnScrollChangeListener {
    private static final String TAG = "PostDetailActivity";

    @BindView(R.id.post_title) TextView postTitle;
    @BindView(R.id.user_name) TextView userName;
    @BindView(R.id.dog_type_detail) TextView dogTypeDetail;
    @BindView(R.id.dog_gender_detail) TextView dogGenderDetail;
    @BindView(R.id.dog_age_detail) TextView dogAgeDetail;
    @BindView(R.id.dog_region_detail) TextView dogRegionDetail;
    @BindView(R.id.dog_price) TextView dogPrice;
    @BindView(R.id.post_intro) TextView postIntro;
    @BindView(R.id.dog_color) TextView dogColor;
    @BindView(R.id.dog_small) Button dogSmall;
    @BindView(R.id.dog_middle) Button dogMiddle;
    @BindView(R.id.dog_big) Button dogBig;
    @BindView(R.id.vacinn_dhppl) Button vaccinDhppl;
    @BindView(R.id.vacinn_corona) Button vaccinCorona;
    @BindView(R.id.vacinn_kennel) Button vaccinKennel;
    @BindView(R.id.regions) TextView regions;
    @BindView(R.id.post_condition) TextView postCondition;
    @BindView(R.id.message_btn) Button messageBtn;

    @BindView(R.id.pager) ViewPager pager;
    @BindView(R.id.scroll_view) NestedScrollView scollView;
    @BindView(R.id.message_layout) RelativeLayout messageLayout;
    @BindView(R.id.report) ImageView report;
    @BindView(R.id.heart) ImageView heart;
    @BindView(R.id.color_icon) ImageView colorIcon;

    private PostDetailModel postDetail;
    private DogImageFragmentAdapter dogImageAdapter;
    private int postId;
    private String userId;
    private int position;
    private int myList;
    private int isWish;
    private String context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();    //UserPostAdapter, UserPostListFragment
        if(intent != null) {
            postId = intent.getIntExtra("postId", 0);   //int로 넘어옴
            userId = intent.getStringExtra("userId");   //string으로 넘어옴
            position = intent.getIntExtra("position", 0);
            isWish = intent.getIntExtra("isWish", 0);
            myList = intent.getIntExtra("myList", 0);   //int로 넘어옴. UserPostListFragment에서 myList->1 내글
            Log.d("emergencyPostList", "ASDf");
            Log.d(TAG, isWish + ", " + position + " , " + postId + ", " + userId + ", " + myList);

            //게시판 상세페이지 가져오기
            getPostService(postId);
        }

        dogImageAdapter = new DogImageFragmentAdapter(getSupportFragmentManager(), 0);
        pager.setAdapter(dogImageAdapter);

        scollView.setOnScrollChangeListener(this);
    }

    @OnClick({R.id.message_btn})
    public void onStartMessageClicked(View view) {
        Toast.makeText(this, "추후구현 예정입니다.", Toast.LENGTH_SHORT).show();
//        switch (view.getId()){
//            //대화시작
//            case R.id.message_btn:
//                Intent intent = new Intent(PostDetailActivity.this, MessageDetailActivity.class);
//                intent.putExtra("root", "PostDetailActivity");
//                intent.putExtra("userId", postDetail.userId);       //상대방 유저아이디, Int
//                startActivity(intent);
//                break;
//        }
    }

    boolean reportFlag = false;
    boolean heartFlag = false;

    @OnClick({R.id.report, R.id.heart})
    public void onToolbarClicked(View view){
        switch (view.getId()) {
            case R.id.report:
                if(reportFlag == false)
                    floatDialog();
                else {
                    report.setImageResource(R.drawable.toolbar_report);
                    reportFlag = false;
                }
                break;
            case R.id.heart:
                if(userId == null) {
                    LoginFragment loginFragment = new LoginFragment();
                    loginFragment.show(getSupportFragmentManager(), "loginFragment");
                } else {
                    if (isWish == 0) {  //위시에 추가가 안되어있는 상태
                        heart.setImageResource(R.drawable.toolbar_heart_selected);  //위시추가

                        UserService userService = RestService.createService(UserService.class);
                        Call<ResponseBody> addWishService = userService.registerWishList(postId, Integer.parseInt(userId));
                        addWishService.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                JSONObject jsonObject ;
                                String message ;
                                try {
                                    jsonObject = new JSONObject(response.body().string());
                                    message = jsonObject.getString("Message");

                                    Log.d("PostDetail", "Message : " + message );
                                    if(message.equals("add")) {
                                        Log.d("DogLists", "add");
                                        Toast.makeText(PostDetailActivity.this, "위시리스트에 추가하셨습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Toast.makeText(PostDetailActivity.this, "위시리스트에 추가하셨습니다", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    } else if(isWish == 1) {    //이미 위시에 추가되있는 상태
                        heart.setImageResource(R.drawable.toolbar_heart);   //위세 제거

                        UserService userService = RestService.createService(UserService.class);
                        Call<ResponseBody> removeWishService = userService.registerWishList(postId, Integer.parseInt(userId));
                        removeWishService.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                JSONObject jsonObject;
                                String message;
                                try {
                                    jsonObject = new JSONObject(response.body().string());
                                    message = jsonObject.getString("Message");

                                    Log.d("PostDetail", "Message : " + message);
                                    if (message.equals("remove")) {
                                        Log.d("DogLists", "remove");
                                        Toast.makeText(PostDetailActivity.this, "위시리스트에 추가하셨습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Toast.makeText(PostDetailActivity.this, "위시리스트에 추가하셨습니다", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }

                }
                break;
        }
    }

    private void floatDialog() {
        report.setImageResource(R.drawable.toolbar_report_selected);
        PostReportDialogFragment postReportDialogFragment = new PostReportDialogFragment();
        postReportDialogFragment.show(getSupportFragmentManager(), "postReport");
        postReportDialogFragment.setOnAdapterItemClickListener(new PostReportDialogFragment.OnAdapterItemClickLIstener() {
            @Override
            public void onAdapterItemClick(String answer) {
                if(answer.equals("yes")) {
                    ReportReasonDialogFragment reportReasonDialogFragment = new ReportReasonDialogFragment();
                    reportReasonDialogFragment.show(getSupportFragmentManager(), "reportReason");
                    reportReasonDialogFragment.setOnAdapterItemClickListener(new ReportReasonDialogFragment.OnAdapterItemClickLIstener() {
                        @Override
                        public void onAdapterItemClick(String answer) {
                            if (answer.equals("yes"))
                                Toast.makeText(PostDetailActivity.this, "해당글이 신고되었습니다.", Toast.LENGTH_SHORT).show();
                            else
                                report.setImageResource(R.drawable.toolbar_report);
                        }
                    });
                } else {
                    report.setImageResource(R.drawable.toolbar_report);
                }
            }
        });

        reportFlag = true;
    }

    private void getPostService(int postId) {
        PostService service = RestService.createService(PostService.class);
        Call<PostDetailModel> getPostCall = service.getPost(postId);

        getPostCall.enqueue(new Callback<PostDetailModel>() {
            @Override
            public void onResponse(Call<PostDetailModel> call, Response<PostDetailModel> response) {
                if (response.isSuccessful()) {
                    postDetail = response.body();

                    Log.d(TAG, "onResponse" +postDetail.dogSize + ", " + postDetail.toString());
                    Log.d("emergencyPostList", "onResponse" +postDetail.postId);

                    //서버로 부터 가져온 값에 따라 게시판 구성
                    if(postDetail != null)
                        setPost();
                }
            }

            @Override
            public void onFailure(Call<PostDetailModel> call, Throwable t) {
                Log.e(TAG, "onFailure" + t.getMessage());
            }
        });
    }

    @BindView(R.id.dog_parent_image) RelativeLayout parentDogImage;
    @BindView(R.id.blood_image) RelativeLayout bloodImage;

    @BindView(R.id.parent_dog_image) ImageView parentImage;
    @BindView(R.id.blood_doc_image) ImageView bloodDocImage;

    boolean isParentImageShow = false;
    boolean isBloodDocImageShow = false;

    @OnClick({R.id.dog_parent_image, R.id.blood_image})
    public void onImageLayoutClicked(View view) {
        switch (view.getId()) {
            case R.id.dog_parent_image:
                if(isParentImageShow == false) {
                    parentImage.setVisibility(View.VISIBLE);
                    isParentImageShow = true;
                } else {
                    parentImage.setVisibility(View.GONE);
                    isParentImageShow = false;
                }
                break;
            case R.id.blood_doc_image:
                if(isBloodDocImageShow == false) {
                    bloodDocImage.setVisibility(View.VISIBLE);
                    isBloodDocImageShow = true;
                } else {
                    bloodDocImage.setVisibility(View.GONE);
                    isBloodDocImageShow = false;
                }
                break;
        }
    }

    private void setPost() {
        if(isWish == 1)
            heart.setImageResource(R.drawable.heart_wish_bigger);

        //펫 사진을 받아옴
//        String dogImageUrl = null;
        if(postDetail.dogImage.size() != 0) {
            ArrayList<DogImage> dogImages = (ArrayList<DogImage>) postDetail.dogImage;
            //null일 수 없음
//            if (dogImages != null) {
//                dogImageUrl = dogImages.get(0).dogImageUrl;
            Log.d(TAG, "dogImageNotNull" + dogImages.get(0).toString());
            dogImageAdapter.setDogImages(dogImages);
            dogImageAdapter.notifyDataSetChanged();
//            }
        }

        //지역 정보
        String region1 = postDetail.region1;
        String region2 = postDetail.region2;
        String region = region1 + " " + region2;

        //펫 크기 정보
        String dogSize = postDetail.dogSize;

        //부모견 사진을 받아옴
        String parentDogImageUrl = null;
        if(postDetail.parentDogImage.size() != 0) {
            ArrayList<ParentDogImage> parentDogImages = (ArrayList<ParentDogImage>) postDetail.parentDogImage;
            parentDogImageUrl = parentDogImages.get(0).parentDogImageUrl;
        }

        postTitle.setText(postDetail.postTitle);
        userName.setText(postDetail.username);
        dogTypeDetail.setText(postDetail.dogType);
        dogGenderDetail.setText(postDetail.dogGender);
        dogAgeDetail.setText(postDetail.dogAge);
        dogRegionDetail.setText(region);
        dogPrice.setText(postDetail.dogPrice);
        postIntro.setText(postDetail.postIntro);
        dogColor.setText(postDetail.dogColor);  //??????

        if(postDetail.dogColor != null) {
            switch (postDetail.dogColor) {
                case "흰색":
                    colorIcon.setImageResource(R.drawable.white);
                    break;
                case "아이보리":
                    colorIcon.setImageResource(R.drawable.ivory);
                    break;
                case "밝은 갈색":
                    colorIcon.setImageResource(R.drawable.bright_brown);
                    break;
                case "어두운 갈색":
                    colorIcon.setImageResource(R.drawable.dark_brown);
                    break;
                case "회색":
                    colorIcon.setImageResource(R.drawable.grey);
                    break;
                case "검정색":
                    colorIcon.setImageResource(R.drawable.black);
                    break;
                case "얼룩무늬":
                    colorIcon.setImageResource(R.drawable.spot);
                    break;
                case "기타":
                    colorIcon.setImageResource(R.drawable.etc);
                    break;
            }
        }


        switch (dogSize) {
            case "소":
                dogSmall.setBackgroundResource(R.drawable.btn_selected);
                dogSmall.setTextColor(Color.WHITE);
                break;
            case "중":
                dogMiddle.setBackgroundResource(R.drawable.btn_selected);
                dogMiddle.setTextColor(Color.WHITE);
                break;
            case "대":
                dogBig.setBackgroundResource(R.drawable.btn_selected);
                dogBig.setTextColor(Color.WHITE);
                break;
        }

        if(postDetail.vaccinKennel==1) {
            vaccinKennel.setBackgroundResource(R.drawable.btn_selected);
            vaccinKennel.setTextColor(Color.WHITE);
        }
        if(postDetail.vaccinCorona==1) {
            vaccinCorona.setBackgroundResource(R.drawable.btn_selected);
            vaccinCorona.setTextColor(Color.WHITE);
        }
        if(postDetail.vacinnDHPPL==1) {
            vaccinDhppl.setBackgroundResource(R.drawable.btn_selected);
            vaccinDhppl.setTextColor(Color.WHITE);
        }

        Activity activity = PostDetailActivity.this;
        if (activity.isFinishing())
            return;
        Glide.with(this)
                .load(parentDogImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(parentImage);

        Glide.with(this)
                .load(postDetail.bloodImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(bloodDocImage);

        regions.setText(region);
        postCondition.setText(postDetail.postCondition);


        //본인의 글을 확인하는 것인지 구별.
        String uId = SharedPreferencesUtil.getInstance().getUserId();
        Log.d(TAG, "userId: " + userId +", " + SharedPreferencesUtil.getInstance().getUserId());
        if(myList == 1) {   //내글
            report.setVisibility(View.GONE);
            heart.setVisibility(View.GONE);
            overflowMenu.setVisibility(View.VISIBLE);
            overflowMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isSelected == false) {
                        View view = getLayoutInflater().inflate(R.layout.edit_post_layout, null);
                        view.setBackgroundColor(Color.WHITE);
//                    view.setPadding();

                        editPost = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        editPost.showAtLocation(view, Gravity.TOP, 450, (int) ConvertPxToDpUtil.convertDpToPixel(75, PostDetailActivity.this));
                        editPost.setOutsideTouchable(true);
                        editPost.showAsDropDown(view);

                        view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editPost.dismiss();
                                final PostDeleteDialogFragment postDeleteDialog = new PostDeleteDialogFragment();
                                postDeleteDialog.setOnAdapterItemClickListener(new PostDeleteDialogFragment.OnAdapterItemClickLIstener() {
                                    @Override
                                    public void onAdapterItemClick(String answer) {
                                        if(answer.equals("yes")) {
                                            PostService postService = RestService.createService(PostService.class);
                                            Call<ResponseBody> deletePostService = postService.deletePost(postDetail.postId);
                                            deletePostService.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    JSONObject jsonObject = null;
                                                    String message ;

                                                    if(response.isSuccessful()) {
                                                        try {
                                                            jsonObject = new JSONObject(response.body().string());
                                                            message = jsonObject.getString("message");

                                                            if (message.equals("save")) {
                                                                Toast.makeText(PostDetailActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();

                                                                Intent intent = new Intent();
                                                                intent.putExtra("position", position);
                                                                setResult(RESULT_OK, intent);
                                                                finish();
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                }
                                            });
                                        } else {    //no
                                            getSupportFragmentManager().beginTransaction().remove(postDeleteDialog).commit();
                                        }
                                    }
                                });
                                postDeleteDialog.show(getSupportFragmentManager(), "deleteDialog");
                            }
                        });
                        view.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PostEdit_25_Dialog postEditFragmentDialog = PostEdit_25_Dialog.newInstance(postDetail);
                                postEditFragmentDialog.setOnAdapterItemClickListener(new PostEdit_25_Dialog.OnAdapterItemClickLIstener() {
                                    @Override
                                    public void onAdapterItemClick(String answer) {
                                        finish();
                                    }
                                });
                                postEditFragmentDialog.show(getSupportFragmentManager(), "post25Edit");
                            }
                        });

                        isSelected = true;
                    } else {
                        editPost.dismiss();
                        isSelected = false;
                    }
                }
            });

        }
    }
    boolean isSelected = false;
    PopupWindow editPost;
    @BindView(R.id.overflow_dots) ImageView overflowMenu;

    @Override
    public void onPause() {
        super.onPause();
        if(editPost != null)
            editPost.dismiss();

        Glide.clear(parentImage);
        Glide.clear(bloodDocImage);
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (scrollY > oldScrollY) {
            Log.i(TAG, "Scroll DOWN");

            messageLayout.setVisibility(GONE);
        }
        if (scrollY < oldScrollY) {
            Log.i(TAG, "Scroll UP");

            messageLayout.setVisibility(View.VISIBLE);
        }

        if (scrollY == 0) {
            Log.i(TAG, "TOP SCROLL");
        }

        if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
            Log.i(TAG, "BOTTOM SCROLL");
        }
    }
}
