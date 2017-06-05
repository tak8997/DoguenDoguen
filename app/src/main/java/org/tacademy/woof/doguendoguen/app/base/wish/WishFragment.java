package org.tacademy.woof.doguendoguen.app.base.wish;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.adapter.DogListsAdapter;
import org.tacademy.woof.doguendoguen.model.PostListModel;

import java.util.ArrayList;

public class WishFragment extends Fragment {
    public WishFragment() {
    }

    public static WishFragment newInstance() {
        WishFragment fragment = new WishFragment();
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
        View view = inflater.inflate(R.layout.fragment_wish, container, false);

        RecyclerView wishLists = (RecyclerView) view.findViewById(R.id.wish_recyclerview);
        wishLists.setLayoutManager(new LinearLayoutManager(DoguenDoguenApplication.getContext()));

        ArrayList<PostListModel> postListModels = new ArrayList<>();
        PostListModel postListModel = new PostListModel(0, "가정에서 태어난 건강한 웰시코기 친구", "default", "코기엄마", 0);
        postListModels.add(postListModel);

        DogListsAdapter dogListsAdapter = new DogListsAdapter();
        wishLists.setAdapter(dogListsAdapter);

        dogListsAdapter.addPostList(postListModels);
        dogListsAdapter.notifyDataSetChanged();

        return view;
    }

}