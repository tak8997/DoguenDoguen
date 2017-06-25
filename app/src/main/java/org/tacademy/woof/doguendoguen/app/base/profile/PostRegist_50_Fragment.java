package org.tacademy.woof.doguendoguen.app.base.profile;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.adapter.RegionAdapter;
import org.tacademy.woof.doguendoguen.app.base.SearchDogTypeActivity;
import org.tacademy.woof.doguendoguen.model.PostDetailModel;
import org.tacademy.woof.doguendoguen.util.ConvertPxToDpUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static org.tacademy.woof.doguendoguen.R.id.age;
import static org.tacademy.woof.doguendoguen.R.id.city;

public class PostRegist_50_Fragment extends Fragment implements NestedScrollView.OnScrollChangeListener  {
    private final String TAG = "PostRegist_50_Fragment";

    private static final String TITLE = "title";
    private static final String DOGIMAGE ="dogImage";
    private static final int GET_DOG_TYPE = 100;
    private static final int REGION_CITY = 10;
    private static final int REGION_DISTRICT = 20;

    public PostRegist_50_Fragment() {
    }

    public static PostRegist_50_Fragment newInstance(PostDetailModel postDetail, String title, ArrayList<String> dogImagesFileLocation) {
        PostRegist_50_Fragment fragment = new PostRegist_50_Fragment();
        Bundle args = new Bundle();
        args.putParcelable("PostDetailModel", postDetail);
        args.putString(TITLE, title);
        args.putStringArrayList(DOGIMAGE, dogImagesFileLocation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postDetail = getArguments().getParcelable("PostDetailModel");
            postTitle = getArguments().getString(TITLE);
            dogImagesFileLocation = getArguments().getStringArrayList(DOGIMAGE);
        }
        Log.d(TAG, postTitle + " , " + dogImagesFileLocation);
    }
    //이전 분양글 등록하기에서 가져온 데이터.
    PostDetailModel postDetail = null;
    String postTitle;
    ArrayList<String> dogImagesFileLocation;

    @BindView(R.id.search_dog_age) RelativeLayout searchDogLayout;

    @BindView(R.id.dog_gender_box) LinearLayout dogGenderBox;
    @BindView(R.id.dog_age_box) Spinner dogAgeSpinner;
    @BindView(R.id.dog_price_box) LinearLayout dogPriceBox;
    @BindView(R.id.dog_region_box) LinearLayout dogRegionBox;


    @BindView(R.id.dog_gender_arrow) ImageView genderArrow;
    @BindView(R.id.dog_age_arrow) ImageView ageArrow;
    @BindView(R.id.dog_region_arrow) ImageView regionArrow;
    @BindView(R.id.dog_price_arrow) ImageView priceArrow;


    @BindView(R.id.male) TextView male;
    @BindView(R.id.female) TextView female;
    @BindView(R.id.any_gender) TextView anyGender;

    @BindView(R.id.region_city_gridview) GridView cityGridview;
    @BindView(R.id.region_district_gridview) GridView districtGridView;
    @BindView(R.id.city) TextView cityTextView;
    @BindView(R.id.district) TextView districtTextView;

    @BindView(R.id.dog_price_money) EditText dogPriceEdit;

    @BindView(R.id.scroll_view) NestedScrollView scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_regist_50_, container, false);
        ButterKnife.bind(this, view);

        //희망 분양지역 중 도시 선택
        setCitiyRegion();
        //희망 분양지역 중 시군구 선택
        setDistrictRegion();
        
        if(postDetail != null)
            editPost();

        scrollView.setOnScrollChangeListener(this);

        return view;
    }

    @BindView(R.id.dog_type_selected) TextView dogTypeTv;
    @BindView(R.id.dog_gender_selected) TextView dogGenderTv;
    @BindView(R.id.dog_age_selected) TextView dogAgeTv;
    @BindView(R.id.dog_city_selected) TextView dogCityTv;
    @BindView(R.id.dog_district_selected) TextView dogDistrictTv;
    @BindView(R.id.dog_price_selected) TextView dogPriceTv;

    @BindView(R.id.edit_post_title) TextView editPostTitle;
    private void editPost() {
        editPostTitle.setText("분양글 수정하기");
        dogTypeTv.setText(postDetail.dogType);
        dogGenderTv.setText(postDetail.dogGender);
        dogAgeTv.setText(postDetail.dogAge);
        dogCityTv.setText(postDetail.region1);
        dogDistrictTv.setText(postDetail.region2);
        dogPriceTv.setText(postDetail.dogPrice);
    }

    private void setCitiyRegion() {
        final RegionAdapter cityRegionAdapter = new RegionAdapter(REGION_CITY);
        cityRegionAdapter.setCityRegions();
        cityRegionAdapter.notifyDataSetChanged();
        cityRegionAdapter.setOnAdapterItemClickListener(new RegionAdapter.OnAdapterItemClickLIstener() {
            @Override
            public void onAdapterItemClick(int position) {
                String city = (String) cityRegionAdapter.getItem(position);
                dogCityTv.setVisibility(View.VISIBLE);
                dogCityTv.setText(city);

                //다음 분양글 등록으로 넘길 데이터
                dogCity = city;

                ViewGroup.LayoutParams districtLayoutParams = dogRegionBox.getLayoutParams();
                districtLayoutParams.height = (int) ConvertPxToDpUtil.convertDpToPixel(200, getContext());
                dogRegionBox.setLayoutParams(districtLayoutParams);

                districtGridView.setVisibility(View.VISIBLE);
                districtTextView.setBackgroundColor(Color.parseColor("#EDBC64"));
                districtTextView.setTextColor(Color.parseColor("#ffffffff"));
                cityGridview.setVisibility(View.GONE);
                cityTextView.setBackgroundColor(Color.parseColor("#ffffffff"));
                cityTextView.setTextColor(Color.parseColor("#3E3A39"));
            }
        });
        cityGridview.setAdapter(cityRegionAdapter);
    }

    private void setDistrictRegion() {
        final RegionAdapter districtRegionAdatper = new RegionAdapter(REGION_DISTRICT);
        districtRegionAdatper.setDistrictRegions();
        districtRegionAdatper.notifyDataSetChanged();
        districtRegionAdatper.setOnAdapterItemClickListener(new RegionAdapter.OnAdapterItemClickLIstener() {
            @Override
            public void onAdapterItemClick(int position) {
                String district = (String) districtRegionAdatper.getItem(position);
                dogDistrictTv.setVisibility(View.VISIBLE);
                dogDistrictTv.setText(district);

                //다음 분양글 등록으로 넘길 데이터
                dogDistrict = district;

                dogRegionBox.setVisibility(View.GONE);
            }
        });
        districtGridView.setAdapter(districtRegionAdatper);
    }

    boolean genderFlag = false;
    boolean ageFlag = false;
    boolean regionFlag = false;
    boolean priceFlag = false;

    @OnClick({R.id.search_dog_type, R.id.search_dog_gender, R.id.search_dog_age, R.id.search_dog_region, R.id.search_dog_price,
            R.id.prev_btn, R.id.next_btn,
            R.id.male, R.id.female, R.id.any_gender,
            R.id.city, R.id.district,
            R.id.dog_price_box, R.id.price_selection
            })
    public void onDogConditionClicked(View view) {

        switch (view.getId()) {
            case R.id.search_dog_type:
                Intent intent = new Intent(DoguenDoguenApplication.getContext(), SearchDogTypeActivity.class);
                startActivityForResult(intent, GET_DOG_TYPE);
                break;
            case R.id.search_dog_gender:
                if(genderFlag == false) {
                    dogGenderBox.setVisibility(View.VISIBLE);
                    genderArrow.setImageResource(R.drawable.up_arrow);
                    genderFlag = true;
                } else {
                    dogGenderBox.setVisibility(View.GONE);
                    genderArrow.setImageResource(R.drawable.down_arrow);
                    genderFlag = false;
                }
                break;
            case R.id.search_dog_age:
                setDogAgeSpinner();
                if(ageFlag == false) {
                    dogAgeSpinner.setVisibility(View.VISIBLE);
                    ageArrow.setImageResource(R.drawable.up_arrow);
                    ageFlag = true;
                } else {
                    dogAgeSpinner.setVisibility(View.GONE);
                    ageArrow.setImageResource(R.drawable.down_arrow);
                    ageFlag = false;
                }

                break;
            case R.id.search_dog_region:
                if(regionFlag == false) {
                    dogRegionBox.setVisibility(View.VISIBLE);
                    regionArrow.setImageResource(R.drawable.up_arrow);
                    regionFlag = true;
                } else {
                    dogRegionBox.setVisibility(View.GONE);
                    regionArrow.setImageResource(R.drawable.down_arrow);
                    regionFlag = false;
                }
                break;
            case R.id.search_dog_price:
                if(priceFlag == false) {
                    dogPriceBox.setVisibility(View.VISIBLE);
                    priceArrow.setImageResource(R.drawable.up_arrow);

                    //버튼 하단 탭과 겹치므로 하단 탭을 없애줌.
                    getActivity().findViewById(R.id.appbar).setVisibility(View.GONE);
                    priceFlag = true;
                } else {
                    dogPriceBox.setVisibility(View.GONE);
                    priceArrow.setImageResource(R.drawable.down_arrow);

                    getActivity().findViewById(R.id.appbar).setVisibility(View.VISIBLE);
                    priceFlag = false;
                }

                break;

            //dog gender 선택
            case R.id.male:
                dogGenderTv.setVisibility(View.VISIBLE);
                dogGenderTv.setText(male.getText().toString());
                //다음 분양글 등록으로 넘길 데이터
                dogGender = male.getText().toString();

                male.setTextColor(Color.parseColor("#EDBC64"));
                female.setTextColor(Color.parseColor("#3E3A39"));
                anyGender.setTextColor(Color.parseColor("#3E3A39"));
                break;
            case R.id.female:
                dogGenderTv.setVisibility(View.VISIBLE);
                dogGenderTv.setText(female.getText().toString());
                //다음 분양글 등록으로 넘길 데이터
                dogGender = female.getText().toString();

                female.setTextColor(Color.parseColor("#EDBC64"));
                male.setTextColor(Color.parseColor("#3E3A39"));
                anyGender.setTextColor(Color.parseColor("#3E3A39"));
                break;
            case R.id.any_gender:
                dogGenderTv.setVisibility(View.VISIBLE);
                dogGenderTv.setText(anyGender.getText().toString());
                //다음 분양글 등록으로 넘길 데이터
                dogGender = anyGender.getText().toString();

                anyGender.setTextColor(Color.parseColor("#EDBC64"));
                male.setTextColor(Color.parseColor("#3E3A39"));
                female.setTextColor(Color.parseColor("#3E3A39"));
                break;

            //dog region 선택
            case city:
                setRegionLayoutHeight(200);

                cityGridview.setVisibility(View.VISIBLE);
                cityTextView.setBackgroundColor(Color.parseColor("#EDBC64"));
                cityTextView.setTextColor(Color.parseColor("#ffffffff"));
                districtGridView.setVisibility(View.INVISIBLE);
                districtTextView.setBackgroundColor(Color.parseColor("#ffffffff"));
                districtTextView.setTextColor(Color.parseColor("#3E3A39"));
                break;
            case R.id.district:
                setRegionLayoutHeight(156);

                districtGridView.setVisibility(View.VISIBLE);
                districtTextView.setBackgroundColor(Color.parseColor("#EDBC64"));
                districtTextView.setTextColor(Color.parseColor("#ffffffff"));
                cityGridview.setVisibility(View.INVISIBLE);
                cityTextView.setBackgroundColor(Color.parseColor("#ffffffff"));
                cityTextView.setTextColor(Color.parseColor("#3E3A39"));
                break;

            //dog price 선택
            case R.id.dog_price_box:
                InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(dogRegionBox, 0);

                dogPriceTv.setVisibility(View.VISIBLE);
                dogPriceEdit.requestFocus();
                break;
            case R.id.price_selection:
                String price = dogPriceEdit.getText().toString();
                dogPriceTv.setVisibility(View.VISIBLE);
                dogPriceTv.setText(price + "만원");
                dogAgeSpinner.setVisibility(View.VISIBLE);
                dogPrice = price;
                break;
            case R.id.prev_btn:
                previousRegistPage();
                break;
            case R.id.next_btn:
                nextRegistPage();
                break;
        }
    }

    //다음 분양글에 가져갈 데이터
    String dogType = null;
    String dogGender = null;
    String dogAge = null;
    String dogCity = null;
    String dogDistrict = null;
    String dogPrice = null;

    private void setRegionLayoutHeight(int px) {
        ViewGroup.LayoutParams districtLayoutParams = dogRegionBox.getLayoutParams();
        districtLayoutParams.height = (int) ConvertPxToDpUtil.convertDpToPixel(px, getContext());
        dogRegionBox.setLayoutParams(districtLayoutParams);
    }

    private void previousRegistPage() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    private void nextRegistPage() {
        if(dogType == null || dogGender == null || dogAge == null || dogCity == null || dogDistrict == null || dogPrice == null)
            Toast.makeText(DoguenDoguenApplication.getContext(), "항목을 전부 입력해주세요", Toast.LENGTH_SHORT).show();
        else {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.container,
                    PostRegist_75_Fragment.newInstance(postDetail, postTitle, dogImagesFileLocation, dogType, dogGender, dogAge, dogCity, dogDistrict, dogPrice));
            fragmentTransaction.commit();
        }
    }

    private void setDogAgeSpinner() {
        ArrayList<String> dogGenders = new ArrayList<>();
        dogGenders.add("1개월");dogGenders.add("2개월");dogGenders.add("3개월");
        dogGenders.add("4개월");dogGenders.add("5개월");dogGenders.add("6개월");
        dogGenders.add("7개월");dogGenders.add("8개월");dogGenders.add("9개월");
        dogGenders.add("10개월");dogGenders.add("11개월");dogGenders.add("12개월");
        dogGenders.add("13개월");dogGenders.add("14개월");dogGenders.add("15개월");
        dogGenders.add("16개월");dogGenders.add("17개월");dogGenders.add("18개월");

        DogAgeSpinnerAdapter adapter = new DogAgeSpinnerAdapter(dogGenders);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(DoguenDoguenApplication.getContext(),
//                R.array.age_spinner, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        Field popup = null;
        try {
            popup =Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(dogAgeSpinner);
            popupWindow.setHeight((int) ConvertPxToDpUtil.convertDpToPixel(133, getContext()));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        dogAgeSpinner.setAdapter(adapter);
        dogAgeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView age = (TextView) view.findViewById(R.id.age);
                dogAgeTv.setVisibility(View.VISIBLE);
                dogAgeTv.setText(age.getText().toString());

                dogAge = age.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                dogAgeTv.setText("");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == GET_DOG_TYPE) {
            dogType = data.getExtras().getString(SearchDogTypeActivity.DOGTYPE);

            dogTypeTv.setVisibility(View.VISIBLE);
            dogTypeTv.setText(dogType);
        }
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

    //Dog age Select Adapter
    private class DogAgeSpinnerAdapter extends BaseAdapter {
        private ArrayList<String> dogGenders;

        public DogAgeSpinnerAdapter(ArrayList<String> dogGenders) {
            this.dogGenders = dogGenders;
        }

        @Override
        public int getCount() {
            return dogGenders.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if(view == null) {
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.dog_age_spinner_item, null, false);
            }

            TextView ageTv = (TextView) view.findViewById(age);
            ageTv.setText(dogGenders.get(position));

            return view;
        }
    }

    @OnClick(R.id.back)
    public void onBackClicked() {
        getFragmentManager().popBackStack();
    }
}
