package org.tacademy.woof.doguendoguen.rest;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Tak on 2017. 6. 12..
 */

public class RestClient {
    private static RestClient instance;

    private static final String BAER_URL = "http://13.124.26.143:3000";

    private OkHttpClient okHttpClient;
    private Retrofit retrofit;

    public static <S> S createService(Class<S> service) {
        if(instance == null) {
            instance = new RestClient();
        }

        return instance.getService(service);
    }

    private RestClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        Request.Builder reqBuilder = original.newBuilder()
                                .addHeader("user_token", "21")
                                .addHeader("Accept", "application/json");

                        Request request = reqBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BAER_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private <S> S getService(Class<S> service) {
        return retrofit.create(service);
    }
}
