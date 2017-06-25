package org.tacademy.woof.doguendoguen.app.home;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by Tak on 2017. 6. 22..
 */

public class BaseActivity extends AppCompatActivity{
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
