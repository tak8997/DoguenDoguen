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

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostRegist_50_Fragment extends Fragment {
    public PostRegist_50_Fragment() {
    }

    public static PostRegist_50_Fragment newInstance() {
        PostRegist_50_Fragment fragment = new PostRegist_50_Fragment();
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

    @BindView(R.id.next_btn) Button nextBtn;
    @BindView(R.id.prev_btn) Button prevBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_regist_50_, container, false);
        ButterKnife.bind(this, view);

        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.popBackStack();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, PostRegist_75_Fragment.newInstance());
                fragmentTransaction.commit();
            }
        });

        return view;
    }

}
