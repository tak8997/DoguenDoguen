package org.tacademy.woof.doguendoguen.app.base.profile;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;

public class UserProfileFragment extends Fragment {
    public UserProfileFragment() {
    }

    public static UserProfileFragment newInstance() {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    ImageView userImage;
    TextView postRegist;
    TextView userPostList;
    DrawerLayout registDrawer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        registDrawer = (DrawerLayout) view.findViewById(R.id.registDrawer);

        //유저 프로필 확인
        userImage = (ImageView) view.findViewById(R.id.profile_user_image);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoguenDoguenApplication.getContext(), UserProfileDetailActivity.class);
                startActivity(intent);
            }
        });

        //분양글 등록하기
        postRegist = (TextView) view.findViewById(R.id.post_regist);
        postRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, PostRegist_25_Fragment.newInstance());
                fragmentTransaction.commit();
            }
        });


        //내가 쓴 글 목록
        userPostList = (TextView) view.findViewById(R.id.user_post_list);
        userPostList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, UserPostListFragment.newInstance());
                fragmentTransaction.commit();
            }
        });


        return view;
    }


}
