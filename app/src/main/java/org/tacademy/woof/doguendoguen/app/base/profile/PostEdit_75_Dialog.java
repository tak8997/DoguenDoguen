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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.model.PostDetailModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostEdit_75_Dialog extends DialogFragment implements NestedScrollView.OnScrollChangeListener {
    private static final String TAG = "PostRegist_75_Fragment";
    private static final int PARENT_IMAGE_PICK_FROM_GALLERY = 100;
    private static final int HIERARCHY_IMAGE_PICK_FROM_GALLERY = 101;
    private final int MY_PERMISSION_REQUEST_STORAGE = 100;

    private static final String POST_TITLE = "postTitle";
    private static final String DOG_IMAGES_FILE_LOCATION = "dogImagesFileLocation";
    private static final String DOG_TYPE = "dogType";
    private static final String DOG_GENDER = "dogGender";
    private static final String DOG_AGE = "dogAge";
    private static final String DOG_CITY = "dogCity";
    private static final String DOG_DISTRICT = "dogDistrict";
    private static final String DOG_PRICE = "dogPrice";

    public PostEdit_75_Dialog() {
    }
    public static PostEdit_75_Dialog newInstance(int imageId, PostDetailModel postDetail, String postTitle, ArrayList<String> dogImagesFileLocation, String dogType, String dogGender, String dogAge, String dogCity, String dogDistrict, String dogPrice) {
        PostEdit_75_Dialog fragment = new PostEdit_75_Dialog();
        Bundle args = new Bundle();
        args.putInt("imageId", imageId);
        args.putParcelable("PostDetailModel", postDetail);
        args.putString(POST_TITLE, postTitle);
        args.putStringArrayList(DOG_IMAGES_FILE_LOCATION, dogImagesFileLocation);
        args.putString(DOG_TYPE, dogType);
        args.putString(DOG_GENDER, dogGender);
        args.putString(DOG_AGE, dogAge);
        args.putString(DOG_CITY, dogCity);
        args.putString(DOG_DISTRICT, dogDistrict);
        args.putString(DOG_PRICE, dogPrice);
        Log.d("price", dogPrice);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialogTheme);
        if (getArguments() != null) {
            imageId = getArguments().getInt("imageId");
            postDetail = getArguments().getParcelable("PostDetailModel");
            postTitle = getArguments().getString(POST_TITLE);
            dogImagesFileLocation = getArguments().getStringArrayList(DOG_IMAGES_FILE_LOCATION);
            dogType = getArguments().getString(DOG_TYPE);
            dogGender = getArguments().getString(DOG_GENDER);
            dogAge = getArguments().getString(DOG_AGE);
            dogCity = getArguments().getString(DOG_CITY);
            dogDistrict = getArguments().getString(DOG_DISTRICT);
            dogPrice = getArguments().getString(DOG_PRICE);
//            Log.d("dogPrice" ,dogPrice);
        }
    }

    @BindView(R.id.text_white) TextView colorWhite;
    @BindView(R.id.text_ivory) TextView colorIvory;
    @BindView(R.id.text_bright_brown) TextView colorBrightBrown;
    @BindView(R.id.text_dark_brown) TextView colorDarkBrown;
    @BindView(R.id.text_grey) TextView colorGrey;
    @BindView(R.id.text_black) TextView colorBlack;
    @BindView(R.id.text_spot) TextView colorSpot;
    @BindView(R.id.text_etc) TextView colorEtc;

    @BindView(R.id.small) Button sizeSmall;
    @BindView(R.id.middle) Button sizeMiddle;
    @BindView(R.id.big) Button sizeBig;

    @BindView(R.id.dhppl) Button vaccinDhppl;
    @BindView(R.id.corrona) Button vaccinCorrona;
    @BindView(R.id.kennel) Button vaccinKennel;

    @BindView(R.id.prev_btn) RelativeLayout prevBtn;
    @BindView(R.id.next_btn) RelativeLayout nextBtn;

    @BindView(R.id.scroll_view) NestedScrollView scrollView;

    //이전 분양글로 부터 가져온 데이터
    int imageId;
    PostDetailModel postDetail = null;
    String postTitle = " ";
    ArrayList<String> dogImagesFileLocation;
    String dogType = " ";
    String dogGender = " ";
    String dogAge = " ";
    String dogCity = " ";
    String dogDistrict = " ";
    String dogPrice = " ";

    //다음 분양글로 넘길 데이터
    int parentId;
    String color = " ";
    String size = null;
    int dhppl = 0;
    int corrona = 0;
    int kennel = 0;
    ArrayList<String> parentImagesFileLocation = null;
    String bloodHierarchyFileLocation = null;

    Drawable drawable;
    Drawable drawableSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_regist_75_, container, false);
        ButterKnife.bind(this, view);

        initFileConfig();

        //버튼 색깔
        drawable = ContextCompat.getDrawable(getActivity(), R.drawable.button_layout);
        drawableSelected = ContextCompat.getDrawable(getActivity(), R.drawable.button_selected_layout);

        if(postDetail != null)
            editPost();

        scrollView.setOnScrollChangeListener(this);

        return view;
    }

    @BindView(R.id.dog_image_title) TextView addDogImageTitle;
    @BindView(R.id.dog_image_sub1_title) TextView firstSubTitle;
    @BindView(R.id.dog_image_sub2_title) TextView secondSubTitle;
    @BindView(R.id.edit_post_title) TextView editPostTitle;
    @BindView(R.id.doc_title) TextView docTitle;
    @BindView(R.id.doc_sub_title) TextView docSubTitle;
    private void editPost() {
        editPostTitle.setText("분양글 수정하기");
        if(color == null)
            color = " ";
        else
            color = postDetail.dogColor;
        Log.d("asdf", color+ " ," + postDetail.dogColor);
        
        size = postDetail.dogSize;
        switch (size) {
            case "소":
                sizeSmall.setBackground(drawableSelected);
                sizeSmall.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            case "중":
                sizeMiddle.setBackground(drawableSelected);
                sizeMiddle.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            case "대":
                sizeBig.setBackground(drawableSelected);
                sizeBig.setTextColor(Color.parseColor("#FFFFFF"));
                break;
        }

        int dhppl = postDetail.vacinnDHPPL;
        int corrona = postDetail.vaccinCorona;
        int kennel = postDetail.vaccinKennel;

        if(dhppl == 1) {
            vaccinDhppl.setBackground(drawableSelected);
            vaccinDhppl.setTextColor(Color.parseColor("#FFFFFF"));
        }
        if(corrona == 1){
            vaccinCorrona.setBackground(drawableSelected);
            vaccinCorrona.setTextColor(Color.parseColor("#FFFFFF"));
        }
        if(kennel == 1) {
            vaccinKennel.setBackground(drawableSelected);
            vaccinKennel.setTextColor(Color.parseColor("#FFFFFF"));
        }

        addParentImageLayout.setVisibility(View.VISIBLE);
        parentImage.setVisibility(View.VISIBLE);
        addDogImageTitle.setVisibility(View.INVISIBLE);
        firstSubTitle.setVisibility(View.INVISIBLE);
        secondSubTitle.setVisibility(View.INVISIBLE);

        if(postDetail.parentDogImage.size() != 0) {
            String parentImageUrl = postDetail.parentDogImage.get(0).parentDogImageUrl;
            Glide.with(DoguenDoguenApplication.getContext())
                    .load(parentImageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(parentImage);
        }

        hierarchyImage.setVisibility(View.VISIBLE);
        addHierarchyImageLayout.setVisibility(View.VISIBLE);
        docTitle.setVisibility(View.INVISIBLE);
        docSubTitle.setVisibility(View.INVISIBLE);

        String bloodHierarchyImageUrl = postDetail.bloodImageUrl;
        Glide.with(DoguenDoguenApplication.getContext())
                .load(bloodHierarchyImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(hierarchyImage);
    }

    File myImageDir; //카메라로 찍은 사진을 저장할 디렉토리
    private void initFileConfig() {
        String currentAppPackage = getActivity().getPackageName();
        myImageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), currentAppPackage);
        checkPermission();

        if (!myImageDir.exists()) {
            if (myImageDir.mkdirs()) {
                Toast.makeText(DoguenDoguenApplication.getContext(), " 저장할 디렉토리가 생성 됨", Toast.LENGTH_SHORT).show();
            }
        }
    }

    boolean whiteFlag = false;
    boolean ivoryFlag = false;
    boolean brightBrownFlag = false;
    boolean darkBrownFlag = false;
    boolean greyFlag = false;
    boolean blackFlag = false;
    boolean spotFlag = false;
    boolean etcFlag = false;

    @OnClick({R.id.color_white, R.id.color_ivory, R.id.color_bright_brown, R.id.color_dark_brown,
            R.id.color_grey, R.id.color_black, R.id.color_spot, R.id.color_etc})
    public void onColorClicked(View view) {
        switch (view.getId()) {
            case R.id.color_white:
                if(whiteFlag == false) {
                    color = colorWhite.getText().toString();
                    view.setSelected(true);

                    whiteFlag = true;
                } else {
                    color = null;
                    view.setSelected(false);

                    whiteFlag = false;
                }
                break;
            case R.id.color_ivory:
                if(ivoryFlag == false) {
                    color = colorIvory.getText().toString();
                    view.setSelected(true);

                    ivoryFlag = true;
                } else {
                    color = null;
                    view.setSelected(false);

                    ivoryFlag = false;
                }
                break;
            case R.id.color_bright_brown:
                if(brightBrownFlag == false) {
                    color = colorBrightBrown.getText().toString();
                    view.setSelected(true);

                    brightBrownFlag = true;
                } else {
                    color = null;
                    view.setSelected(false);

                    brightBrownFlag = false;
                }
                break;
            case R.id.color_dark_brown:
                if(darkBrownFlag == false) {
                    color = colorDarkBrown.getText().toString();
                    view.setSelected(true);

                    darkBrownFlag = true;
                } else {
                    color = null;
                    view.setSelected(false);

                    darkBrownFlag = false;
                }
                break;
            case R.id.color_grey:
                if (greyFlag == false) {
                    color = colorGrey.getText().toString();
                    view.setSelected(true);

                    greyFlag = true;
                } else {
                    color = null;
                    view.setSelected(false);

                    greyFlag = false;
                }
                break;
            case R.id.color_black:
                if(blackFlag == false) {
                    color = colorBlack.getText().toString();
                    view.setSelected(true);

                    blackFlag = true;
                } else {
                    color = null;
                    view.setSelected(false);

                    blackFlag = false;
                }
                break;
            case R.id.color_spot:
                if(spotFlag == false) {
                    color = colorSpot.getText().toString();
                    view.setSelected(true);

                    spotFlag = true;
                } else {
                    color = null;
                    view.setSelected(false);

                    spotFlag = false;
                }
                break;
            case R.id.color_etc:
                if(etcFlag == false) {
                    color = colorEtc.getText().toString();
                    view.setSelected(true);

                    etcFlag = false;
                } else {
                    color = null;
                    view.setSelected(false);

                    etcFlag = true;
                }
                break;

        }
        Log.d("colorr", color );
    }

    boolean smallFlag = false;
    boolean middleFlag = false;
    boolean bigFlag = false;

    @OnClick({R.id.small, R.id.middle, R.id.big})
    public void onSizeClicked(View view) {
        switch (view.getId()) {
            case R.id.small:
                if(smallFlag == false) {
                    sizeSmall.setBackground(drawableSelected);
                    sizeSmall.setTextColor(Color.parseColor("#FFFFFF"));
                    sizeMiddle.setBackground(drawable);
                    sizeMiddle.setTextColor(Color.parseColor("#3E3A39"));
                    sizeBig.setBackground(drawable);
                    sizeBig.setTextColor(Color.parseColor("#3E3A39"));

                    size = sizeSmall.getText().toString();

                    smallFlag = true;
                } else {
                    sizeSmall.setBackground(drawable);
                    sizeSmall.setTextColor(Color.parseColor("#3E3A39"));

                    size = null;

                    smallFlag = false;
                }
                break;
            case R.id.middle:
                if(middleFlag == false) {
                    sizeMiddle.setBackground(drawableSelected);
                    sizeMiddle.setTextColor(Color.parseColor("#FFFFFF"));
                    sizeSmall.setBackground(drawable);
                    sizeSmall.setTextColor(Color.parseColor("#3E3A39"));
                    sizeBig.setBackground(drawable);
                    sizeBig.setTextColor(Color.parseColor("#3E3A39"));

                    size = sizeMiddle.getText().toString();

                    middleFlag = true;
                } else {
                    sizeMiddle.setBackground(drawable);
                    sizeMiddle.setTextColor(Color.parseColor("#3E3A39"));

                    size = null;

                    middleFlag = false;
                }
                break;
            case R.id.big:
                if(bigFlag == false) {
                    sizeBig.setBackground(drawableSelected);
                    sizeBig.setTextColor(Color.parseColor("#FFFFFF"));
                    sizeMiddle.setBackground(drawable);
                    sizeMiddle.setTextColor(Color.parseColor("#3E3A39"));
                    sizeSmall.setBackground(drawable);
                    sizeSmall.setTextColor(Color.parseColor("#3E3A39"));

                    size = sizeBig.getText().toString();

                    bigFlag = true;
                } else {
                    sizeBig.setBackground(drawable);
                    sizeBig.setTextColor(Color.parseColor("#3E3A39"));

                    size = null;

                    bigFlag = false;
                }
                break;
        }
    }

    boolean dhpplFlag = false;
    boolean coronaFlag = false;
    boolean kennelFlag = false;

    @OnClick({R.id.dhppl, R.id.corrona, R.id.kennel})
    public void onVaccineClicked(View view) {
        switch (view.getId()) {
            case R.id.dhppl:
                if(dhpplFlag == false) {
                    vaccinDhppl.setBackground(drawableSelected);
                    vaccinDhppl.setTextColor(Color.parseColor("#FFFFFF"));
                    vaccinCorrona.setBackground(drawable);
                    vaccinCorrona.setTextColor(Color.parseColor("#3E3A39"));
                    vaccinKennel.setBackground(drawable);
                    vaccinKennel.setTextColor(Color.parseColor("#3E3A39"));

                    dhppl = 1;

                    dhpplFlag = true;
                } else {
                    vaccinDhppl.setBackground(drawable);
                    vaccinDhppl.setTextColor(Color.parseColor("#3E3A39"));

                    dhppl = 0;

                    dhpplFlag = false;
                }

                break;
            case R.id.corrona:
                if(coronaFlag == false) {
                    vaccinCorrona.setBackground(drawableSelected);
                    vaccinCorrona.setTextColor(Color.parseColor("#FFFFFF"));
                    vaccinKennel.setBackground(drawable);
                    vaccinKennel.setTextColor(Color.parseColor("#3E3A39"));
                    vaccinDhppl.setBackground(drawable);
                    vaccinDhppl.setTextColor(Color.parseColor("#3E3A39"));

                    corrona = 1;

                    coronaFlag = true;
                } else {
                    vaccinCorrona.setBackground(drawable);
                    vaccinCorrona.setTextColor(Color.parseColor("#3E3A39"));

                    corrona = 0;

                    coronaFlag = false;
                }

                break;
            case R.id.kennel:
                if(kennelFlag == false) {
                    vaccinKennel.setBackground(drawableSelected);
                    vaccinKennel.setTextColor(Color.parseColor("#FFFFFF"));
                    vaccinCorrona.setBackground(drawable);
                    vaccinCorrona.setTextColor(Color.parseColor("#3E3A39"));
                    vaccinDhppl.setBackground(drawable);
                    vaccinDhppl.setTextColor(Color.parseColor("#3E3A39"));

                    kennel = 1;

                    kennelFlag = true;
                } else {
                    vaccinKennel.setBackground(drawable);
                    vaccinKennel.setTextColor(Color.parseColor("#3E3A39"));

                    kennel = 0;

                    kennelFlag = false;
                }

                break;
        }
    }

    @BindView(R.id.add_parent_image) RelativeLayout addParentImageLayout;
    @BindView(R.id.add_blood_hierarchy) RelativeLayout addHierarchyImageLayout;
    @BindView(R.id.parent_image) ImageView parentImage;
    @BindView(R.id.hierarchy_image) ImageView hierarchyImage;
    boolean isParentImageShow = false;
    boolean isHierarchyImageShow = false;

    @OnClick({R.id.dog_parent_image, R.id.dog_blood_hierarchy})
    public void onImageOrHierarchyClicked(View view) {
        switch (view.getId()) {
            case R.id.dog_parent_image:
                if(isParentImageShow == false) {
                    addParentImageLayout.setVisibility(View.VISIBLE);
                    isParentImageShow = true;
                } else {
                    addParentImageLayout.setVisibility(View.GONE);
                    isParentImageShow = false;
                }
                break;
            case R.id.dog_blood_hierarchy:
                if(isHierarchyImageShow == false) {
                    addHierarchyImageLayout.setVisibility(View.VISIBLE);
                    isHierarchyImageShow = true;
                } else {
                    addHierarchyImageLayout.setVisibility(View.GONE);
                    isHierarchyImageShow = false;
                }
                break;
        }
    }

    @OnClick({R.id.prev_btn, R.id.next_btn})
    public void onRegistrationClicked(View view) {
        switch (view.getId()) {
            case R.id.prev_btn:
                previousRegistPage();
                break;
            case R.id.next_btn:
                nextRegistPage();
                break;
        }
    }
    @OnClick({R.id.add_parent_image, R.id.add_blood_hierarchy})
    public void onImageAddClicked(View view) {
        switch (view.getId()) {
            case R.id.add_parent_image:
                addImageFromGallery(PARENT_IMAGE_PICK_FROM_GALLERY);
                break;
            case R.id.add_blood_hierarchy:
                addImageFromGallery(HIERARCHY_IMAGE_PICK_FROM_GALLERY);
                break;
        }
    }

    private void addImageFromGallery(int imageType) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, imageType);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == PARENT_IMAGE_PICK_FROM_GALLERY && data != null) {
                Log.d(TAG, "parentImage");

                Uri imageUri = data.getData();
                if(imageUri != null) {
                    parentImage.setVisibility(View.VISIBLE);
                    parentImage.setImageURI(imageUri);

                    //업로드 할 수 있도록 절대 주소를 알아낸다.!!!!!!
                    String fileLocation = findImageFileNameFromUri(imageUri);
                    if (fileLocation!=null) {
                        Log.e(TAG, " 갤러리에서 절대주소 Pick 성공");

                        parentImagesFileLocation = new ArrayList<>();
                        parentImagesFileLocation.add(fileLocation);
                        imageId = postDetail.parentDogImage.get(0).imageId;
                    }else {
                        Log.e(TAG, " 갤러리에서 절대주소 Pick 실패");
                    }
                } else {
                    Bundle extras = data.getExtras();
                    Bitmap returedBitmap = (Bitmap) extras.get("data");

                    String fileLoaction = tempSavedBitmapFile(returedBitmap);
                    if (fileLoaction != null) {
                        Log.e(TAG, "갤러리에서 Uri값이 없어 실제 파일로 저장 성공");

                        parentImagesFileLocation = new ArrayList<>();
                        parentImagesFileLocation.add(fileLoaction);
                        imageId = postDetail.parentDogImage.get(0).imageId;
                    } else {
                        Log.e(TAG,"갤러리에서 Uri값이 없어 실제 파일로 저장 실패");
                    }
                }
            } else if(requestCode == HIERARCHY_IMAGE_PICK_FROM_GALLERY && data != null) {
                Log.d(TAG, "bloodHierarchy");

                Uri imageUri = data.getData();
                if(imageUri != null) {
                    hierarchyImage.setVisibility(View.VISIBLE);
                    hierarchyImage.setImageURI(imageUri);

                    //업로드 할 수 있도록 절대 주소를 알아낸다.!!!!!!
                    String fileLocation = findImageFileNameFromUri(imageUri);

                    if (fileLocation!=null) {
                        Log.e(TAG, " 갤러리에서 절대주소 Pick 성공");
                        bloodHierarchyFileLocation = fileLocation;
                    }else {
                        Log.e(TAG, " 갤러리에서 절대주소 Pick 실패");
                    }
                } else {
                    Bundle extras = data.getExtras();
                    Bitmap returedBitmap = (Bitmap) extras.get("data");

                    String fileLoaction = tempSavedBitmapFile(returedBitmap);
                    if (fileLoaction != null) {
                        Log.e(TAG, "갤러리에서 Uri값이 없어 실제 파일로 저장 성공");

                        bloodHierarchyFileLocation = fileLoaction;
                    } else {
                        Log.e(TAG,"갤러리에서 Uri값이 없어 실제 파일로 저장 실패");
                    }
                }
            }
        }
    } // End Of onActivityResult..

    private void nextRegistPage() {
//        Log.d("price", color);
        if (size == null) {
            Toast.makeText(DoguenDoguenApplication.getContext(), "사이즈를 입력해주세요", Toast.LENGTH_SHORT).show();
        } else {
            PostEdit_100_Dialog postEditDialog = PostEdit_100_Dialog.newInstance(imageId, parentId, postDetail, postTitle, dogImagesFileLocation, dogType, dogGender, dogAge, dogCity,
                    dogDistrict, dogPrice, color, size, dhppl, corrona, kennel, parentImagesFileLocation, bloodHierarchyFileLocation);
//            Log.d("price", dogPrice);
//            Log.d("price", color);
            postEditDialog.setOnAdapterItemClickListener(new PostEdit_100_Dialog.OnAdapterItemClickLIstener() {
                @Override
                public void onAdapterItemClick(String answer) {
                    if(listener != null) {
                        listener.onAdapterItemClick("");
                        dismiss();
                    }
                }
            });
            postEditDialog.show(getActivity().getSupportFragmentManager(), "post100Edit");
        }
    }

    public interface OnAdapterItemClickLIstener {
        public void onAdapterItemClick(String answer);
    }
    OnAdapterItemClickLIstener listener;
    public void setOnAdapterItemClickListener(OnAdapterItemClickLIstener listener) {
        this.listener = listener;
    }

    private void previousRegistPage() {
        dismiss();
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

        if (scrollY > oldScrollY) {
            Log.i(TAG, "Scroll DOWN");
        }
        if (scrollY < oldScrollY) {
            Log.i(TAG, "Scroll UP");
        }

        if (scrollY == 0) {
            Log.i(TAG, "TOP SCROLL");
        }

        if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
            Log.i(TAG, "BOTTOM SCROLL");
        }
    }

    //파일 가져오기기
    private String tempSavedBitmapFile(Bitmap tempBitmap) {
        String fileLocation = null; //업로드 전에 가져올 최종적으로 가져올 이미지의 절대주소

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

            fileLocation = tempFile.getAbsolutePath();
        } catch (IOException i) {
            Log.e("저장중 문제발생", i.toString(), i);
        }
        return fileLocation;
    }
    private String findImageFileNameFromUri(Uri tempUri) {
        String fileLocation = null; //업로드 전에 가져올 최종적으로 가져올 이미지의 절대주소

        //실제 Image Uri의 절대이름, 절대 디렉토리!!
        String[] IMAGE_DB_COLUMN = {MediaStore.Images.ImageColumns.DATA};
        Cursor cursor = null;
        try {
            //Primary Key값을 추출
            String imagePK = String.valueOf(ContentUris.parseId(tempUri));
            //Image DB에 쿼리를 날린다.
            cursor = getActivity().getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_DB_COLUMN,
                    MediaStore.Images.Media._ID + "=?",
                    new String[]{imagePK}, null, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                fileLocation = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
            }
        } catch (SQLiteException sqle) {
            Log.e("findImage....", sqle.toString(), sqle);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return fileLocation;
    }
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to write the permission.
                    Toast.makeText(DoguenDoguenApplication.getContext(), "Read/Write external storage", Toast.LENGTH_SHORT).show();
                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_REQUEST_STORAGE);

            } else {
                //사용자가 언제나 허락
            }
        }
    }

    @OnClick(R.id.back)
    public void onBackClicked() {
        this.dismiss();
//        getFragmentManager().popBackStack();
    }
}
