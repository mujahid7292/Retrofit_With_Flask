package com.sand_corporation.rerofit_with_flask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.sand_corporation.rerofit_with_flask.api.Api;
import com.sand_corporation.rerofit_with_flask.api.GithubClient;
import com.sand_corporation.rerofit_with_flask.api.model.GithubRepo;
import com.sand_corporation.rerofit_with_flask.api.model.User;
import com.sand_corporation.rerofit_with_flask.global.Common;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Retrofit_Tutorial";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1. Simple GET Request.
        //getGithubReposForUser();

        //2. Send Objects In Request Body.
        SendObjectsInRequestBody();
    }

    private void SendObjectsInRequestBody() {
        // To be sent json body
//        {
//            "name": "Saifullah Al Mujahid",
//             "email": "mujahid7292@gmail.com",
//             "age":32,
//             "topics": [
//                 "android",
//                 "python"
//             ]
//        }

        String name = "Saifullah Al Mujahid";
        String email = "mujahid7292@gmail.com";
        int age = 32;
        String topics[] = "android,python".split(",");

        final User user = new User(name, email, age, topics);

        Api api = Common.getApi();
        Call<User>call = api.createAccount(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user1 = response.body();
                Log.i(TAG,"User Id: " + user1.getId());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void getGithubReposForUser() {
        GithubClient client = Common.getGithubClient();
        Call<List<GithubRepo>>call = client.reposForUser("mujahid7292");
        call.enqueue(new Callback<List<GithubRepo>>() {
            @Override
            public void onResponse(Call<List<GithubRepo>> call, Response<List<GithubRepo>> response) {
                List<GithubRepo> githubRepos  = response.body();
                for (int i = 0; i< githubRepos.size(); i++){
                    GithubRepo singleRepo = githubRepos.get(i);
                    Log.i(TAG,"Repo Name: " + singleRepo.getName());
                }
            }

            @Override
            public void onFailure(Call<List<GithubRepo>> call, Throwable t) {

            }
        });
    }
}