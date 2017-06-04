package org.tacademy.woof.doguendoguen.app.base.profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.tacademy.woof.doguendoguen.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostRegist_100_Fragment extends Fragment {

    public PostRegist_100_Fragment() {
    }

    public static PostRegist_100_Fragment newInstance() {
        PostRegist_100_Fragment fragment = new PostRegist_100_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @BindView(R.id.regist_btn) Button registBtn;
    @BindView(R.id.post_intro) EditText postIntro;
    @BindView(R.id.post_sub_intro) EditText postSubIntro;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_regist_100_, container, false);
        ButterKnife.bind(this, view);

        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        return view;
    }

}









