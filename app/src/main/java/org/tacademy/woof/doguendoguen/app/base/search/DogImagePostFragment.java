package org.tacademy.woof.doguendoguen.app.base.search;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.model.DogImage;

import static android.content.ContentValues.TAG;

public class DogImagePostFragment extends Fragment {
    private static final String TAG = "DogImagePostFragment";
    private static final String DOG_IMAGE = "dogImage";

    public DogImagePostFragment() {
    }

    public static Fragment newInstance(String dogImage) {
        DogImagePostFragment fragment = new DogImagePostFragment();
        Bundle args = new Bundle();
        args.putString(DOG_IMAGE, dogImage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dogImageUrl = getArguments().getString(DOG_IMAGE);
        }
    }

    String dogImageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dog_image_post, container, false);

        Log.d(TAG, "viewCreated");

        ImageView dogImage = (ImageView) view.findViewById(R.id.dog_image);

        Glide.with(DoguenDoguenApplication.getContext())
                .load(dogImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(dogImage);

        return view;
    }


}
