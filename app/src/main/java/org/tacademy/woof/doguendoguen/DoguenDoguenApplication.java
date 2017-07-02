package org.tacademy.woof.doguendoguen;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.tsengvn.typekit.Typekit;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by Tak on 2017. 5. 25..
 */

public class DoguenDoguenApplication extends Application {
    private static Context context;

    private static Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://13.124.26.143:3000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        Typekit.getInstance()
//                .addNormal(Typekit.createFromAsset(this, "NotoSansCJKkr-Regular.otf"))
//                .addBold(Typekit.createFromAsset(this, "NotoSansCJKkr-Bold.otf"))
//                .addCustom1(Typekit.createFromAsset(this, "NotoSansCJKkr-Medium.otf"));
        context = this;
    }

    public static Socket getSocket() {
        return mSocket;
    }
    public static Context getContext() {
        return context;
    }
}
