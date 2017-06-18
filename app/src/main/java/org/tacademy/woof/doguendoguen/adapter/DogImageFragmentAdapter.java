package org.tacademy.woof.doguendoguen.adapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import org.tacademy.woof.doguendoguen.app.base.profile.DogImageRegistFragment;
import org.tacademy.woof.doguendoguen.app.base.search.DogImagePostFragment;
import org.tacademy.woof.doguendoguen.model.DogImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tak on 2017. 6. 8..
 */

public class  DogImageFragmentAdapter extends FragmentPagerAdapter {
    ArrayList<Uri> dogImages = new ArrayList<>();   //PostRegist_25_Fragment -> 1
    ArrayList<DogImage> dogImageModels = new ArrayList<>(); //PostDetailActivity로 부터 -> 0
    int flag;

    public DogImageFragmentAdapter(FragmentManager fm, int flag) {
        super(fm);

        this.flag = flag;
    }

    @Override
    public Fragment getItem(int position) {
        if(flag == 1)
            return DogImageRegistFragment.newInstance(dogImages.get(position));
        else
            return DogImagePostFragment.newInstance(dogImageModels.get(position).dogImageUrl);
    }

    @Override
    public int getCount() {
        if(flag == 1)
            return dogImages.size();
        else
            return dogImageModels.size();
    }

    //PostRegist_25_Fragment
    public void addDogImage(Uri dogImageBitmap) {
        dogImages.add(dogImageBitmap);
    }

    //PostDetailActivity로 부터
    public void setDogImages(ArrayList<DogImage> dogImageModels) {
        this.dogImageModels = dogImageModels;
    }

//    public void clearAllImgaes() {
//        Log.d("DogImageFragmentAdapter", "clearAllImgaes");
//        for(int i=0; i<dogImages.size(); i++)
//            dogImages.remove(i);
//
//        this.notifyDataSetChanged();
//    }
}
