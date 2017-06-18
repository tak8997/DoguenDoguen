package org.tacademy.woof.doguendoguen.app.base.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @BindView(R.id.female) TextView femmale;
    @BindView(R.id.male) TextView male;
    @BindView(R.id.any_gender) TextView anyGender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_gender_search_dialog, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick({R.id.female, R.id.male, R.id.any_gender})
    public void onGenderClicked(View view) {
        switch (view.getId()) {
            case R.id.female:
                if(listener != null)
                    listener.onAdapterItemClick(femmale.getText().toString());
                break;
            case R.id.male:
                if(listener != null)
                    listener.onAdapterItemClick(male.getText().toString());
                break;
            case R.id.any_gender:
                if(listener != null)
                    listener.onAdapterItemClick(anyGender.getText().toString());
                break;
        }
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    public interface OnAdapterItemClickLIstener {
        public void onAdapterItemClick(String gender);
    }

    OnAdapterItemClickLIstener listener;
    public void setOnAdapterItemClickListener(OnAdapterItemClickLIstener listener) {
        this.listener = listener;
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
