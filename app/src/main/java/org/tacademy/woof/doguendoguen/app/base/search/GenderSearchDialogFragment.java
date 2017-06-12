package org.tacademy.woof.doguendoguen.app.base.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import org.tacademy.woof.doguendoguen.R;

import static android.R.attr.width;

/**
 * Created by Tak on 2017. 6. 12..
 */

public class GenderSearchDialogFragment extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_gender_search_dialog, container, false);


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        int width = getResources().getDimensionPixelSize(R.dimen.dialog_width);
//        int height = getResources().getDimensionPixelSize(R.dimen.dialog_height);
//        getDialog().getWindow().setLayout(width, height);
//        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
//        lp.gravity = Gravity.LEFT | Gravity.TOP;
//        lp.x = 100;
//        lp.y = 100;
//        getDialog().getWindow().setAttributes(lp);

    }


}
