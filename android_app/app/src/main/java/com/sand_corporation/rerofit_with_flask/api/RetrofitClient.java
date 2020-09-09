package com.sand_corporation.rerofit_with_flask.api;

import com.sand_corporation.rerofit_with_flask.BuildConfig;
import com.sand_corporation.rerofit_with_flask.global.Common;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    private static OkHttpClient.Builder getOkHttpBuilder(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (BuildConfig.DEBUG){
            builder.addInterceptor(interceptor);
        }
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                Request.Builder newRequest = request.newBuilder()
                        .header(
                                "x-access-token",
                                Common.token == null? "N/A":Common.token
                        );
                return chain.proceed(newRequest.build());
            }
        });
        return builder;
    };


    public static Retrofit getClient(String baseUrl){
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpBuilder().build())
                .build();
        return retrofit;
    };
}
