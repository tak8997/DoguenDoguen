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
import android.widget.Toast;

import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.model.PostDetailModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostRegist_75_Fragment extends Fragment implements NestedScrollView.OnScrollChangeListener {
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

    public PostRegist_75_Fragment() {
    }
    public static PostRegist_75_Fragment newInstance(PostDetailModel postDetail, String postTitle, ArrayList<String> dogImagesFileLocation, String dogType, String dogGender, String dogAge, String dogCity, String dogDistrict, String dogPrice) {
        PostRegist_75_Fragment fragment = new PostRegist_75_Fragment();
        Bundle args = new Bundle();
        args.putParcelable("PostDetailModel", postDetail);
        args.putString(POST_TITLE, postTitle);
        args.putStringArrayList(DOG_IMAGES_FILE_LOCATION, dogImagesFileLocation);
        args.putString(DOG_TYPE, dogType);
        args.putString(DOG_GENDER, dogGender);
        args.putString(DOG_AGE, dogAge);
        args.putString(DOG_CITY, dogCity);
        args.putString(DOG_DISTRICT, dogDistrict);
        args.putString(DOG_PRICE, dogPrice);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postDetail = getArguments().getParcelable("PostDetailModel");
            postTitle = getArguments().getString(POST_TITLE);
            dogImagesFileLocation = getArguments().getStringArrayList(DOG_IMAGES_FILE_LOCATION);
            dogType = getArguments().getString(DOG_TYPE);
            dogGender = getArguments().getString(DOG_GENDER);
            dogAge = getArguments().getString(DOG_AGE);
            dogCity = getArguments().getString(DOG_CITY);
            dogDistrict = getArguments().getString(DOG_DISTRICT);
            dogPrice = getArguments().getString(DOG_PRICE);
            Log.d("postTitle" , postTitle);
        }
    }
    PostDetailModel postDetail = null;

    @BindView(R.id.color_white) ImageView white;
    @BindView(R.id.color_ivory) ImageView ivory;
    @BindView(R.id.color_bright_brown) ImageView brightBrown;
    @BindView(R.id.color_dark_brown) ImageView darkBrown;
    @BindView(R.id.color_grey) ImageView grey;
    @BindView(R.id.color_black) ImageView black;
    @BindView(R.id.color_spot) ImageView spot;
    @BindView(R.id.color_etc) ImageView etc;

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
    String postTitle;
    ArrayList<String> dogImagesFileLocation;
    String dogType;
    String dogGender;
    String dogAge;
    String dogCity;
    String dogDistrict;
    String dogPrice;

    //다음 분양글로 넘길 데이터
    String color = null;
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

        scrollView.setOnScrollChangeListener(this);

        initIcon();
        initSelectedIcon();
        initIconImageView();
        initIconName();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(color != null)
            setColorSelection(color);

        if(size != null) {
            if(size.equals("소형견"))
                setSizeSmall();
            else if(size.equals("중형견"))
                setSizeMiddle();
            else
                setSizeBig();
        }
        if(dhppl == 1)
            setVaccineDhppl();
        if(corrona == 1)
            setVaccineCorrona();;
        if(kennel == 1)
            setVaccineKennel();

        if(parentImgUri != null)
            setParentImage();
        if(hierarchyImgUri != null)
            setHierarchyImage();
    }

    HashMap<String, Integer> colorIconMap;
    private void initIcon() {
        colorIconMap = new HashMap<>();
        colorIconMap.put(whiteFlag, R.drawable.white);            colorIconMap.put(ivoryFlag, R.drawable.ivory);
        colorIconMap.put(brightBrownFlag, R.drawable.bright_brown);      colorIconMap.put(darkBrownFlag, R.drawable.dark_brown);
        colorIconMap.put(greyFlag, R.drawable.grey);             colorIconMap.put(blackFlag, R.drawable.black);
        colorIconMap.put(spotFlag, R.drawable.spot);             colorIconMap.put(etcFlag, R.drawable.etc);
    }
    HashMap<String, ImageView> colorImgMap;
    private void initIconImageView() {
        colorImgMap = new HashMap<>();
        colorImgMap.put(whiteFlag, white);            colorImgMap.put(ivoryFlag, ivory);
        colorImgMap.put(brightBrownFlag, brightBrown);      colorImgMap.put(darkBrownFlag, darkBrown);
        colorImgMap.put(greyFlag, grey);             colorImgMap.put(blackFlag, black);
        colorImgMap.put(spotFlag, spot);             colorImgMap.put(etcFlag, etc);
    }
    HashMap<String, Integer> colorIconSelectionMap;
    private void initSelectedIcon() {
        colorIconSelectionMap = new HashMap<>();
        colorIconSelectionMap.put(whiteFlag, R.drawable.white_selected);                     colorIconSelectionMap.put(ivoryFlag, R.drawable.ivory_selected);
        colorIconSelectionMap.put(brightBrownFlag, R.drawable.light_brwon_selected);         colorIconSelectionMap.put(darkBrownFlag, R.drawable.dark_brown_selected);
        colorIconSelectionMap.put(greyFlag, R.drawable.gray_selected);                       colorIconSelectionMap.put(blackFlag, R.drawable.black_selected);
        colorIconSelectionMap.put(spotFlag, R.drawable.dots_selected);                       colorIconSelectionMap.put(etcFlag, R.drawable.etc_selected);
    }
    HashMap<Integer, String> colorIconName;
    private void initIconName() {
        colorIconName = new HashMap<>();
        colorIconName.put(R.id.color_white, "흰색"); colorIconName.put(R.id.color_ivory, "아이보리");
        colorIconName.put(R.id.color_bright_brown, "밝은 갈색"); colorIconName.put(R.id.color_dark_brown, "어두운 갈색");
        colorIconName.put(R.id.color_grey, "회색"); colorIconName.put(R.id.color_black, "검정색");
        colorIconName.put(R.id.color_spot, "얼룩무늬"); colorIconName.put(R.id.color_etc, "기타");
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

    private String whiteFlag = "흰색";
    private String ivoryFlag = "아이보리";
    private String brightBrownFlag = "밝은 갈색";
    private String darkBrownFlag = "어두운 갈색";
    private String greyFlag = "회색";
    private String blackFlag = "검정색";
    private String spotFlag = "얼룩무늬";
    private String etcFlag = "기타";

    private void setColorSelection(String color) {
        for(Map.Entry<String, ImageView> entry : colorImgMap.entrySet()) {
            if(entry.getKey().equals(color))
                colorImgMap.get(color).setImageResource(colorIconSelectionMap.get(color));
        }
    }
    private void setColorDeselection(String oldColor) {
        for (Map.Entry<String, ImageView> entry : colorImgMap.entrySet()) {
            if(entry.getKey().equals(oldColor))
                colorImgMap.get(oldColor).setImageResource(colorIconMap.get(oldColor));
        }
    }

    private String oldColor = null;
    @OnClick({R.id.color_white, R.id.color_ivory, R.id.color_bright_brown, R.id.color_dark_brown,
            R.id.color_grey, R.id.color_black, R.id.color_spot, R.id.color_etc})
    public void onColorClicked(View view) {
        color = colorIconName.get(view.getId());

        if(oldColor != null)
            setColorDeselection(oldColor);

        setColorSelection(color);
        oldColor = color;
    }

    boolean smallFlag = false;
    boolean middleFlag = false;
    boolean bigFlag = false;

    @OnClick({R.id.small, R.id.middle, R.id.big})
    public void onSizeClicked(View view) {
        switch (view.getId()) {
            case R.id.small:
                if(smallFlag == false) {
                    setSizeSmall();
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
                    setSizeMiddle();
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
                    setSizeBig();
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

    private void setSizeSmall() {
        sizeSmall.setBackground(drawableSelected);
        sizeSmall.setTextColor(Color.parseColor("#FFFFFF"));
        sizeMiddle.setBackground(drawable);
        sizeMiddle.setTextColor(Color.parseColor("#3E3A39"));
        sizeBig.setBackground(drawable);
        sizeBig.setTextColor(Color.parseColor("#3E3A39"));
    }

    private void setSizeBig() {
        sizeBig.setBackground(drawableSelected);
        sizeBig.setTextColor(Color.parseColor("#FFFFFF"));
        sizeMiddle.setBackground(drawable);
        sizeMiddle.setTextColor(Color.parseColor("#3E3A39"));
        sizeSmall.setBackground(drawable);
        sizeSmall.setTextColor(Color.parseColor("#3E3A39"));

    }

    private void setSizeMiddle() {
        sizeMiddle.setBackground(drawableSelected);
        sizeMiddle.setTextColor(Color.parseColor("#FFFFFF"));
        sizeSmall.setBackground(drawable);
        sizeSmall.setTextColor(Color.parseColor("#3E3A39"));
        sizeBig.setBackground(drawable);
        sizeBig.setTextColor(Color.parseColor("#3E3A39"));
    }

    boolean dhpplFlag = false;
    boolean coronaFlag = false;
    boolean kennelFlag = false;

    @OnClick({R.id.dhppl, R.id.corrona, R.id.kennel})
    public void onVaccineClicked(View view) {
        switch (view.getId()) {
            case R.id.dhppl:
                if(!dhpplFlag) {
                    setVaccineDhppl();
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
                if(!coronaFlag) {
                    setVaccineCorrona();
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
                if(!kennelFlag) {
                    setVaccineKennel();
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

    private void setVaccineKennel() {
        vaccinKennel.setBackground(drawableSelected);
        vaccinKennel.setTextColor(Color.parseColor("#FFFFFF"));
    }

    private void setVaccineCorrona() {
        vaccinCorrona.setBackground(drawableSelected);
        vaccinCorrona.setTextColor(Color.parseColor("#FFFFFF"));
    }

    private void setVaccineDhppl() {
        vaccinDhppl.setBackground(drawableSelected);
        vaccinDhppl.setTextColor(Color.parseColor("#FFFFFF"));
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
                if(!isParentImageShow) {
                    addParentImageLayout.setVisibility(View.VISIBLE);
                    isParentImageShow = true;
                } else {
                    addParentImageLayout.setVisibility(View.GONE);
                    isParentImageShow = false;
                }
                break;
            case R.id.dog_blood_hierarchy:
                if(!isHierarchyImageShow) {
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
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, imageType);
    }

    private Uri parentImgUri;
    private Uri hierarchyImgUri;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == PARENT_IMAGE_PICK_FROM_GALLERY && data != null) {
                Log.d(TAG, "parentImage");

                parentImgUri = data.getData();
                if(parentImgUri != null) {
                    setParentImage();
                } else {
                    Bundle extras = data.getExtras();
                    Bitmap returedBitmap = (Bitmap) extras.get("data");

                    String fileLoaction = tempSavedBitmapFile(returedBitmap);
                    if (fileLoaction != null) {
                        Log.e(TAG, "갤러리에서 Uri값이 없어 실제 파일로 저장 성공");

                        parentImagesFileLocation = new ArrayList<>();
                        parentImagesFileLocation.add(fileLoaction);
                    } else {
                        Log.e(TAG,"갤러리에서 Uri값이 없어 실제 파일로 저장 실패");
                    }
                }
            } else if(requestCode == HIERARCHY_IMAGE_PICK_FROM_GALLERY && data != null) {
                Log.d(TAG, "bloodHierarchy");

                hierarchyImgUri = data.getData();
                if(hierarchyImgUri != null) {
                    setHierarchyImage();
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

    private void setHierarchyImage() {
        hierarchyImage.setVisibility(View.VISIBLE);
        hierarchyImage.setImageURI(hierarchyImgUri);

        //업로드 할 수 있도록 절대 주소를 알아낸다.!!!!!!
        String fileLocation = findImageFileNameFromUri(hierarchyImgUri);

        if (fileLocation!=null) {
            Log.e(TAG, " 갤러리에서 절대주소 Pick 성공");
            bloodHierarchyFileLocation = fileLocation;
        }else {
            Log.e(TAG, " 갤러리에서 절대주소 Pick 실패");
        }
    }

    private void setParentImage() {
        parentImage.setVisibility(View.VISIBLE);
        parentImage.setImageURI(parentImgUri);

        //업로드 할 수 있도록 절대 주소를 알아낸다.!!!!!!
        String fileLocation = findImageFileNameFromUri(parentImgUri);
        if (fileLocation!=null) {
            Log.e(TAG, " 갤러리에서 절대주소 Pick 성공");

            parentImagesFileLocation = new ArrayList<>();
            parentImagesFileLocation.add(fileLocation);
        }else {
            Log.e(TAG, " 갤러리에서 절대주소 Pick 실패");
        }
    }

    private void nextRegistPage() {
        if(color == null) {
            Toast.makeText(DoguenDoguenApplication.getContext(), "털 색깔을 입력해주세요", Toast.LENGTH_SHORT).show();
        }
        else if (size == null) {
            Toast.makeText(DoguenDoguenApplication.getContext(), "사이즈를 입력해주세요", Toast.LENGTH_SHORT).show();
        }

        else {
            Log.d(TAG, "transfer color : " + color);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.container,
                    PostRegist_100_Fragment.newInstance(postDetail, postTitle, dogImagesFileLocation, dogType, dogGender, dogAge, dogCity,
                            dogDistrict, dogPrice, color, size, dhppl, corrona, kennel, parentImagesFileLocation, bloodHierarchyFileLocation));
            fragmentTransaction.commit();
        }
    }

    private void previousRegistPage() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);

        if (scrollY > oldScrollY) {
            Log.i(TAG, "Scroll DOWN");

            appBarLayout.setVisibility(View.INVISIBLE);
        }
        if (scrollY < oldScrollY) {
            Log.i(TAG, "Scroll UP");

            appBarLayout.setVisibility(View.VISIBLE);
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
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
        getFragmentManager().popBackStack();
    }
}
