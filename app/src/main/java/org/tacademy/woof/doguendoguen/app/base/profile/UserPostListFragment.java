package org.tacademy.woof.doguendoguen.app.base.profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tacademy.woof.doguendoguen.R;

public class UserPostListFragment extends Fragment {

    public UserPostListFragment() {
    }

    public static UserPostListFragment newInstance() {
        UserPostListFragment fragment = new UserPostListFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_post_list, container, false);



        return view;
    }

}
