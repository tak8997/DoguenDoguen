package org.tacademy.woof.doguendoguen.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Tak on 2017. 6. 13..
 */

public class SharedPreferencesUtil {
    private static SharedPreferencesUtil instance;

    public static SharedPreferencesUtil getInstance() {
        if(instance == null)
            instance = new SharedPreferencesUtil();

        return instance;
    }

    SharedPreferences mPref;
    SharedPreferences.Editor mEditor;

    private SharedPreferencesUtil() {
        mPref = PreferenceManager.getDefaultSharedPreferences(org.tacademy.woof.doguendoguen.DoguenDoguenApplication.getContext());
        mEditor = mPref.edit();
    }

    public static final String USER_ID = "userId";

    public void setUserId(String userId) {
        mEditor.putString(USER_ID, userId);
        mEditor.commit();
    }

    public String getUserId() {
        return mPref.getString(USER_ID, null);
    }

    public void clear() {
        mEditor.clear();
        mEditor.commit();
    }
}
