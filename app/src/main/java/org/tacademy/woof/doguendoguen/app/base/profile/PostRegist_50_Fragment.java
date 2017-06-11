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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.adapter.RegionAdapter;
import org.tacademy.woof.doguendoguen.app.base.SearchDogTypeActivity;
import org.tacademy.woof.doguendoguen.util.ConvertPxToDp;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static org.tacademy.woof.doguendoguen.R.id.city;

public class PostRegist_50_Fragment extends Fragment {
    private static final String TITLE = "title";
    private static final String DOGIMAGE ="dogImage";
    private static final int GET_DOG_TYPE = 100;
    private static final int REGION_CITY = 10;
    private static final int REGION_DISTRICT = 20;

    public PostRegist_50_Fragment() {
    }

    public static PostRegist_50_Fragment newInstance(String title, ArrayList<String> dogImage) {
        PostRegist_50_Fragment fragment = new PostRegist_50_Fragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putStringArrayList(DOGIMAGE, dogImage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postTitle = getArguments().getString(TITLE);
            dogImage = getArguments().getStringArrayList(DOGIMAGE);
        }
    }
    String postTitle;
    ArrayList<String> dogImage;

    @BindView(R.id.search_dog_age) RelativeLayout searchDogLayout;

    @BindView(R.id.dog_type_selected) TextView dogTypeTv;
    @BindView(R.id.dog_gender_selected) TextView dogGenderTv;
    @BindView(R.id.dog_age_selected) TextView dogAgeTv;
    @BindView(R.id.dog_city_selected) TextView dogCityTv;
    @BindView(R.id.dog_district_selected) TextView dogDistrictTv;
    @BindView(R.id.dog_price_selected) TextView dogPriceTv;


    @BindView(R.id.dog_gender_box) LinearLayout dogGenderBox;
//    @BindView(R.id.dog_age_box) Spinner dogAgeSpinnerBox;
    @BindView(R.id.dog_price_box) RelativeLayout dogPriceBox;
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

    @BindView(R.id.dog_price_money) EditText dogPrice;

    @BindView(R.id.scroll_view) NestedScrollView scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_regist_50_, container, false);
        ButterKnife.bind(this, view);

        //희망 분양지역 중 도시 선택
        final RegionAdapter cityRegionAdapter = new RegionAdapter(REGION_CITY);
        cityRegionAdapter.setCityRegions();
        cityRegionAdapter.notifyDataSetChanged();
        cityRegionAdapter.setOnAdapterItemClickListener(new RegionAdapter.OnAdapterItemClickLIstener() {
            @Override
            public void onAdapterItemClick(int position) {
                String city = (String) cityRegionAdapter.getItem(position);
                dogCityTv.setVisibility(View.VISIBLE);
                dogCityTv.setText(city);

                ViewGroup.LayoutParams districtLayoutParams = dogRegionBox.getLayoutParams();
                districtLayoutParams.height = (int) ConvertPxToDp.convertDpToPixel(200, getContext());
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


        //희망 분양지역 중 시군구 선택
        final RegionAdapter districtRegionAdatper = new RegionAdapter(REGION_DISTRICT);
        districtRegionAdatper.setDistrictRegions();
        districtRegionAdatper.notifyDataSetChanged();
        districtRegionAdatper.setOnAdapterItemClickListener(new RegionAdapter.OnAdapterItemClickLIstener() {
            @Override
            public void onAdapterItemClick(int position) {
                String district = (String) districtRegionAdatper.getItem(position);
                dogDistrictTv.setVisibility(View.VISIBLE);
                dogDistrictTv.setText(district);
            }
        });
        districtGridView.setAdapter(districtRegionAdatper);

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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
        });


        return view;
    }

    boolean genderFlag = false;

    @OnClick({R.id.search_dog_type, R.id.search_dog_gender, R.id.search_dog_age, R.id.search_dog_region, R.id.search_dog_price,
            R.id.prev_btn, R.id.next_btn,
            R.id.male, R.id.female, R.id.any_gender,
            R.id.city, R.id.district,
            R.id.dog_price_box
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
//                setDogAgeSpinner();
                dogAgeTv.setVisibility(View.VISIBLE);
                dogAgeTv.setText("2개월");
                ageArrow.setImageResource(R.drawable.up_arrow);
                break;
            case R.id.search_dog_region:
                dogRegionBox.setVisibility(View.VISIBLE);
                regionArrow.setImageResource(R.drawable.up_arrow);

                break;
            case R.id.search_dog_price:
                dogPriceBox.setVisibility(View.VISIBLE);
                dogPrice.setSelected(false);
                priceArrow.setImageResource(R.drawable.up_arrow);
                break;

            //dog gender 선택
            case R.id.male:
                dogGenderTv.setVisibility(View.VISIBLE);
                dogGenderTv.setText(male.getText().toString());
                male.setTextColor(Color.parseColor("#EDBC64"));
                break;
            case R.id.female:
                dogGenderTv.setVisibility(View.VISIBLE);
                dogGenderTv.setText(female.getText().toString());
                female.setTextColor(Color.parseColor("#EDBC64"));
                break;
            case R.id.any_gender:
                dogGenderTv.setVisibility(View.VISIBLE);
                dogGenderTv.setText(anyGender.getText().toString());
                anyGender.setTextColor(Color.parseColor("#EDBC64"));
                break;

            //dog region 선택
            case city:
                ViewGroup.LayoutParams cityLayoutParams = dogRegionBox.getLayoutParams();
                cityLayoutParams.height = (int) ConvertPxToDp.convertDpToPixel(156, getContext());
                dogRegionBox.setLayoutParams(cityLayoutParams);

                cityGridview.setVisibility(View.VISIBLE);
                cityTextView.setBackgroundColor(Color.parseColor("#EDBC64"));
                cityTextView.setTextColor(Color.parseColor("#ffffffff"));
                districtGridView.setVisibility(View.INVISIBLE);
                districtTextView.setBackgroundColor(Color.parseColor("#ffffffff"));
                districtTextView.setTextColor(Color.parseColor("#3E3A39"));
                break;
            case R.id.district:
                ViewGroup.LayoutParams districtLayoutParams = dogRegionBox.getLayoutParams();
                districtLayoutParams.height = (int) ConvertPxToDp.convertDpToPixel(200, getContext());
                dogRegionBox.setLayoutParams(districtLayoutParams);

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
                inputManager.showSoftInput(dogPrice, 0);

                dogPrice.requestFocus();
                break;
            case R.id.price_selection:
                dogPriceTv.setText(dogPrice.getText().toString()+"만원");
                break;
            case R.id.prev_btn:
                previousRegistPage();
                break;
            case R.id.next_btn:
                nextRegistPage();
                break;
        }
    }

    private void previousRegistPage() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    private void nextRegistPage() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.container, PostRegist_75_Fragment.newInstance());
        fragmentTransaction.commit();
    }

//    private void setDogAgeSpinner() {
//        dogAgeSpinnerBox.setOnItemSelectedListener(this);
//
//        ArrayList<String> dogGenders = new ArrayList<>();
//        dogGenders.add("1개월");
//        dogGenders.add("2개월");
//        dogGenders.add("3개월");
//        dogGenders.add("4개월");
//        dogGenders.add("5개월");
//        dogGenders.add("6개월");
//        dogGenders.add("7개월");
//        dogGenders.add("8개월");
//        dogGenders.add("9개월");
//
//        DogAgeSpinnerAdapter adapter = new DogAgeSpinnerAdapter(dogGenders);
//        dogAgeSpinnerBox.setAdapter(adapter);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == GET_DOG_TYPE) {
            String dogType = data.getExtras().getString(SearchDogTypeActivity.DOGTYPE);

            dogTypeTv.setVisibility(View.VISIBLE);
            dogTypeTv.setText(dogType);
        }
    }


    //Dog age select Spinner
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }
//
//    //Dog age Select Adapter
//    private class DogAgeSpinnerAdapter extends BaseAdapter{
//        private ArrayList<String> dogGenders;
//
//        public DogAgeSpinnerAdapter(ArrayList<String> dogGenders) {
//            this.dogGenders = dogGenders;
//        }
//
//        @Override
//        public int getCount() {
//            return dogGenders.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View view, ViewGroup parent) {
//
//            if(view == null) {
//                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                view = inflater.inflate(R.layout.dog_age_spinner_item, null, false);
//            }
//
//            TextView ageTv = (TextView) view.findViewById(R.id.age);
//            ageTv.setText(dogGenders.get(position));
//
//            return view;
//        }
//    }
}
