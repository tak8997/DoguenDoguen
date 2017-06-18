package org.tacademy.woof.doguendoguen.app.base.profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.model.PostDetailModel;
import org.tacademy.woof.doguendoguen.rest.RestService;
import org.tacademy.woof.doguendoguen.rest.post.PostService;

import java.io.File;
import java.util.ArrayList;

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

public class PostRegist_100_Fragment extends Fragment {
    private static final String TAG = "PostRegist_100_Fragment";

    public static final MediaType IMAGE_MIME_TYPE = MediaType.parse("image/*");

    private static final String POST_TITLE = "postTitle";
    private static final String DOG_IMAGES_FILE_LOCATION = "dogImagesFileLocation";
    private static final String DOG_TYPE = "dogType";
    private static final String DOG_GENDER = "dogGender";
    private static final String DOG_AGE = "dogAge";
    private static final String DOG_CITY = "dogCity";
    private static final String DOG_DISTRICT = "dogDistrict";
    private static final String DOG_PRICE = "dogPrice";
    private static final String DOG_COLOR = "dogColor";
    private static final String DOG_SIZE = "dogSize";
    private static final String VACCIN_DHPPL = "vaccinDhppl";
    private static final String VACCIN_CORRONA = "vaccinCorrona";
    private static final String VACCIN_KENNEL = "vaccinKennel";
    private static final String PARENT_IMAGES_FILE_LOCATION = "parentImagesFileLocation";
    private static final String BLOOD_HIERARCHY_FILE_LOCATION = "bloodHierarchyFileLocation";

    public PostRegist_100_Fragment() {
    }

    public static PostRegist_100_Fragment newInstance(PostDetailModel postDetail, String postTitle, ArrayList<String> dogImagesFileLocation, String dogType, String dogGender, String dogAge, String dogCity, String dogDistrict, String dogPrice,
                                                      String color, String size, int dhppl, int corrona, int kennel, ArrayList<String> parentImagesFileLocation, String bloodHierarchyFileLocation) {
        PostRegist_100_Fragment fragment = new PostRegist_100_Fragment();
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
        args.putString(DOG_COLOR, color);
        args.putString(DOG_SIZE, size);
        args.putInt(VACCIN_DHPPL, dhppl);
        args.putInt(VACCIN_CORRONA, corrona);
        args.putInt(VACCIN_KENNEL, kennel);
        args.putStringArrayList(PARENT_IMAGES_FILE_LOCATION, parentImagesFileLocation);
        args.putString(BLOOD_HIERARCHY_FILE_LOCATION, bloodHierarchyFileLocation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postDetail = getArguments().getParcelable("PostDetailModel");
            postTitle = getArguments().getParcelable(POST_TITLE);
            dogImagesFileLocation = getArguments().getStringArrayList(DOG_IMAGES_FILE_LOCATION);
            dogType = getArguments().getString(DOG_TYPE);
            dogGender = getArguments().getString(DOG_GENDER);
            dogAge = getArguments().getString(DOG_AGE);
            dogCity = getArguments().getString(DOG_CITY);
            dogDistrict = getArguments().getString(DOG_DISTRICT);
            dogPrice = getArguments().getString(DOG_PRICE);
            dogColor = getArguments().getString(DOG_COLOR);
            dogSize = getArguments().getString(DOG_SIZE);
            dhppl = getArguments().getInt(VACCIN_DHPPL);
            corrona = getArguments().getInt(VACCIN_CORRONA);
            kennel = getArguments().getInt(VACCIN_KENNEL);
            parentImagesFileLocation = getArguments().getStringArrayList(PARENT_IMAGES_FILE_LOCATION);
            bloodHierarchyFileLocation = getArguments().getString(BLOOD_HIERARCHY_FILE_LOCATION);
        }
    }
    PostDetailModel postDetail = null;

    //이전 등록페이지에서 넘어온 데이터
    int userId = 21;
    String postTitle;
    ArrayList<String> dogImagesFileLocation;
    String dogType;
    String dogGender;
    String dogAge;
    String dogCity;
    String dogDistrict;
    String dogPrice;    
    String dogColor;    //null가능
    String dogSize;     
    int dhppl = 0;      //null가능
    int corrona = 0;    //null가능
    int kennel = 0;     //null가능
    ArrayList<String> parentImagesFileLocation = null;  //null가능
    String bloodHierarchyFileLocation = null;           //null가능

    @BindView(R.id.regist_btn) Button registBtn;
    @BindView(R.id.post_intro) EditText postIntro;
    @BindView(R.id.post_sub_intro) EditText postSubIntro;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_regist_100_, container, false);

        return view;
    }

    @BindView(R.id.postEditTitle) TextView postEditTitle;
    private void editPost() {
        postEditTitle.setText("분양글 수정하기");
        postIntro.setText(postDetail.postIntro);
        postSubIntro.setText(postDetail.postCondition);
    }

    //이번 페이지에서 등록할 조건
    String postContent = null;
    String postSubContent = null;

    @OnClick({R.id.regist_btn})
    public void onRegistrationFinishClicked(View view) {
        if(view.getId() == R.id.regist_btn) {
            postContent = postIntro.getText().toString();   //소개글
            postSubContent = postSubIntro.getText().toString();//조건글

            if(postContent == null || postSubContent == null) {
                Toast.makeText(DoguenDoguenApplication.getContext(), "분양글을 모두 작성해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                //파일 업로드(3개)
                //펫 이미지
                String dogImageFileLocation = dogImagesFileLocation.get(0);
                File uploadDogImages = new File(dogImageFileLocation);
                RequestBody dogImageRequestFile = RequestBody.create(IMAGE_MIME_TYPE, uploadDogImages);
                MultipartBody.Part dogImage = MultipartBody.Part.createFormData("pet", uploadDogImages.getName(), dogImageRequestFile);

                //부모견 이미지
                File uploadParentDogImages = null;
                MultipartBody.Part parentDogImage = null;
                if(parentImagesFileLocation != null) {
                    Log.d(TAG, "parentImageLocation");
                    uploadParentDogImages = new File(parentImagesFileLocation.get(0));
                    RequestBody dogParentRequestFile = RequestBody.create(IMAGE_MIME_TYPE, uploadParentDogImages);
                    parentDogImage = MultipartBody.Part.createFormData("parent", uploadParentDogImages.getName(), dogParentRequestFile);
                }

                //혈통서 이미지
                File uploadHierarchyImage = null;
                MultipartBody.Part hierarchyImage = null;
                if(bloodHierarchyFileLocation != null) {
                    Log.d(TAG, "hierarchyLocation");
                    uploadHierarchyImage = new File(bloodHierarchyFileLocation);
                    RequestBody hierarchRequestFile = RequestBody.create(IMAGE_MIME_TYPE, uploadHierarchyImage);
                    hierarchyImage = MultipartBody.Part.createFormData("lineage", uploadHierarchyImage.getName(), hierarchRequestFile);
                }

                //그 이외 Post내용들(15개)
                RequestBody id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userId));
                RequestBody title = RequestBody.create(MediaType.parse("text/plain"), postTitle);
                RequestBody type = RequestBody.create(MediaType.parse("text/plain"), dogType);
                RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), dogGender);
                RequestBody age = RequestBody.create(MediaType.parse("text/plain"), dogAge);
                RequestBody city = RequestBody.create(MediaType.parse("text/plain"), dogCity);
                RequestBody district = RequestBody.create(MediaType.parse("text/plain"), dogDistrict);
                RequestBody price = RequestBody.create(MediaType.parse("text/plain"), dogPrice);
                RequestBody color = RequestBody.create(MediaType.parse("text/plain"), dogColor);
                RequestBody size = RequestBody.create(MediaType.parse("text/plain"), dogSize);
                RequestBody vDhppl = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(dhppl));
                RequestBody vCorrona = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(corrona));
                RequestBody vKennel = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(kennel));
                RequestBody introduction = RequestBody.create(MediaType.parse("text/plain"), postContent);
                RequestBody condition = RequestBody.create(MediaType.parse("text/plain"), postSubContent);

                PostService postService = RestService.createService(PostService.class);
                Call<ResponseBody> registerPostCall = postService.registerPost(dogImage, parentDogImage, hierarchyImage,
                        id, title, type, gender, age, city, district, price, color, size, vDhppl, vCorrona, vKennel, introduction, condition);
                registerPostCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(DoguenDoguenApplication.getContext(), "등록이 완료되었습니다", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "success" + response.code() + response.raw().toString());

                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            for(int i=0; i < fm.getBackStackEntryCount(); ++i) {
                                fm.popBackStack();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(TAG, t.getMessage());
                    }
                });   
            }
        } //End of onRegistrationFinishClicked
    }
}









