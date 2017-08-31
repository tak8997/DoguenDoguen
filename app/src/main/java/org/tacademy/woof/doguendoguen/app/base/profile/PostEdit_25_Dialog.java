package org.tacademy.woof.doguendoguen.app.base.profile;


import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.adapter.DogImageFragmentAdapter;
import org.tacademy.woof.doguendoguen.model.PostDetailModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostEdit_25_Dialog extends DialogFragment {
    private static final String TAG = "PostEdit";

    private static final int PICK_FROM_GALLERY = 100;
    private final int MY_PERMISSION_REQUEST_STORAGE = 100;

    File myImageDir; //카메라로 찍은 사진을 저장할 디렉토리

    public PostEdit_25_Dialog() {
    }

    public static PostEdit_25_Dialog newInstance(PostDetailModel postDetail) {
        PostEdit_25_Dialog fragment = new PostEdit_25_Dialog();
        Bundle args = new Bundle();
        args.putParcelable("PostDetailModel", postDetail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialogTheme);
        if (getArguments() != null) {
            postDetail = getArguments().getParcelable("PostDetailModel");
        }

        String currentAppPackage = getActivity().getPackageName();
        myImageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), currentAppPackage);
        checkPermission();

        if (!myImageDir.exists()) {
            if (myImageDir.mkdirs()) {
                Toast.makeText(DoguenDoguenApplication.getContext(), " 저장할 디렉토리가 생성 됨", Toast.LENGTH_SHORT).show();
            }
        }
    }
    PostDetailModel postDetail = null;

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

    @BindView(R.id.add_pet_image) RelativeLayout addPetImage;
    @BindView(R.id.add_pet_image_container) RelativeLayout addPetImageContainer;
    @BindView(R.id.pager) ViewPager pager;
//    @BindView(R.id.add_image) ImageView addImage;
    @BindView(R.id.title) EditText postTitle;

    ArrayList<String> dogImagesFileLocation = null;
    String pTitle;
    DogImageFragmentAdapter adapter;
    int cnt = 0;
    int imageId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_regist_25_, container, false);
        ButterKnife.bind(this, view);

        dogImagesFileLocation = new ArrayList<>();
        adapter = new DogImageFragmentAdapter(getActivity().getSupportFragmentManager(), 1);
        pager.setAdapter(adapter);

        //게시글 수정
        if(postDetail != null)
            editPost();

        return view;
    }

    @BindView(R.id.dog_image_sub1_title) TextView firstSubTitle;
    @BindView(R.id.dog_image_sub2_title) TextView secondSubTitle;
    @BindView(R.id.edit_post) TextView updatePostTitle;
    @BindView(R.id.udpate_dog_image) ImageView updateDog;
    private void editPost() {
        Log.d("edit", "edittt");
        String updateDogImageUrl = postDetail.dogImage.get(0).dogImageUrl;
        String title = postDetail.postTitle;

//        dogImagesFileLocation.add(null);

        updateDog.setVisibility(View.VISIBLE);
        firstSubTitle.setVisibility(View.INVISIBLE);
        secondSubTitle.setVisibility(View.INVISIBLE);

        updatePostTitle.setText("분양글 수정하기");
        Glide.with(DoguenDoguenApplication.getContext())
                .load(updateDogImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(updateDog);
        postTitle.setText(title);

        pTitle = title;
    }

    @OnClick({R.id.next_btn, R.id.add_pet_image_container})
    public void onButtonClicked(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.next_btn:
                registNextPost();
                break;
            case R.id.add_pet_image_container:
                addPetImagesFromGallery();
//                addImage.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void registNextPost() {
        String title = postTitle.getText().toString();
        Log.d(TAG, title + "," + dogImagesFileLocation);

        if(title.equals(""))
            Toast.makeText(DoguenDoguenApplication.getContext(), "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
        else {
            PostEdit_50_Dialog postEditDialog = PostEdit_50_Dialog.newInstance(imageId, postDetail, title, dogImagesFileLocation);
            postEditDialog.setOnAdapterItemClickListener(new PostEdit_50_Dialog.OnAdapterItemClickLIstener() {
                @Override
                public void onAdapterItemClick(String answer) {
                    if(listener != null) {
                        listener.onAdapterItemClick("");
                        dismiss();
                    }
                }
            });
            postEditDialog.show(getActivity().getSupportFragmentManager(), "post50Edit");
        }
    }
    public interface OnAdapterItemClickLIstener {
        public void onAdapterItemClick(String answer);
    }
    OnAdapterItemClickLIstener listener;
    public void setOnAdapterItemClickListener(OnAdapterItemClickLIstener listener) {
        this.listener = listener;
    }


    private void addPetImagesFromGallery() {
        if(cnt < 5) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent, PICK_FROM_GALLERY);
            
            cnt++;
        } else {
            Toast.makeText(DoguenDoguenApplication.getContext(), "최대 5장의 사진까지만 등록 가능합니다", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK && requestCode == PICK_FROM_GALLERY) {
            if(data != null) {
                Log.d(TAG, "dataGet");

                Uri imageUri = data.getData();
                if(imageUri != null) {
//                    adapter.addDogImage(imageUri);
//                    adapter.notifyDataSetChanged();
//
//                    addPetImage.setVisibility(View.VISIBLE);
//                    addPetImageContainer.setVisibility(View.GONE);

                    updateDog.setImageURI(imageUri);
                    //업로드 할 수 있도록 절대 주소를 알아낸다.!!!!!!
                    String fileLocation = findImageFileNameFromUri(imageUri);

                    if (fileLocation!=null) {
                        Log.e(TAG, " 갤러리에서 절대주소 Pick 성공");

                        dogImagesFileLocation.add(fileLocation);
                        imageId = postDetail.dogImage.get(0).imageId;
                    }else {
                        Log.e(TAG, " 갤러리에서 절대주소 Pick 실패");
                    }
                } else {
                    Bundle extras = data.getExtras();
                    Bitmap returedBitmap = (Bitmap) extras.get("data");

                    String fileLoaction = tempSavedBitmapFile(returedBitmap);
                    if (fileLoaction != null) {
                        Log.e(TAG, "갤러리에서 Uri값이 없어 실제 파일로 저장 성공");

                        dogImagesFileLocation.add(fileLoaction);
                        imageId = postDetail.dogImage.get(0).imageId;
                    } else {
                        Log.e(TAG,"갤러리에서 Uri값이 없어 실제 파일로 저장 실패");
                    }
                }
            }

        }
    } // End Of onActivityResult..

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

    @OnClick(R.id.back)
    public void onBackClicked() {
//        getFragmentManager().popBackStack();
        this.dismiss();
        Toast.makeText(DoguenDoguenApplication.getContext(), "back", Toast.LENGTH_SHORT).show();
    }
}






