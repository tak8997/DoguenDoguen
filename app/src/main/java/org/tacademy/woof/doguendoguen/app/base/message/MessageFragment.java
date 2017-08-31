package org.tacademy.woof.doguendoguen.app.base.message;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.adapter.MessageListsAdapter;
import org.tacademy.woof.doguendoguen.app.sign.LoginFragment;
import org.tacademy.woof.doguendoguen.model.ChattingRoom;
import org.tacademy.woof.doguendoguen.rest.RestClient;
import org.tacademy.woof.doguendoguen.rest.user.ChatService;
import org.tacademy.woof.doguendoguen.util.SharedPreferencesUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MessageFragment extends Fragment {
    private static final String USER_ID = "userId";

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
        Log.d("MessageFragment", "onCreate");
        if (getArguments() != null) {
        }

        Log.d(this.getClass().getSimpleName(), "onCreateView" + ", userId: " + userId);
    }
    MessageListsAdapter messageListsAdapter;
    String userId;

    @BindView(R.id.message_recyclerview) RecyclerView messageRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        userId = SharedPreferencesUtil.getInstance().getUserId();

        //로그인 되어있지 않으면
        if(userId == null) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.container, LoginFragment.newInstance());
            ft.commit();
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messageListsAdapter= new MessageListsAdapter(getContext());
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(DoguenDoguenApplication.getContext()));
        messageRecyclerView.setAdapter(messageListsAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(userId != null) {
            ChatService chatService = RestClient.createService(ChatService.class);
            Observable<List<ChattingRoom>> getChatRooms = chatService.getChatRoom();

            getChatRooms.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(chattingRooms -> {
                        messageListsAdapter.addMessageList(chattingRooms);
                        messageListsAdapter.notifyDataSetChanged();
                    }, Throwable::printStackTrace);
        }
    }
}
