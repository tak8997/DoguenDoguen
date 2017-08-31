package org.tacademy.woof.doguendoguen.app.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.app.base.BaseActivity;
import org.tacademy.woof.doguendoguen.app.base.message.MessageFragment;
import org.tacademy.woof.doguendoguen.app.base.profile.UserProfileFragment;
import org.tacademy.woof.doguendoguen.app.base.search.SearchFragment;
import org.tacademy.woof.doguendoguen.app.base.wish.WishFragment;
import org.tacademy.woof.doguendoguen.util.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {
    private static final String TAG = "HomeActivity";

    @BindView(R.id.tablayout) TabLayout tabLayout;

    private Fragment selectedFragment;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        init();

        if (savedInstanceState == null) {
            selectedFragment = SearchFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, selectedFragment).commit();
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                switch (position) {
                    case 0:
                        selectedFragment = SearchFragment.newInstance();
                        break;
                    case 1:
                        selectedFragment = WishFragment.newInstance();
                        break;
                    case 2:
                        selectedFragment = MessageFragment.newInstance();
                        break;
                    case 3:
                        selectedFragment = UserProfileFragment.newInstance();
                        break;
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, selectedFragment);
                fragmentTransaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        userId = SharedPreferencesUtil.getInstance().getUserId();
    }

    private void init() {
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        SharedPreferencesUtil.getInstance().setUserId(userId);

        Log.d(TAG, userId+" /kakao");
    }
    long pressedTime;
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {

            if (pressedTime == 0) {
                Toast.makeText(HomeActivity.this, " 한 번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show();
                pressedTime = System.currentTimeMillis();
            } else {
                int seconds = (int) (System.currentTimeMillis() - pressedTime);

                if (seconds > 2000) {
                    Toast.makeText(HomeActivity.this, " 한 번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show();
                    pressedTime = 0;
                } else {

                    super.onBackPressed();
                }
            }
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}
