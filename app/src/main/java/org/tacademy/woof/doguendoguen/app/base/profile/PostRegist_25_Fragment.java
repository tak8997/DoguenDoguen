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

public class PostRegist_25_Fragment extends Fragment {

    public PostRegist_25_Fragment() {
    }

    public static PostRegist_25_Fragment newInstance() {
        PostRegist_25_Fragment fragment = new PostRegist_25_Fragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_regist_25_, container, false);
        ButterKnife.bind(this, view);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, PostRegist_50_Fragment.newInstance());
                fragmentTransaction.commit();
            }
        });

        return view;
    }

}






