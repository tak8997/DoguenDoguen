package org.tacademy.woof.doguendoguen.app.base.profile;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.adapter.UserPostAdapter;
import org.tacademy.woof.doguendoguen.app.base.search.UserDropDialogFragment;
import org.tacademy.woof.doguendoguen.app.home.BaseActivity;
import org.tacademy.woof.doguendoguen.model.UserModel;
import org.tacademy.woof.doguendoguen.rest.RestService;
import org.tacademy.woof.doguendoguen.rest.user.UserService;
import org.tacademy.woof.doguendoguen.util.SharedPreferencesUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.tacademy.woof.doguendoguen.DoguenDoguenApplication.getContext;
import static org.tacademy.woof.doguendoguen.R.id.etc;
import static org.tacademy.woof.doguendoguen.R.id.house;
import static org.tacademy.woof.doguendoguen.R.id.keep;
import static org.tacademy.woof.doguendoguen.R.id.user;

/**
 * Created by Tak on 2017. 5. 31..
 */

public class UserProfileDetailActivity extends BaseActivity {
    private static final String TAG = "UserProfileDetailActivity";
    private static final MediaType IMAGE_MIME_TYPE = MediaType.parse("image/*");
    private static final int PICK_FROM_GALLERY = 100;
    private final int MY_PERMISSION_REQUEST_STORAGE = 100;

    @BindView(R.id.fab) FloatingActionButton fab;

    @BindView(R.id.detail_user_image) ImageView userImage;
//    @BindView(R.id.user_name) EditText name;
    @BindView(R.id.user_gender) TextView gender;
    @BindView(R.id.user_family_size) TextView familySize;
    @BindView(R.id.user_apart) TextView apartType;
    @BindView(R.id.user_region) TextView region;
    @BindView(R.id.user_pet_own) TextView petOwn;
    @BindView(R.id.post_list) TextView postList;
    @BindView(R.id.regist_btn) Button updateBtn;

    @BindView(R.id.user_post_recyclerview) RecyclerView userPostRecyclerView;

    String updatedImage;
    String updatedName;
    String updatedGender;
    String updatedFamilySize;
    String updatedApartType;
    String updatedRegion;
    int updatedPetOwn;

    private File myImageDir; //카메라로 찍은 사진을 저장할 디렉토리
    private String userImagefileLocation; //업로드 전에 가져올 최종적으로 가져올 이미지의 절대주소
    private UserModel user;
    private int userId;
    private UserPostAdapter userPostAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent != null) {
            user = intent.getParcelableExtra("user");
            Log.d(TAG, "userId, userName : " + user.userId + ", " +user.userName);
        }

        userId = user.userId;
        updatedImage = user.userImageUrl;
        updatedName = user.userName;
        updatedGender = user.userGender;
        updatedFamilySize = user.userFamilySize;
        updatedApartType = user.userApartType;
        updatedRegion = user.userRegion;
        updatedPetOwn = user.userPetOwn;

        Log.d("asdffffx", updatedApartType);
        Glide.with(this)
            .load(updatedImage)
            .placeholder(R.drawable.dog_sample)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(userImage);

        editUserName.setText(updatedName);
        gender.setText(updatedGender);
        familySize.setText(updatedFamilySize);
        apartType.setText(updatedApartType);
        region.setText(updatedRegion);
        if(updatedPetOwn == 0)
            petOwn.setText("있음");
        else
            petOwn.setText("없음");

        if(user.userPostList != null && user.userPostList.size() != 0) {
            postList.setText("");
            userPostRecyclerView.setVisibility(View.VISIBLE);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

            userPostAdapter = new UserPostAdapter(this);
            userPostAdapter.addUserPost(user);
            userPostAdapter.notifyDataSetChanged();

            userPostRecyclerView.setLayoutManager(layoutManager);
            userPostRecyclerView.setAdapter(userPostAdapter);
        }

        initDirConfig();

        updateUserName();
        updateUserRegion();
        updateBtn.setEnabled(true);
        userImage.setClickable(false);
        editPhoto.setClickable(false);
    }

    private void initDirConfig() {
        String currentAppPackage = getPackageName();
        myImageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), currentAppPackage);
        checkPermission();

        if (!myImageDir.exists()) {
            if (myImageDir.mkdirs()) {
                Toast.makeText(getApplication(), " 저장할 디렉토리가 생성 됨", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @BindView(R.id.edit_gender) ImageView editGender;
    @BindView(R.id.edit_family_type) ImageView editFamilyType;
    @BindView(R.id.edit_apart_type) ImageView editApartType;
    @BindView(R.id.edit_region) ImageView editRegion;
    @BindView(R.id.edit_pet_own) ImageView editPetOwn;
    @BindView(R.id.edit_photo) ImageView editPhoto;

    boolean editFlag = false;//업데이트 버튼 클릭
    @OnClick(R.id.fab)
    public void onUserEditClicked() {
        editUser.setVisibility(View.VISIBLE);
        editGender.setVisibility(View.VISIBLE);
        editFamilyType.setVisibility(View.VISIBLE);
        editApartType.setVisibility(View.VISIBLE);
        editRegion.setVisibility(View.VISIBLE);
        editPetOwn.setVisibility(View.VISIBLE);
        editPhoto.setVisibility(View.VISIBLE);

        editUserName.setEnabled(true);
        editUserName.setFocusable(true);
        editUserName.setPadding(0, 0, 110, 0);
        gender.setPadding(0, 0, 110, 0);
        familySize.setPadding(0, 0, 110, 0);
        apartType.setPadding(0, 0, 110, 0);
        region.setPadding(0, 0, 110, 0);
        petOwn.setPadding(0, 0, 110, 0);
        postList.setPadding(0, 0, 110, 0);

        fab.setVisibility(View.INVISIBLE);
        updateBtn.setEnabled(true);
        userImage.setClickable(true);
        editPhoto.setClickable(true);

        editFlag = true;    //업데이트 가능
    }

    @OnClick({R.id.detail_user_image, R.id.edit_photo})
    public void onImageClicked() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_GALLERY);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri returedImgURI;

        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == PICK_FROM_GALLERY) {
                returedImgURI =  data.getData();
                if (returedImgURI != null) {
                    userImage.setImageURI(returedImgURI);

                    //업로드 할 수 있도록 절대 주소를 알아낸다.
                    if (findImageFileNameFromUri(returedImgURI)) {
                        Log.e(TAG, " 갤러리에서 절대주소 Pick 성공");
                    }else {
                        Log.e(TAG, " 갤러리에서 절대주소 Pick 실패");
                    }
                } else {
                    Bundle extras = data.getExtras();
                    Bitmap returedBitmap = (Bitmap) extras.get("data");
                    if (tempSavedBitmapFile(returedBitmap)) {
                        Log.e(TAG, "갤러리에서 Uri값이 없어 실제 파일로 저장 성공");
                    } else {
                        Log.e(TAG,"갤러리에서 Uri값이 없어 실제 파일로 저장 실패");
                    }
                }
            } else if(requestCode == UserPostAdapter.READ_USER_POST) {
                int position = data.getExtras().getInt("position");
                Log.d(TAG, "position : " + position);
                userPostAdapter.removeUserPost(position);
                userPostAdapter.notifyDataSetChanged();
            }
        }

    }


    private void updateUserName() {
        editUserName.setCursorVisible(false);
        editUserName.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        editUserName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    String name = editUserName.getText().toString();
                    editUserName.setText(name);
                    updatedName = name;
                    return true;
                }

                return false;
            }
        });
    }


    @BindView(R.id.woman) TextView woman;
    @BindView(R.id.man) TextView man;
    @BindView(R.id.both) TextView both;
    boolean womanFlag = false;
    boolean manFlag = false;
    boolean bothFlag = false;

    @OnClick({R.id.woman, R.id.man, R.id.both})
    public void onGenderClicked(View view) {
        switch (view.getId()) {
            case R.id.woman:
                if(womanFlag == false) {
                    woman.setTextColor(Color.parseColor("#EDBC64"));
                    man.setTextColor(Color.parseColor("#E0E0E0"));
                    both.setTextColor(Color.parseColor("#E0E0E0"));

                    gender.setText("여성");
                    updatedGender = "여성";
                    womanFlag = true;
                } else if(womanFlag == true) {
//                    woman.setTextColor(Color.parseColor("#E0E0E0"));

//                    gender.setText("");
                    womanFlag = false;
                }
                break;
            case R.id.man:
                if(manFlag == false) {
                    man.setTextColor(Color.parseColor("#EDBC64"));
                    woman.setTextColor(Color.parseColor("#E0E0E0"));
                    both.setTextColor(Color.parseColor("#E0E0E0"));

                    gender.setText("남성");
                    updatedGender = "남성";
                    manFlag = true;
                } else if(manFlag == true) {
//                    man.setTextColor(Color.parseColor("#E0E0E0"));

//                    gender.setText("");
                    manFlag = false;
                }
                break;
            case R.id.both:
                if(bothFlag == false) {
                    both.setTextColor(Color.parseColor("#EDBC64"));
                    woman.setTextColor(Color.parseColor("#E0E0E0"));
                    man.setTextColor(Color.parseColor("#E0E0E0"));

                    gender.setText("기타");
                    updatedGender = "기타";
                    bothFlag = true;
                } else if(bothFlag == true) {
//                    both.setTextColor(Color.parseColor("#E0E0E0"));

//                    gender.setText("");
                    bothFlag = false;
                }
                break;
        }
    }


    @BindView(R.id.one) TextView one;
    @BindView(R.id.two) TextView two;
    @BindView(R.id.three) TextView three;
    @BindView(R.id.four_over) TextView fourOver;
    boolean sizeFlag = false;

    @OnClick({R.id.one, R.id.two, R.id.three, R.id.four_over})
    public void onSizeClicked(View view) {
        switch (view.getId()) {
            case R.id.one:
                if(sizeFlag == false) {
                    one.setTextColor(Color.parseColor("#EDBC64"));
                    two.setTextColor(Color.parseColor("#E0E0E0"));
                    three.setTextColor(Color.parseColor("#E0E0E0"));
                    fourOver.setTextColor(Color.parseColor("#E0E0E0"));

                    familySize.setText("1인 가구");
                    updatedFamilySize = "1인 가구";
                    sizeFlag = true;
                } else if(sizeFlag == true) {
                    sizeFlag = false;
                }
                break;
            case R.id.two:
                if(sizeFlag == false) {
                    two.setTextColor(Color.parseColor("#EDBC64"));
                    one.setTextColor(Color.parseColor("#E0E0E0"));
                    three.setTextColor(Color.parseColor("#E0E0E0"));
                    fourOver.setTextColor(Color.parseColor("#E0E0E0"));

                    familySize.setText("2인 가구");
                    updatedFamilySize = "2인 가구";
                    sizeFlag = true;
                } else if(sizeFlag == true) {
//                    two.setTextColor(Color.parseColor("#E0E0E0"));
//                    familySize.setText("");
                    sizeFlag = false;
                }
                break;
            case R.id.three:
                if(sizeFlag == false) {
                    three.setTextColor(Color.parseColor("#EDBC64"));
                    two.setTextColor(Color.parseColor("#E0E0E0"));
                    one.setTextColor(Color.parseColor("#E0E0E0"));
                    fourOver.setTextColor(Color.parseColor("#E0E0E0"));

                    familySize.setText("3인 가구");
                    updatedFamilySize = "3인 가구";
                    sizeFlag = true;
                } else if(sizeFlag == true) {
//                    three.setTextColor(Color.parseColor("#E0E0E0"));

                    sizeFlag = false;
                }
                break;
            case R.id.four_over:
                if(sizeFlag == false) {
                    fourOver.setTextColor(Color.parseColor("#EDBC64"));
                    two.setTextColor(Color.parseColor("#E0E0E0"));
                    three.setTextColor(Color.parseColor("#E0E0E0"));
                    one.setTextColor(Color.parseColor("#E0E0E0"));

                    familySize.setText("4인 가구 이상");
                    updatedFamilySize = "4인 가구 이상";
                    sizeFlag = true;
                } else if(sizeFlag == true) {
//                    fourOver.setTextColor(Color.parseColor("#E0E0E0"));

                    sizeFlag = false;
                }
                break;
        }
    }


    @BindView(R.id.one_room) TextView oneRoom;
    @BindView(R.id.apart) TextView apart;
    @BindView(R.id.house) TextView house;
    @BindView(R.id.etc) TextView etc;
    boolean apartFlag = false;

    @OnClick({R.id.one_room, R.id.apart, R.id.house, R.id.etc})
    public void onApartTypeClicked(View view){
        switch (view.getId()) {
            case R.id.one_room:
                if(apartFlag == false) {
                    oneRoom.setTextColor(Color.parseColor("#EDBC64"));
                    apart.setTextColor(Color.parseColor("#E0E0E0"));
                    house.setTextColor(Color.parseColor("#E0E0E0"));
                    etc.setTextColor(Color.parseColor("#E0E0E0"));

                    apartType.setText("원룸");
                    updatedApartType = "원룸";
                    apartFlag = true;
                } else if(sizeFlag == true) {
//                    oneRoom.setTextColor(Color.parseColor("#E0E0E0"));

                    apartFlag = false;
                }
                break;
            case R.id.apart:
                if(apartFlag == false) {
                    apart.setTextColor(Color.parseColor("#EDBC64"));
                    oneRoom.setTextColor(Color.parseColor("#E0E0E0"));
                    house.setTextColor(Color.parseColor("#E0E0E0"));
                    etc.setTextColor(Color.parseColor("#E0E0E0"));

                    apartType.setText("아파트");
                    updatedApartType = "아파트";
                    apartFlag = true;
                } else if(sizeFlag == true) {
//                    apart.setTextColor(Color.parseColor("#E0E0E0"));

                    apartFlag = false;
                }
                break;
            case R.id.house:
                if(apartFlag == false) {
                    house.setTextColor(Color.parseColor("#EDBC64"));
                    apart.setTextColor(Color.parseColor("#E0E0E0"));
                    oneRoom.setTextColor(Color.parseColor("#E0E0E0"));
                    etc.setTextColor(Color.parseColor("#E0E0E0"));

                    apartType.setText("주택");
                    updatedApartType = "주택";
                    apartFlag = true;
                } else if(sizeFlag == true) {
//                    house.setTextColor(Color.parseColor("#E0E0E0"));

                    apartFlag = false;
                }
                break;
            case R.id.etc:
                if(apartFlag == false) {
                    etc.setTextColor(Color.parseColor("#EDBC64"));
                    apart.setTextColor(Color.parseColor("#E0E0E0"));
                    house.setTextColor(Color.parseColor("#E0E0E0"));
                    oneRoom.setTextColor(Color.parseColor("#E0E0E0"));

                    apartType.setText("기타");
                    updatedApartType = "기타";
                    apartFlag = true;
                } else if(sizeFlag == true) {
//                    etc.setTextColor(Color.parseColor("#E0E0E0"));

                    apartFlag = false;
                }
                break;
        }
    }


    @BindView(R.id.user_region_edit) EditText regionEdit;
    private void updateUserRegion() {
        regionEdit.setCursorVisible(false);
        regionEdit.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        regionEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    String regions = regionEdit.getText().toString();
                    region.setText(regions);
                    updatedRegion = regions;
                    return true;
                }

                return false;
            }
        });
    }

    @BindView(R.id.keep) TextView keep;
    @BindView(R.id.none) TextView none;

    boolean isOwnPetFlag = false;

    @OnClick({R.id.keep, R.id.none})
    public void onPetOwnClicked(View view) {
        switch (view.getId()) {
            case R.id.keep:
                if(isOwnPetFlag == false) {
                    keep.setTextColor(Color.parseColor("#EDBC64"));
                    none.setTextColor(Color.parseColor("#E0E0E0"));

                    petOwn.setText("있음");
                    updatedPetOwn = 1;
                    isOwnPetFlag = true;
                } else if(isOwnPetFlag == true) {
//                    keep.setTextColor(Color.parseColor("#E0E0E0"));

                    isOwnPetFlag = false;
                }
                break;
            case R.id.none:
                if(isOwnPetFlag == false) {
                    none.setTextColor(Color.parseColor("#EDBC64"));
                    keep.setTextColor(Color.parseColor("#E0E0E0"));

                    petOwn.setText("없음");
                    updatedPetOwn = 0;
                    isOwnPetFlag = true;
                } else if(isOwnPetFlag == true) {
//                    none.setTextColor(Color.parseColor("#E0E0E0"));

                    isOwnPetFlag = false;
                }
                break;
        }
    }

    @BindView(R.id.user_gender_layout) LinearLayout genderLayout;
    @BindView(R.id.user_family_size_layout) LinearLayout familySizeLayout;
    @BindView(R.id.user_apart_type_layout) LinearLayout apartTypeLayout;
    @BindView(R.id.pet_own_layout) LinearLayout petOwnLayout;

    boolean genderFlag = false;
    boolean familySizeFlag = false;
    boolean apartTypeFlag = false;
    boolean regionFlag = false;
    boolean ownFlag = false;

    @OnClick({R.id.gender_layout, R.id.size_layout, R.id.apart_type_layout, R.id.region_layout, R.id.is_own_layout})
    public void onEditLayoutClicked(View view) {
        if(editFlag == true) {
            switch (view.getId()) {
                case R.id.gender_layout:
                    if(genderFlag == false) {
                        genderLayout.setVisibility(View.VISIBLE);

                        genderFlag = true;
                    } else {
                        genderLayout.setVisibility(View.GONE);

                        genderFlag = false;
                    }
                    break;
                case R.id.size_layout:
                    if(familySizeFlag == false) {
                        familySizeLayout.setVisibility(View.VISIBLE);

                        familySizeFlag = true;
                    } else {
                        familySizeLayout.setVisibility(View.GONE);

                        familySizeFlag = false;
                    }
                    break;
                case R.id.apart_type_layout:
                    if(apartTypeFlag == false) {
                        apartTypeLayout.setVisibility(View.VISIBLE);

                        apartTypeFlag = true;
                    } else {
                        apartTypeLayout.setVisibility(View.GONE);

                        apartTypeFlag = false;
                    }
                    break;
                case R.id.region_layout:
                    if(regionFlag == false) {
                        regionEdit.setVisibility(View.VISIBLE);

                        regionFlag = true;
                    } else {
                        regionEdit.setVisibility(View.GONE);

                        regionFlag = false;
                    }
                    break;
                case R.id.is_own_layout:
                    if(ownFlag == false) {
                        petOwnLayout.setVisibility(View.VISIBLE);

                        ownFlag = true;
                    } else {
                        petOwnLayout.setVisibility(View.GONE);

                        ownFlag = false;
                    }
                    break;
            }
        }
    }

    @OnClick({R.id.user_drop, R.id.regist_btn})
    public void onRegistClicked(View view) {
        switch (view.getId()) {
            case R.id.user_drop:

                break;
        }
    }

    @BindView(R.id.edit_user) ImageView editUser;
    @BindView(R.id.user_name) EditText editUserName;
    @OnClick(R.id.regist_btn)
    public void onUserUpdateClicked() {
        if(editFlag == true) {
            editUser.setVisibility(View.INVISIBLE);
            editGender.setVisibility(View.INVISIBLE);
            editFamilyType.setVisibility(View.INVISIBLE);
            editApartType.setVisibility(View.INVISIBLE);
            editRegion.setVisibility(View.INVISIBLE);
            editPetOwn.setVisibility(View.INVISIBLE);

            editUser.setVisibility(View.GONE);
            editUserName.setPadding(0, 0, 0, 0);
            gender.setPadding(0, 0, 0, 0);
            familySize.setPadding(0, 0, 0, 0);
            apartType.setPadding(0, 0, 0, 0);
            region.setPadding(0, 0, 0, 0);
            petOwn.setPadding(0, 0, 0, 0);
            postList.setPadding(0, 0, 0, 0);

            fab.setVisibility(View.VISIBLE);

            updateUserProfile();

            editFlag = false;
        }
    }

    private void updateUserProfile() {
        File uploadUserImage = null;
        MultipartBody.Part userImage = null;
        if(userImagefileLocation != null) {
            uploadUserImage = new File(userImagefileLocation);
            RequestBody userImageRequestFile = RequestBody.create(IMAGE_MIME_TYPE, uploadUserImage);
            userImage = MultipartBody.Part.createFormData("profile_image", uploadUserImage.getName(), userImageRequestFile);
        }

        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), updatedName);
        RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), updatedGender);
        RequestBody lifestyle = RequestBody.create(MediaType.parse("text/plain"), updatedFamilySize);
        RequestBody region = RequestBody.create(MediaType.parse("text/plain"), updatedRegion);
        RequestBody petOwn = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(updatedPetOwn));
        RequestBody familySize = RequestBody.create(MediaType.parse("text/plain"), updatedFamilySize);

        UserService userService = RestService.createService(UserService.class);
        Call<ResponseBody> userProfileService
                = userService.updateUser(userId, userImage, username, gender, lifestyle, region, petOwn, familySize);
        userProfileService.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(UserProfileDetailActivity.this, "프로필 수정이 완료되었습니다", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "updateUserProfile" + response.raw().toString());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    private boolean tempSavedBitmapFile(Bitmap tempBitmap) {
        boolean flag = false;
        try {
            String tempName = "upload_" + (System.currentTimeMillis() / 1000);
            String fileSuffix = ".jpg";
            //임시파일을 실행한다.(현재입이 종료되면 스스로 삭제)
            File tempFile = File.createTempFile(
                    tempName,            // prefix
                    fileSuffix,                   // suffix
                    myImageDir                   // directory
            );
            final FileOutputStream bitmapStream = new FileOutputStream(tempFile);
            tempBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bitmapStream);
            if (bitmapStream != null) {
                bitmapStream.close();
            }
            userImagefileLocation = tempFile.getAbsolutePath();
        } catch (IOException i) {
            Log.e("저장중 문제발생", i.toString(), i);
        }
        return flag;
    }
    private boolean findImageFileNameFromUri(Uri tempUri) {// !!!!!
        boolean flag = false;

        //실제 Image Uri의 절대이름, 절대 디렉토리!!
        String[] IMAGE_DB_COLUMN = {MediaStore.Images.ImageColumns.DATA};
        Cursor cursor = null;
        try {
            //Primary Key값을 추출
            String imagePK = String.valueOf(ContentUris.parseId(tempUri));
            //Image DB에 쿼리를 날린다.
            cursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_DB_COLUMN,
                    MediaStore.Images.Media._ID + "=?",
                    new String[]{imagePK}, null, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                userImagefileLocation = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                flag = true;
            }
        } catch (SQLiteException sqle) {
            Log.e("findImage....", sqle.toString(), sqle);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return flag;
    }
    private void checkPermission()
    {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to write the permission.
                    Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_REQUEST_STORAGE);

            } else {
                //사용자가 언제나 허락
            }
        }
    }

    @OnClick(R.id.user_drop)
    public void onUserDropClicked() {
        final UserDropDialogFragment userDropDialog = UserDropDialogFragment.newInstance(userId);
        userDropDialog.show(getSupportFragmentManager(), "userDrop");
        userDropDialog.setOnAdapterItemClickListener(new UserDropDialogFragment.OnAdapterItemClickLIstener() {
            @Override
            public void onAdapterItemClick(String answer) {
                if (answer.equals("yes")) {
                    Toast.makeText(UserProfileDetailActivity.this, "정상적으로 탈퇴되었습니다.", Toast.LENGTH_SHORT).show();
                    UserService userService = RestService.createService(UserService.class);
                    Call<ResponseBody> dropUserService = userService.userDrop(userId);
                    dropUserService.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.isSuccessful()) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body().string());
                                    String message = jsonObject.getString("message");

                                    if(message.equals("delete success")) {
                                        Log.d("UserProfile", "drop user");
                                        Toast.makeText(UserProfileDetailActivity.this, "정상적으로 탈퇴되었습니다.", Toast.LENGTH_SHORT).show();

                                        //로그아웃
                                        SharedPreferencesUtil.getInstance().clear();
                                        Intent intent = new Intent();
                                        setResult(RESULT_OK);
                                        finish();
                                    }
                                        
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                } else {
                    userDropDialog.dismiss();
                }
            }
        });
    }

//    @OnClick(R.id.back)
//    public void onBackClicked() {
//        getFragmentManager().popBackStack();
//    }
}














