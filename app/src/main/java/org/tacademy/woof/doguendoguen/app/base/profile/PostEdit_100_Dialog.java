package org.tacademy.woof.doguendoguen.app.base.profile;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.model.PostDetailModel;
import org.tacademy.woof.doguendoguen.rest.RestClient;
import org.tacademy.woof.doguendoguen.rest.post.PostService;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PostEdit_100_Dialog extends DialogFragment {
    private static final String TAG = "PostEdit_100_";

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

    public PostEdit_100_Dialog() {
    }

    public static PostEdit_100_Dialog newInstance(int imageId, int parentId, PostDetailModel postDetail, String postTitle, ArrayList<String> dogImagesFileLocation, String dogType, String dogGender, String dogAge, String dogCity, String dogDistrict, String dogPrice,
                                                  String color, String size, int dhppl, int corrona, int kennel, ArrayList<String> parentImagesFileLocation, String bloodHierarchyFileLocation) {
        PostEdit_100_Dialog fragment = new PostEdit_100_Dialog();
        Bundle args = new Bundle();
        args.putInt("imageId", imageId);
        args.putInt("parentId", parentId);
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
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialogTheme);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageId = getArguments().getInt("imageId");
            parentId = getArguments().getInt("parentId");
            postDetail = getArguments().getParcelable("PostDetailModel");
            postTitle = getArguments().getString(POST_TITLE);
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
//            Log.d("asdf", dogColor);
        }
    }
    PostDetailModel postDetail = null;

    //이전 등록페이지에서 넘어온 데이터
    int imageId;
    int parentId;
    int userId;
    String postTitle;
    ArrayList<String> dogImagesFileLocation;
    String dogType;
    String dogGender;
    String dogAge;
    String dogCity;
    String dogDistrict;
    String dogPrice = " ";
    String dogColor = " ";    //null가능
    String dogSize;     
    int dhppl = 0;      //null가능
    int corrona = 0;    //null가능
    int kennel = 0;     //null가능
    ArrayList<String> parentImagesFileLocation = null;  //null가능
    String bloodHierarchyFileLocation = " ";           //null가능

    @BindView(R.id.regist_btn) Button registBtn;
    @BindView(R.id.post_intro) EditText postIntro;
    @BindView(R.id.post_sub_intro) EditText postSubIntro;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_regist_100_, container, false);
        ButterKnife.bind(this, view);
        editPost();

        return view;
    }

    @BindView(R.id.edit_title) TextView postEditTitle;

    private void editPost() {
        postEditTitle.setText("분양글 수정하기");
        postIntro.setText(postDetail.postIntro);
        postSubIntro.setText(postDetail.postCondition);

        registBtn.setText("수정 완료");
    }

    //이번 페이지에서 등록할 조건
    String postContent = null;
    String postSubContent = null;

    @BindView(R.id.circularProgressbar)
    ProgressBar progressBar;

    @OnClick({R.id.regist_btn})
    public void onRegistrationFinishClicked(View view) {
        Log.d("edit", "clicked");
        if(view.getId() == R.id.regist_btn) {
            postContent = postIntro.getText().toString();   //소개글
            postSubContent = postSubIntro.getText().toString();//조건글

            if(postContent == null || postSubContent == null) {
                Toast.makeText(DoguenDoguenApplication.getContext(), "분양글을 모두 작성해주세요.", Toast.LENGTH_SHORT).show();
            } else {

                registBtn.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);

                //파일 업로드(3개)
                //펫 이미지
                String dogImageFileLocation = null;
                File uploadDogImages = null;
                MultipartBody.Part dogImage = null;
                if(dogImagesFileLocation.size() != 0 ) {
                    Log.d("asdfasdf", dogImagesFileLocation.get(0));
                    dogImageFileLocation = dogImagesFileLocation.get(0);
                    uploadDogImages = new File(dogImageFileLocation);
                    RequestBody dogImageRequestFile = RequestBody.create(IMAGE_MIME_TYPE, uploadDogImages);
                    dogImage = MultipartBody.Part.createFormData("pet", uploadDogImages.getName(), dogImageRequestFile);
                }

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
                RequestBody petImageId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(imageId));
                RequestBody parentImageId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(parentId));
                RequestBody id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userId));
                RequestBody title = RequestBody.create(MediaType.parse("text/plain"), postTitle);
                RequestBody type = RequestBody.create(MediaType.parse("text/plain"), dogType);
                RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), dogGender);
                RequestBody age = RequestBody.create(MediaType.parse("text/plain"), dogAge);
                RequestBody city = RequestBody.create(MediaType.parse("text/plain"), dogCity);
                RequestBody district = RequestBody.create(MediaType.parse("text/plain"), dogDistrict);
                RequestBody price = RequestBody.create(MediaType.parse("text/plain"), dogPrice);
                RequestBody color = RequestBody.create(MediaType.parse("text/plain"), "");
                RequestBody size = RequestBody.create(MediaType.parse("text/plain"), dogSize);
                RequestBody vDhppl = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(dhppl));
                RequestBody vCorrona = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(corrona));
                RequestBody vKennel = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(kennel));
                RequestBody introduction = RequestBody.create(MediaType.parse("text/plain"), postContent);
                RequestBody condition = RequestBody.create(MediaType.parse("text/plain"), postSubContent);

                PostService postService = RestClient.createService(PostService.class);
                Observable<JsonObject> registerPostCall = postService.updatePost(petImageId, parentImageId, postDetail.postId, dogImage, parentDogImage, hierarchyImage,
                        id, title, type, gender, age, city, district, price, color, size, vDhppl, vCorrona, vKennel, introduction, condition);

                registerPostCall.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(jsonObject -> {
                            if(jsonObject!= null) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(DoguenDoguenApplication.getContext(), "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                                if(listener != null) {
                                    listener.onAdapterItemClick("");
                                    dismiss();
                                }
                            } else {
                                Toast.makeText(DoguenDoguenApplication.getContext(), "수정에 실패하였습니다. 다시 한번 시도해주세요.", Toast.LENGTH_SHORT).show();
                                registBtn.setEnabled(true);
                            }
                        }, throwable -> {
                            if(listener != null) {
                                listener.onAdapterItemClick("");
                                dismiss();
                            }
                        });
            }
        } //End of onRegistrationFinishClicked
    }
    @OnClick(R.id.back)
    public void onBackClicked() {
        this.dismiss();
    }
    public interface OnAdapterItemClickLIstener {
        public void onAdapterItemClick(String answer);
    }

    OnAdapterItemClickLIstener listener;
    public void setOnAdapterItemClickListener(OnAdapterItemClickLIstener listener) {
        this.listener = listener;
    }
}









