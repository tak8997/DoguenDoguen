package org.tacademy.woof.doguendoguen.app.base.profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.tacademy.woof.doguendoguen.R;

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

    Button prevBtn;
    Button nextRegBtn;
    Button registBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_regist_100_, container, false);

        prevBtn = (Button) getActivity().findViewById(R.id.prevBtn);
        nextRegBtn = (Button) getActivity().findViewById(R.id.prevBtn);
        registBtn = (Button) getActivity().findViewById(R.id.regist_btn);

        prevBtn.setVisibility(View.INVISIBLE);
        nextRegBtn.setVisibility(View.INVISIBLE);
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









