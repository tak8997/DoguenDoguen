package org.tacademy.woof.doguendoguen.adapter;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import org.tacademy.woof.doguendoguen.app.base.profile.DogImageRegistFragment;

import java.util.ArrayList;

/**
 * Created by Tak on 2017. 6. 8..
 */

public class  DogImageFragmentAdapter extends FragmentPagerAdapter {
    ArrayList<Bitmap> dogImages = new ArrayList<>();

    public DogImageFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return DogImageRegistFragment.newInstance(dogImages.get(position));
    }

    @Override
    public int getCount() {
        return dogImages.size();
    }

    public void addDogImage(Bitmap dogImageBitmap) {
        dogImages.add(dogImageBitmap);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);

        dogImages.remove(position);
    }

    public void clearAllImgaes() {
        Log.d("DogImageFragmentAdapter", "clearAllImgaes");
        for(int i=0; i<dogImages.size(); i++)
            dogImages.remove(i);

        this.notifyDataSetChanged();
    }
}
