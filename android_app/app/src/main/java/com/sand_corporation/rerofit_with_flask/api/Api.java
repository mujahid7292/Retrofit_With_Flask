package com.sand_corporation.rerofit_with_flask.api;

import com.sand_corporation.rerofit_with_flask.api.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {

    @POST("/api/SendObjectsInRequestBody")
    Call<User> createAccount(@Body User user);
}
