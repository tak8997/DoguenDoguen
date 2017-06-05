package org.tacademy.woof.doguendoguen.app.home;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.tablayout)
    TabLayout tabLayout;

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

}