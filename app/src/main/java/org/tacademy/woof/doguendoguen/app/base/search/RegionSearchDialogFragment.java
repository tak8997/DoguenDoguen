package org.tacademy.woof.doguendoguen.app.base.search;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.adapter.RegionAdapter;
import org.tacademy.woof.doguendoguen.util.ConvertPxToDpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegionSearchDialogFragment extends DialogFragment {
    private static final int REGION_CITY = 10;
    private static final int REGION_DISTRICT = 20;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialogTheme);
    }

    public RegionSearchDialogFragment() {
    }

    @BindView(R.id.city) TextView cityTitle;
    @BindView(R.id.district) TextView districtTitle;
    @BindView(R.id.region_city_gridview) GridView cityGridView;
    @BindView(R.id.region_district_gridview) GridView districtGridView;
    @BindView(R.id.dog_region_layout) LinearLayout regionLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_region_search_dialog, container, false);
        ButterKnife.bind(this, view);

        setDistrictRegion();
        setCityRegion();

        return view;
    }

    @OnClick(R.id.dog_region)
    public void onRegionTitleClicked() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    @OnClick({R.id.city, R.id.district})
    public void onRegionClicked(View view) {
        switch (view.getId()) {
            case R.id.city:
                ViewGroup.LayoutParams cityLayoutParams = regionLayout.getLayoutParams();
                cityLayoutParams.height = (int) ConvertPxToDpUtil.convertDpToPixel(156, getContext());
                regionLayout.setLayoutParams(cityLayoutParams);

                cityGridView.setVisibility(View.VISIBLE);
                cityTitle.setBackgroundColor(Color.parseColor("#EDBC64"));
                cityTitle.setTextColor(Color.parseColor("#ffffffff"));
                districtGridView.setVisibility(View.INVISIBLE);
                districtTitle.setBackgroundColor(Color.parseColor("#ffffffff"));
                districtTitle.setTextColor(Color.parseColor("#3E3A39"));
                break;
            case R.id.district:
                ViewGroup.LayoutParams districtLayoutParams = regionLayout.getLayoutParams();
                districtLayoutParams.height = (int) ConvertPxToDpUtil.convertDpToPixel(200, getContext());
                regionLayout.setLayoutParams(districtLayoutParams);

                cityGridView.setVisibility(View.INVISIBLE);
                cityTitle.setBackgroundColor(Color.parseColor("#ffffffff"));
                cityTitle.setTextColor(Color.parseColor("#3E3A39"));
                districtGridView.setVisibility(View.VISIBLE);
                districtTitle.setBackgroundColor(Color.parseColor("#EDBC64"));
                districtTitle.setTextColor(Color.parseColor("#ffffffff"));
                break;
        }
    }

    public interface OnAdapterItemClickLIstener {
        public void onAdapterItemClick(String city, String district);
    }

    OnAdapterItemClickLIstener listener;
    public void setOnAdapterItemClickListener(OnAdapterItemClickLIstener listener) {
        this.listener = listener;
    }

    String district;
    String city;

    private void setCityRegion() {
        final RegionAdapter cityRegionAdapter = new RegionAdapter(REGION_CITY);
        cityRegionAdapter.setCityRegions();
        cityRegionAdapter.notifyDataSetChanged();
        cityRegionAdapter.setOnAdapterItemClickListener(new RegionAdapter.OnAdapterItemClickLIstener() {
            @Override
            public void onAdapterItemClick(int position) {
                city = (String) cityRegionAdapter.getItem(position);

                ViewGroup.LayoutParams districtLayoutParams = regionLayout.getLayoutParams();
                districtLayoutParams.height = (int) ConvertPxToDpUtil.convertDpToPixel(200, getContext());
                regionLayout.setLayoutParams(districtLayoutParams);

                districtGridView.setVisibility(View.VISIBLE);
                districtTitle.setBackgroundColor(Color.parseColor("#EDBC64"));
                districtTitle.setTextColor(Color.parseColor("#ffffffff"));
                cityGridView.setVisibility(View.INVISIBLE);
                cityTitle.setBackgroundColor(Color.parseColor("#ffffffff"));
                cityTitle.setTextColor(Color.parseColor("#3E3A39"));
            }
        });
        cityGridView.setAdapter(cityRegionAdapter);
    }

    private void setDistrictRegion() {
        final RegionAdapter districtRegionAdatper = new RegionAdapter(REGION_DISTRICT);
        districtRegionAdatper.setDistrictRegions();
        districtRegionAdatper.notifyDataSetChanged();
        districtRegionAdatper.setOnAdapterItemClickListener(new RegionAdapter.OnAdapterItemClickLIstener() {
            @Override
            public void onAdapterItemClick(int position) {
                district = (String) districtRegionAdatper.getItem(position);

                if(listener != null)
                    listener.onAdapterItemClick(city, district);

                finishFragment();
            }
        });
        districtGridView.setAdapter(districtRegionAdatper);
    }

    private void finishFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }


}
