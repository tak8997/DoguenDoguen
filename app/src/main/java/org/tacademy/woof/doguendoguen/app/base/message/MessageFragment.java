package org.tacademy.woof.doguendoguen.app.base.message;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.adapter.MessageListsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageFragment extends Fragment {

    public MessageFragment() {
    }

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
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

    @BindView(R.id.message_recyclerview) RecyclerView messageRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);

        messageRecyclerView.setLayoutManager(new LinearLayoutManager(DoguenDoguenApplication.getContext()));

        MessageListsAdapter messageListsAdapter = new MessageListsAdapter(getContext());
        messageListsAdapter.addMessageList();
        messageListsAdapter.notifyDataSetChanged();

        messageRecyclerView.setAdapter(messageListsAdapter);


        return view;
    }

}
