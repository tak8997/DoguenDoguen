package org.tacademy.woof.doguendoguen.app.home;

import android.graphics.Color;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.app.base.message.MessageFragment;
import org.tacademy.woof.doguendoguen.app.base.profile.UserProfileFragment;
import org.tacademy.woof.doguendoguen.app.base.search.SearchFragment;
import org.tacademy.woof.doguendoguen.app.base.wish.WishFragment;

import java.util.logging.Handler;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.tablayout) TabLayout tabLayout;

    Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            selectedFragment = SearchFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, selectedFragment).commit();
        }

//        tabLayout.addTab(tabLayout.newTab().setText("새가족 찾기").setIcon(R.drawable.search));
//        tabLayout.addTab(tabLayout.newTab().setText("두근두근").setIcon(R.drawable.wish));
//        tabLayout.addTab(tabLayout.newTab().setText("메시지").setIcon(R.drawable.message));
//        tabLayout.addTab(tabLayout.newTab().setText("마이 페이지").setIcon(R.drawable.profile));
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
//                int position = tab.getPosition();

//                switch (position) {
//                    case 0:
//                        tabLayout.getTabAt(position).setIcon(R.drawable.search);
//                        break;
//                    case 1:
//                        tabLayout.getTabAt(position).setIcon(R.drawable.wish);
//                        break;
//                    case 2:
//                        tabLayout.getTabAt(position).setIcon(R.drawable.message);
//                        break;
//                    case 3:
//                        tabLayout.getTabAt(position).setIcon(R.drawable.profile);
//                        break;
//                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
