package org.tacademy.woof.doguendoguen;

import android.app.Application;
import android.content.Context;

/**
 * Created by Tak on 2017. 5. 25..
 */

public class DoguenDoguenApplication extends Application {
    private static Context context;

    public static int isImageDeleted = 0;
    public static int isParentImageDeleted = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
