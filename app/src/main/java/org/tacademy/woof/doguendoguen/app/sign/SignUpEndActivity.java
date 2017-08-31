package org.tacademy.woof.doguendoguen.app.sign;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.app.base.BaseActivity;
import org.tacademy.woof.doguendoguen.rest.RestClient;
import org.tacademy.woof.doguendoguen.rest.user.UserService;
import org.tacademy.woof.doguendoguen.util.SharedPreferencesUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Tak on 2017. 6. 5..
 */

public class SignUpEndActivity extends BaseActivity {
    private static final String TAG = "SignUpEndActivity";
    public static final MediaType IMAGE_MIME_TYPE = MediaType.parse("image/*");

    //이전 등록 페이지로 부터 받아온 데이터
    private String userImageFileLocation;
    private String userName;
    private String userGender;

    //이번 등록 페이지에서 설정할 값들
    private String userFamilyType = "4인 이상 가구";
    private String userApartType = "아파트";
    private String userRegion = "서울특별시 강동구";
    private String userToken;
    private int isUserPetOwn = 0; //반려동물 유무: 0->없음, 1->있음

    @BindView(R.id.progress_bar) ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_end);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent != null) {
            userImageFileLocation = intent.getExtras().getString("userImage");
            userName = intent.getExtras().getString("userName");
            userGender = intent.getExtras().getString("userGender");
            userToken = intent.getExtras().getString("userToken");
        }
    }

    @BindView(R.id.region_type) TextView regionType;

    @Override
    public void onStart(){
        super.onStart();;

        familyRegion.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    regionType.setText(familyRegion.getText().toString());
                    familyRegion.setVisibility(View.GONE);
                }
                return false;
            }
        });
    }

    @BindView(R.id.size_arrow) ImageView sizeArrow;
    @BindView(R.id.apart_arrow) ImageView apartArrow;
    @BindView(R.id.region_arrow) ImageView regionArrow;
    @BindView(R.id.pet_own_arrow) ImageView petOwnArrow;

    @BindView(R.id.user_family_size_box) LinearLayout familySize;
    @BindView(R.id.user_apart_type_box) LinearLayout familyApart;
    @BindView(R.id.user_region_edit) EditText familyRegion;
    @BindView(R.id.user_pet_own) LinearLayout familyPetOwn;

    boolean sizeFlag = false;
    boolean apartFlag = false;
    boolean apartRegion = false;
    boolean apartPetOwn = false;

    @OnClick({R.id.family_size, R.id.family_apart_type, R.id.family_region, R.id.family_pet_own})
    public void onUserInfoClicked(View view) {
        switch (view.getId()) {
            case R.id.family_size:
                if(sizeFlag == false) {
                    familySize.setVisibility(View.VISIBLE);
                    sizeArrow.setImageResource(R.drawable.down_arrow);
                    sizeFlag = true;
                } else {
                    familySize.setVisibility(View.GONE);
                    sizeArrow.setImageResource(R.drawable.up_arrow);
                    sizeFlag = false;
                }
                break;
            case R.id.family_apart_type:
                if(apartFlag == false) {
                    familyApart.setVisibility(View.VISIBLE);
                    apartArrow.setImageResource(R.drawable.down_arrow);
                    apartFlag = true;
                } else {
                    familyApart.setVisibility(View.GONE);
                    apartArrow.setImageResource(R.drawable.up_arrow);
                    apartFlag = false;
                }
                break;
            case R.id.family_region:
                if(apartRegion == false) {
                    familyRegion.setVisibility(View.VISIBLE);
                    regionArrow.setImageResource(R.drawable.down_arrow);
                    apartRegion = true;
                } else {
                    familyRegion.setVisibility(View.GONE);
                    regionArrow.setImageResource(R.drawable.up_arrow);
                    apartRegion = false;
                }
                break;
            case R.id.family_pet_own:
                if(apartPetOwn == false) {
                    familyPetOwn.setVisibility(View.VISIBLE);
                    petOwnArrow.setImageResource(R.drawable.down_arrow);
                    apartPetOwn = true;
                } else {
                    familyPetOwn.setVisibility(View.GONE);
                    petOwnArrow.setImageResource(R.drawable.up_arrow);
                    apartPetOwn = false;
                }
                break;
        }
    }

    @BindView(R.id.family_one) TextView familyOne;
    @BindView(R.id.family_two) TextView familyTwo;
    @BindView(R.id.family_three) TextView familyThree;
    @BindView(R.id.family_four_over) TextView familyFourOver;

    @BindView(R.id.size) TextView size;

    @OnClick({R.id.family_one, R.id.family_two, R.id.family_three, R.id.family_four_over})
    public void onFamilySizeClicked(View view) {
        switch (view.getId()) {
            case R.id.family_one:
                size.setText("1인 가구");
                familyOne.setTextColor(Color.parseColor("#EDBC64"));
                familyTwo.setTextColor(Color.parseColor("#3E3A39"));
                familyThree.setTextColor(Color.parseColor("#3E3A39"));
                familyFourOver.setTextColor(Color.parseColor("#3E3A39"));
                familySize.setVisibility(View.GONE);
                break;
            case R.id.family_two:
                size.setText("2인 가구");
                familyTwo.setTextColor(Color.parseColor("#EDBC64"));
                familyOne.setTextColor(Color.parseColor("#3E3A39"));
                familyThree.setTextColor(Color.parseColor("#3E3A39"));
                familyFourOver.setTextColor(Color.parseColor("#3E3A39"));
                familySize.setVisibility(View.GONE);
                break;
            case R.id.family_three:
                size.setText("3인 가구");
                familyThree.setTextColor(Color.parseColor("#EDBC64"));
                familyTwo.setTextColor(Color.parseColor("#3E3A39"));
                familyOne.setTextColor(Color.parseColor("#3E3A39"));
                familyFourOver.setTextColor(Color.parseColor("#3E3A39"));
                familySize.setVisibility(View.GONE);
                break;
            case R.id.family_four_over:
                size.setText("4인 이상 가구");
                familyFourOver.setTextColor(Color.parseColor("#EDBC64"));
                familyTwo.setTextColor(Color.parseColor("#3E3A39"));
                familyThree.setTextColor(Color.parseColor("#3E3A39"));
                familyOne.setTextColor(Color.parseColor("#3E3A39"));
                familySize.setVisibility(View.GONE);
                break;
        }
    }

    @BindView(R.id.one_room) TextView oneRoom;
    @BindView(R.id.apart) TextView apart;
    @BindView(R.id.house) TextView house;
    @BindView(R.id.etc) TextView etc;

    @BindView(R.id.apart_type) TextView apartType;

    @OnClick({R.id.one_room, R.id.apart, R.id.house, R.id.etc})
    public void onApartTypeClicked(View view) {
        switch (view.getId()) {
            case R.id.one_room:
                apartType.setText("원룸");
                oneRoom.setTextColor(Color.parseColor("#EDBC64"));
                apart.setTextColor(Color.parseColor("#3E3A39"));
                house.setTextColor(Color.parseColor("#3E3A39"));
                etc.setTextColor(Color.parseColor("#3E3A39"));
                familyApart.setVisibility(View.GONE);
                break;
            case R.id.apart:
                apartType.setText("아파트");
                apart.setTextColor(Color.parseColor("#EDBC64"));
                oneRoom.setTextColor(Color.parseColor("#3E3A39"));
                house.setTextColor(Color.parseColor("#3E3A39"));
                etc.setTextColor(Color.parseColor("#3E3A39"));
                familyApart.setVisibility(View.GONE);
                break;
            case R.id.house:
                house.setText("주택");
                oneRoom.setTextColor(Color.parseColor("#EDBC64"));
                apart.setTextColor(Color.parseColor("#3E3A39"));
                apartType.setTextColor(Color.parseColor("#3E3A39"));
                etc.setTextColor(Color.parseColor("#3E3A39"));
                familyApart.setVisibility(View.GONE);
                break;
            case R.id.etc:
                apartType.setText("기타");
                etc.setTextColor(Color.parseColor("#EDBC64"));
                apart.setTextColor(Color.parseColor("#3E3A39"));
                house.setTextColor(Color.parseColor("#3E3A39"));
                oneRoom.setTextColor(Color.parseColor("#3E3A39"));
                familyApart.setVisibility(View.GONE);
                break;
        }
    }



    @BindView(R.id.pet_own) TextView petOwn;
    @BindView(R.id.keep) TextView keep;
    @BindView(R.id.none) TextView none;

    @OnClick({R.id.keep, R.id.none})
    public void onPetOwnClicked(View view) {
        switch (view.getId()) {
            case R.id.keep:
                petOwn.setText("있음");
                keep.setTextColor(Color.parseColor("#EDBC64"));
                none.setTextColor(Color.parseColor("#3E3A39"));
                familyPetOwn.setVisibility(View.GONE);
                break;
            case R.id.none:
                petOwn.setText("없음");
                none.setTextColor(Color.parseColor("#EDBC64"));
                keep.setTextColor(Color.parseColor("#3E3A39"));
                familyPetOwn.setVisibility(View.GONE);
                break;
        }
    }

    @OnClick(R.id.regist_finish)
    public void onRegistFinsihClicked() {
        registerUserProfile();
    }

    @BindView(R.id.auth_agree_checkbox) CheckBox userAgreeCheck;
    @BindView(R.id.user_info_agree_checkbox) CheckBox userInfoCheck;
    private void registerUserProfile() {
        boolean isAgreeChecked1 = userAgreeCheck.isChecked();
        boolean isAgreeChecked2 = userInfoCheck.isChecked();
        if(!isAgreeChecked1)
            Toast.makeText(this, "회원 약관 및 이용 약관 동의를 체크해 주세요.", Toast.LENGTH_SHORT).show();
        else if(!isAgreeChecked2)
            Toast.makeText(this, "개인 정보 취급 방침 동의를 체크해 주세요.", Toast.LENGTH_SHORT).show();
        else {
            progressBar.setVisibility(View.VISIBLE);
            //파일 업로드(1개)
            File uploadUserImage = new File(userImageFileLocation);
            RequestBody userImageRequestFile = RequestBody.create(IMAGE_MIME_TYPE, uploadUserImage);
            MultipartBody.Part userImage = MultipartBody.Part.createFormData("profile", uploadUserImage.getName(), userImageRequestFile);

            //그 이외 User등록 정보(6개)
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), userName);
            RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), userGender);
            RequestBody familyType = RequestBody.create(MediaType.parse("text/plain"), userFamilyType);
            RequestBody apartType = RequestBody.create(MediaType.parse("text/plain"), userApartType);
            RequestBody region = RequestBody.create(MediaType.parse("text/plain"), userRegion);
            RequestBody petOwn = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(isUserPetOwn));

            UserService userService = RestClient.createService(UserService.class);
            Observable<JsonObject> registerUserService = userService.registerUser(userToken, userImage, name, gender,
                    familyType, apartType, region, petOwn);

            registerUserService.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(jsonObject -> {
                        String message = jsonObject.get("message").getAsString();

                        if(message.equals("success")) {
                            //로그인해서 받아온 user token을 저장
                            SharedPreferencesUtil.getInstance().setUserId(userToken);
                            Log.d(TAG, SharedPreferencesUtil.getInstance().getUserId());

                            Toast.makeText(SignUpEndActivity.this, "회원 가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }, throwable -> {
                        progressBar.setVisibility(View.GONE);
                    });
        }
    }
}














