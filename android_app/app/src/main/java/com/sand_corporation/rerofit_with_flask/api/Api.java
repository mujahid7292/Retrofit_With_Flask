package com.sand_corporation.rerofit_with_flask.api;

import com.sand_corporation.rerofit_with_flask.api.model.User;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

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

    @Multipart
    @POST("api/upload-dynamic-amount-of-files")
    Call<ResponseBody> uploadDynamicAmountOfFiles(
            @Part("Description") RequestBody description,
            @Part List<MultipartBody.Part> files
    );

    //6. Custom Request Headers

//    @Headers("Cache-Control: max-age-3600")
//    @POST("/api/custom-request-headers")
//    Call<User> customRequestHeaders(@Body User user);

    @Headers({
            "Cache-Control: max-age-3600",
            "User-Agent: Android"
    })
    @POST("/api/custom-request-headers")
    Call<User> customRequestHeaders(@Body User user);


    @Headers({
            "Cache-Control: max-age-3600",
            "User-Agent: Android"
    })
    @POST("/api/custom-request-headers")
    Call<User> customRequestHeaders(
            @Header("UserName") String userName,
            @Header("Cache-Control") String cache, //Same Headers
            @Body User user
    );

    //7. Dynamic Urls for Requests
    @GET
    Call<ResponseBody> getProfilePic(
            @Url String url
    );

    // 8. Download Files from Server
    @GET("/api/get-file/{file_name}")
    Call<ResponseBody> getFile(
            @Path("file_name") String file_name
    );

    @Streaming
    @GET("/api/get-file/{file_name}")
    Call<ResponseBody> getFileStream(
            @Path("file_name") String file_name
    );

    // 9. Error Handling.
    @GET("/api/error_handling/{error_code}")
    Call<ResponseBody> getSpecificError(
            @Path("error_code") int error_code
    );

    // 10. Send Data Form-Urlencoded
    @FormUrlEncoded
    @POST("/api/form-url-encoded")
    Call<ResponseBody> formUrlEncoded(
            @Field("full_name") String full_name,
            @Field("username") String username,
            @Field("password") String password,
            @Field("age") String age,
            @Field(value = "topics", encoded = true) List<String> topics
    );

    @FormUrlEncoded
    @POST("/api/form-url-encoded")
    Call<ResponseBody> formUrlEncoded(
            @FieldMap Map<String, Object> map
    );

    // 11. Send Plain Text Request Body
    @POST("/api/plain-text-request-body")
    Call<ResponseBody> sendPlainTextRequestBody(
            @Body String message
    );

    @POST("/api/plain-text-request-body")
    Call<ResponseBody> sendPlainTextRequestBody(
            @Body RequestBody message
    );

    // Send Query Parameters
    @GET("/api/query-parameters")
    Call<ResponseBody> sendQueryParameters(
            @Query("api_key") String api_key,
            @Query("characters") String characters,
            @Query("order") String order
    );

    // 12.Add Query Parameters to Every Request
    @GET("/api/query-parameters")
    Call<ResponseBody> addQueryParametersToEveryRequest(
            @Query("characters") String characters,
            @Query("order") String order
    );

    // 13. Basic Authentication
    @GET("/api/basic-authentication")
    Call<User> basicAuthentication(
            @Header("Authorization") String authHeader
    );

    // 14. Token Authentication.
    @GET("/api/token-authentication")
    Call<ResponseBody> tokenAuthentication(
            @Header("x-access-token") String token
    );
}
