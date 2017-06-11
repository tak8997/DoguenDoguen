package org.tacademy.woof.doguendoguen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;

import java.util.ArrayList;

/**
 * Created by Tak on 2017. 6. 11..
 */

public class RegionAdapter extends BaseAdapter {
    ArrayList<String> cityRegions = new ArrayList<>();
    ArrayList<String> districtRegions = new ArrayList<>();
    int regionType;

    public RegionAdapter(int regionType) {
        this.regionType = regionType;
    }

    @Override
    public int getCount() {
        if(regionType == 10) {
            return cityRegions.size();
        } else
            return districtRegions.size();
    }

    @Override
    public Object getItem(int position) {
        if(regionType == 10) {
            return cityRegions.get(position);
        } else
            return districtRegions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView regionCity;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.city_region_item, null, false);
        }

        regionCity = (TextView) convertView.findViewById(R.id.city_region);
        regionCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onAdapterItemClick(position);
            }
        });
        if(regionType == 10)
            regionCity.setText(cityRegions.get(position));
        else
            regionCity.setText(districtRegions.get(position));

        return convertView;
    }

    public interface OnAdapterItemClickLIstener {
        public void onAdapterItemClick(int position);
    }

    OnAdapterItemClickLIstener listener;
    public void setOnAdapterItemClickListener(OnAdapterItemClickLIstener listener) {
        this.listener = listener;
    }

    public void setCityRegions() {
        cityRegions.add("서울");cityRegions.add("경기");
        cityRegions.add("인천");cityRegions.add("강원");
        cityRegions.add("대전");cityRegions.add("충북");
        cityRegions.add("충남");cityRegions.add("부산");
        cityRegions.add("울산");cityRegions.add("경남");
        cityRegions.add("경북");cityRegions.add("대구");
        cityRegions.add("광주");cityRegions.add("전남");
        cityRegions.add("전북");cityRegions.add("제주");
        cityRegions.add("전국");
    }

    public void setDistrictRegions() {
        districtRegions.add("강남구");districtRegions.add("강북구");
        districtRegions.add("강서구");districtRegions.add("강동구");
        districtRegions.add("관악구");districtRegions.add("광진구");
        districtRegions.add("구로구");districtRegions.add("금천구");
        districtRegions.add("노원구");districtRegions.add("도봉구");
        districtRegions.add("동대문구");districtRegions.add("동작구");
        districtRegions.add("마포구");districtRegions.add("서대문구");
        districtRegions.add("서초구");districtRegions.add("성동구");
        districtRegions.add("성북구");districtRegions.add("송파구");
        districtRegions.add("양천구");districtRegions.add("영등포구");
        districtRegions.add("용산구");districtRegions.add("은평구");
        districtRegions.add("종로구");districtRegions.add("중랑구");
        districtRegions.add("중구");
    }
}
