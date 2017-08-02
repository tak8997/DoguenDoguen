package org.tacademy.woof.doguendoguen.app.sign;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
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
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.app.base.BaseActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Tak on 2017. 6. 4..
 */

public class SignUpActivity extends BaseActivity implements NestedScrollView.OnScrollChangeListener {
    private static final String TAG = "SignUpActivity";

    @BindView(R.id.next_btn) Button nextBtn;
//    @BindView(R.id.sign_up_user) RelativeLayout userImage;
    @BindView(R.id.user_image) ImageView userImage;
    @BindView(R.id.user_name) EditText userName;
    @BindView(R.id.user_gender) TextView userGender;
    @BindView(R.id.add_user) TextView addUserText;
    @BindView(R.id.add_user_image) ImageView addUserImage;
    @BindView(R.id.edit_gender) ImageView editGender;
    @BindView(R.id.user_gender_box) LinearLayout genderSelectBox;

    @BindView(R.id.female) TextView female;
    @BindView(R.id.male) TextView male;
    @BindView(R.id.any_gender) TextView anyGender;

    private static final int PICK_FROM_GALLERY = 100;
    private static final int NEXT_SIGN_UP = 200;
    private final int MY_PERMISSION_REQUEST_STORAGE = 100;

    private File myImageDir; //카메라로 찍은 사진을 저장할 디렉토리
    private String userImageFileLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        String currentAppPackage = this.getPackageName();

        myImageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), currentAppPackage);

        checkPermission();

        if (!myImageDir.exists()) {
            if (myImageDir.mkdirs()) {
                Toast.makeText(DoguenDoguenApplication.getContext(), " 저장할 디렉토리가 생성 됨", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.next_btn)
    public void nextRegistClicked() {
        String name = userName.getText().toString();
        String gender = userGender.getText().toString();

        if(userImageFileLocation == null) {
            Toast.makeText(this, "이미지를 등록해주세요", Toast.LENGTH_SHORT).show();
        } else if(name == null) {
            Toast.makeText(this, "이름은 등록해주세요", Toast.LENGTH_SHORT).show();
        } else if(gender == null) {
            Toast.makeText(this, "성별은 등록해주세요", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(SignUpActivity.this, SignUpEndActivity.class);
            intent.putExtra("userImage", userImageFileLocation);
            intent.putExtra("userName", name);
            intent.putExtra("userGender", gender);
            startActivityForResult(intent, NEXT_SIGN_UP);
        }
    }

    boolean genderFlag = false;

    @OnClick({R.id.sign_up_user, R.id.add_user_name, R.id.add_user_gender})
    public void onRegistUser(View view) {
        switch (view.getId()) {
            case R.id.sign_up_user:
                addPetImagesFromGallery();
                break;
            case R.id.add_user_name:
                InputMethodManager inputNameManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputNameManager.toggleSoftInputFromWindow(userName.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                userName.getText();
                break;
            case R.id.add_user_gender:
//                InputMethodManager inputGenderManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputGenderManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
//                userGender.setText("성별별");
                if(genderFlag == false) {
                    editGender.setImageResource(R.drawable.up_arrow);
                    genderSelectBox.setVisibility(View.VISIBLE);

                    genderFlag = true;
                } else {
                    editGender.setImageResource(R.drawable.down_arrow2);
                    genderSelectBox.setVisibility(View.GONE);

                    genderFlag = false;
                }

               break;
        }
    }

    @OnClick({R.id.female, R.id.male, R.id.any_gender})
    public void onGenderClicked(View view) {
        switch (view.getId()) {
            case R.id.female:
                userGender.setText("여성");
                female.setTextColor(Color.parseColor("#EDBC64"));
                male.setTextColor(Color.parseColor("#3E3A39"));
                anyGender.setTextColor(Color.parseColor("#3E3A39"));
                break;
            case R.id.male:
                userGender.setText("남성");
                male.setTextColor(Color.parseColor("#EDBC64"));
                female.setTextColor(Color.parseColor("#3E3A39"));
                anyGender.setTextColor(Color.parseColor("#3E3A39"));
                break;
            case R.id.any_gender:
                userGender.setText("상관없음");
                anyGender.setTextColor(Color.parseColor("#EDBC64"));
                male.setTextColor(Color.parseColor("#3E3A39"));
                female.setTextColor(Color.parseColor("#3E3A39"));
                break;
        }
    }

    private void addPetImagesFromGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == PICK_FROM_GALLERY) {
                Log.d(TAG, "dataGet");

                Uri imageUri = data.getData();
                if(imageUri != null ) {
                    addUserImage.setVisibility(View.INVISIBLE);
                    addUserText.setVisibility(View.INVISIBLE);
                    userImage.setVisibility(View.VISIBLE);
                    userImage.setImageURI(imageUri);

                    //업로드 할 수 있도록 절대 주소를 알아낸다.!!!!!!
                    userImageFileLocation = findImageFileNameFromUri(imageUri);

                    if (userImageFileLocation != null) {
                        Log.e(TAG, " 갤러리에서 절대주소 Pick 성공");
                    }else {
                        Log.e(TAG, " 갤러리에서 절대주소 Pick 실패");
                    }
                } else {
                    Bundle extras = data.getExtras();
                    Bitmap returedBitmap = (Bitmap) extras.get("data");

                    String fileLoaction = tempSavedBitmapFile(returedBitmap);
                    if (fileLoaction != null) {
                        Log.e(TAG, "갤러리에서 Uri값이 없어 실제 파일로 저장 성공");
                    } else {
                        Log.e(TAG,"갤러리에서 Uri값이 없어 실제 파일로 저장 실패");
                    }
                }
            } else if(requestCode == NEXT_SIGN_UP) {
                //SignUpEndActivity로 부터 userId를 받아옴.
                String userId = data.getExtras().getString("userId");

                //받아온 userId를 다시 LoginFragment로 돌려줌.
                Intent intent = new Intent();
                intent.putExtra("userId", userId);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

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
            cursor = this.getContentResolver().query(
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
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
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
}















