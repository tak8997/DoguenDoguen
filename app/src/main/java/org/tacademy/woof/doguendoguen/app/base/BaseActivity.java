package org.tacademy.woof.doguendoguen.app.base;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;

import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by Tak on 2017. 6. 22..
 */

public class BaseActivity extends AppCompatActivity implements NestedScrollView.OnScrollChangeListener {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {}
}
