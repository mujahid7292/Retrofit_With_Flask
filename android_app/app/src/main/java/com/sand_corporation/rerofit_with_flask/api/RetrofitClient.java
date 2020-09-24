package com.sand_corporation.rerofit_with_flask.api;

import com.sand_corporation.rerofit_with_flask.BuildConfig;
import com.sand_corporation.rerofit_with_flask.global.Common;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Header;

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

                // If we want to pass single header
//                Request.Builder newRequest = request.newBuilder()
//                        .header(
//                                "X-Access-Token",
//                                Common.token == null? "N/A":Common.token
//                        );

                // If we want to pass multiple headers.
                Request.Builder newRequest = request.newBuilder()
                        .header("Header_1","1")
                        .header("Header_2","2")
                        .header("Header_3","3")
                        .header("Authorization",Common.token == null? "N/A":Common.token)
                        .addHeader("Cache-Control","max-age-2400");
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
