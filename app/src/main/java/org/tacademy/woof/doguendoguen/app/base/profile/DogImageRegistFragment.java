package org.tacademy.woof.doguendoguen.app.base.profile;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.tacademy.woof.doguendoguen.R;

import java.util.ArrayList;

public class DogImageRegistFragment extends Fragment {
    private static final String DOGIMAGE = "dogImage";

    public DogImageRegistFragment() {
    }

    public static DogImageRegistFragment newInstance(Bitmap dogImage) {
        DogImageRegistFragment fragment = new DogImageRegistFragment();
        Bundle args = new Bundle();
        args.putParcelable(DOGIMAGE, dogImage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dogImage = getArguments().getParcelable(DOGIMAGE);
        }
    }

    Bitmap dogImage;
    ImageView image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dog_image_regist, container, false);

        image = (ImageView) view.findViewById(R.id.dog_image);
        image.setImageBitmap(dogImage);

        return view;
    }

}
