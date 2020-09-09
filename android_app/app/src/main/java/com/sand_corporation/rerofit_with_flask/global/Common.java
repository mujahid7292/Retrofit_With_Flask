package com.sand_corporation.rerofit_with_flask.global;

import com.sand_corporation.rerofit_with_flask.api.Api;
import com.sand_corporation.rerofit_with_flask.api.GithubClient;
import com.sand_corporation.rerofit_with_flask.api.RetrofitClient;

public class Common {
    public static String token="N/A";

    public static final String apiBaseUrl = "http://10.0.2.2:5000";
    public static Api getApi(){
        return RetrofitClient.getClient(apiBaseUrl).create(Api.class);
    }

    public static final String githubBaseUrl = "https://api.github.com/";

    public static GithubClient getGithubClient(){
        return RetrofitClient.getClient(githubBaseUrl).create(GithubClient.class);
    }
}
