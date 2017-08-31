package org.tacademy.woof.doguendoguen;

import android.app.Application;
import android.content.Context;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;



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
        context = this;

    }

    public static Socket getSocket() {
        return mSocket;
    }
    public static Context getContext() {
        return context;
    }
}
