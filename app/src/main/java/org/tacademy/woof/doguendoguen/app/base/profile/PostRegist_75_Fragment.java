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

import static org.tacademy.woof.doguendoguen.R.id.nextRegBtn;
import static org.tacademy.woof.doguendoguen.R.id.prevBtn;

public class PostRegist_75_Fragment extends Fragment {


    public PostRegist_75_Fragment() {
    }
    public static PostRegist_75_Fragment newInstance() {
        PostRegist_75_Fragment fragment = new PostRegist_75_Fragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_regist_75_, container, false);


        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        prevBtn = (Button) getActivity().findViewById(R.id.prevBtn);
        prevBtn.setVisibility(View.VISIBLE);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.popBackStack();
            }
        });

        nextRegBtn = (Button) getActivity().findViewById(R.id.nextRegBtn);
        nextRegBtn.setVisibility(View.VISIBLE);
        nextRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, PostRegist_100_Fragment.newInstance());
                fragmentTransaction.commit();
            }
        });

        return view;
    }

}
