package com.sand_corporation.rerofit_with_flask.api;

import com.sand_corporation.rerofit_with_flask.api.model.User;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface Api {

    @POST("/api/SendObjectsInRequestBody")
    Call<User> createAccount(@Body User user);

    //3. Upload Files to Server
    @Multipart
    @POST("/api/upload-single-file")
    Call<ResponseBody> uploadSingleFile(
            @Part("Description") RequestBody description,
            @Part MultipartBody.Part file
    );

    //4. Passing Multiple Parts Along a File with @PartMap
    @Multipart
    @POST("/api/upload-single-file-with-multi-parts")
    Call<ResponseBody> uploadSingleFileWithMultiParts(
            @Part("Description") RequestBody description,
            @Part("Location") RequestBody location,
            @Part("Photographer") RequestBody photographer,
            @Part("Year") RequestBody year,
            @Part MultipartBody.Part file
    );

    @Multipart
    @POST("/api/upload-single-file-with-part-map")
    Call<ResponseBody> uploadSingleFileWithPartMap(
            @PartMap()Map<String, RequestBody> data,
            @Part MultipartBody.Part file
    );

    //5. Upload Multiple Files to Server.
    @Multipart
    @POST("api/upload-multiple-files")
    Call<ResponseBody> uploadMultipleFiles(
            @Part("Description") RequestBody description,
            @Part MultipartBody.Part file1,
            @Part MultipartBody.Part file2
    );
}
