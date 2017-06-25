package org.tacademy.woof.doguendoguen;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.tsengvn.typekit.Typekit;

/**
 * Created by Tak on 2017. 5. 25..
 */

public class DoguenDoguenApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

//        Typekit.getInstance()
//                .addNormal(Typekit.createFromAsset(this, "NotoSansCJKkr-Regular.otf"))
//                .addBold(Typekit.createFromAsset(this, "NotoSansCJKkr-Bold.otf"))
//                .addCustom1(Typekit.createFromAsset(this, "NotoSansCJKkr-Medium.otf"));
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
