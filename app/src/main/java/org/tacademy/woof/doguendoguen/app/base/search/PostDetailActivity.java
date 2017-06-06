package org.tacademy.woof.doguendoguen.app.base.search;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.app.base.message.MessageDetailActivity;
import org.tacademy.woof.doguendoguen.model.DogImage;
import org.tacademy.woof.doguendoguen.model.ParentDogImage;
import org.tacademy.woof.doguendoguen.model.PostDetailModel;
import org.tacademy.woof.doguendoguen.rest.RestGenerator;
import org.tacademy.woof.doguendoguen.rest.post.PostService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.media.CamcorderProfile.get;
import static org.tacademy.woof.doguendoguen.rest.RestGenerator.createService;

/**
 * Created by Tak on 2017. 6. 2..
 */

public class PostDetailActivity extends AppCompatActivity {
    private static final String TAG = "PostDetailActivity";

    @BindView(R.id.dog_image) ImageView dogImage;
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
    @BindView(R.id.dog_parent_image) ImageView parentDogImage;
    @BindView(R.id.blood_image) ImageView bloodImage;
    @BindView(R.id.regions) TextView regions;
    @BindView(R.id.post_condition) TextView postCondition;
    @BindView(R.id.dog_type) TextView dogType;
    @BindView(R.id.dog_gender) TextView dogGender;
    @BindView(R.id.dog_age) TextView dogAge;
    @BindView(R.id.dog_region) TextView dogRegion;
    @BindView(R.id.message_btn) Button messageBtn;

    PostDetailModel postDetail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        int postId = intent.getExtras().getInt("postId");

        //게시판 상세페이지 가져오기
        getPostService(postId);

        //대화시작
        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostDetailActivity.this, MessageDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getPostService(int postId) {
        PostService service = RestGenerator.createService(PostService.class);
        Call<PostDetailModel> getPostCall = service.getPost(130);

        getPostCall.enqueue(new Callback<PostDetailModel>() {
            @Override
            public void onResponse(Call<PostDetailModel> call, Response<PostDetailModel> response) {
                if (response.isSuccessful()) {
                    postDetail = response.body();

                    Log.d(TAG, postDetail.toString());

                    //서버로 부터 가져온 값에 따라 게시판 구성
                    setPost();
                }
            }

            @Override
            public void onFailure(Call<PostDetailModel> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void setPost() {
        //펫 사진을 받아옴
        String dogImageUrl = null;
        if(postDetail.dogImage != null) {
            ArrayList<DogImage> dogImages = (ArrayList<DogImage>) postDetail.dogImage;
            dogImageUrl = dogImages.get(0).dogImageUrl;
        }

        //지역 정보
        String region1 = postDetail.region1;
        String region2 = postDetail.region2;
        String region = region1 + " " + region2;

        //펫 크기 정보
        int dogSize = postDetail.dogSize;

        //부모견 사진을 받아옴
        String parentDogImageUrl = null;
        if(postDetail.parentDogImage != null) {
            ArrayList<ParentDogImage> parentDogImages = (ArrayList<ParentDogImage>) postDetail.parentDogImage;
            parentDogImageUrl = parentDogImages.get(0).parentDogImageUrl;
        }

        Glide.with(this)
                .load(dogImageUrl)
                .placeholder(R.drawable.dog_sample)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(dogImage);

        postTitle.setText(postDetail.postTitle);
        userName.setText(postDetail.username);
        dogTypeDetail.setText(postDetail.region1);
        dogGenderDetail.setText(postDetail.dogGender);
        dogAgeDetail.setText(postDetail.dogAge);
        dogRegionDetail.setText(region);
        dogPrice.setText(postDetail.dogPrice);
        postIntro.setText(postDetail.postIntro);
        dogColor.setText(postDetail.dogColor+"");  //??????

        switch (dogSize) {
            case 0:
                dogSmall.setBackgroundColor(Color.parseColor("#EDBC64"));
                break;
            case 1:
                dogMiddle.setBackgroundColor(Color.parseColor("#EDBC64"));
                break;
            case 2:
                dogBig.setBackgroundColor(Color.parseColor("#EDBC64"));
                break;
        }

        if(postDetail.vaccinKennel==1) {
            vaccinKennel.setBackgroundColor(Color.parseColor("#EDBC64"));
            vaccinKennel.setTextColor(Color.WHITE);
        }
        if(postDetail.vaccinCorona==1) {
            vaccinCorona.setBackgroundColor(Color.parseColor("#EDBC64"));
            vaccinCorona.setTextColor(Color.WHITE);
        }
        if(postDetail.vacinnDHPPL==1) {
            vaccinDhppl.setBackgroundColor(Color.parseColor("#EDBC64"));
            vaccinDhppl.setTextColor(Color.WHITE);
        }

        Glide.with(this)
                .load(parentDogImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.dog_sample)
                .into(parentDogImage);

        Glide.with(this)
                .load(postDetail.bloodImageUrl)
                .placeholder(R.drawable.dog_sample)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(bloodImage);

        regions.setText(region);
        postCondition.setText(postDetail.postCondition);
        dogType.setText(postDetail.dogType + " / ");
        dogAge.setText(postDetail.dogAge);
        dogGender.setText(postDetail.dogGender + " / ");
        dogRegion.setText(postDetail.region1);
    }
}
