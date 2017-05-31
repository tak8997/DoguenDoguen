package org.tacademy.woof.doguendoguen.rest;

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
 * Created by Tak on 2017. 5. 26..
 */

public class RestGenerator {
    private static final String BAER_URL = "http://13.124.26.143:3000";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BAER_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();


    public static <S> S createService(Class<S> serviceClass) {
        if(!httpClient.interceptors().contains(loggingInterceptor)) {
            httpClient
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();

                            Request.Builder reqBuilder = original.newBuilder()
                                    .addHeader("Authorization", "")
                                    .addHeader("Accept", "application/json");

                            Request request = reqBuilder.build();
                            return chain.proceed(request);
                        }
                    })
                    .connectTimeout(15, TimeUnit.SECONDS);

            builder.client(httpClient.build());
            retrofit = builder.build();
        }

        return retrofit.create(serviceClass);
    }
}
