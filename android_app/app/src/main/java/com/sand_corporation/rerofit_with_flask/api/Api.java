package com.sand_corporation.rerofit_with_flask.api;

import com.sand_corporation.rerofit_with_flask.api.model.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {

    @POST("/api/SendObjectsInRequestBody")
    Call<User> createAccount(@Body User user);

    @Multipart
    @POST("/api/upload-single-file")
    Call<ResponseBody> uploadSingleFile(
            @Part("File[]") RequestBody description,
            @Part MultipartBody.Part file
    );
}
