package org.tacademy.woof.doguendoguen.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.adapter.UserPostAdapter;

import butterknife.ButterKnife;

/**
 * Created by Tak on 2017. 5. 31..
 */

public class UserProfileDetailActivity extends AppCompatActivity{

    private RecyclerView userPostRecyclerview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_detail);
        ButterKnife.bind(this);

        userPostRecyclerview = (RecyclerView) findViewById(R.id.user_post_recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        UserPostAdapter userPostAdapter = new UserPostAdapter();
        userPostAdapter.addUserPost();

        userPostRecyclerview.setLayoutManager(layoutManager);
        userPostRecyclerview.setAdapter(userPostAdapter);

    }
}














