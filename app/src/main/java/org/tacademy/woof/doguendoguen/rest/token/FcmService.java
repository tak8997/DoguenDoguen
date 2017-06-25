package org.tacademy.woof.doguendoguen.rest.token;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by Tak on 2017. 6. 20..
 */

public interface FcmService {

    @POST
    Call<ResponseBody> putUserInfo(String fcmToken, String uuidValue);
}
