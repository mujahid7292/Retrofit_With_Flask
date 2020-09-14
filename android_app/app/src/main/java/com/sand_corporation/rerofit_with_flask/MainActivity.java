package com.sand_corporation.rerofit_with_flask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sand_corporation.rerofit_with_flask.api.Api;
import com.sand_corporation.rerofit_with_flask.api.GithubClient;
import com.sand_corporation.rerofit_with_flask.api.model.GithubRepo;
import com.sand_corporation.rerofit_with_flask.api.model.User;
import com.sand_corporation.rerofit_with_flask.global.Common;
import com.sand_corporation.rerofit_with_flask.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Retrofit_Tutorial";
    private static final int REQUEST_CODE_FOR_GALLERY_ACCESS = 100;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = findViewById(R.id.image);
        requestAllPermission(this);
        checkHowManyPermissionAllowed(this);

        //1. Simple GET Request.
        //getGithubReposForUser();

        //2. Send Objects In Request Body.
        //SendObjectsInRequestBody();

        //3. Upload Files to Server
        getSingleImageFromFileExplorer();
    }

    private void getSingleImageFromFileExplorer() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE_FOR_GALLERY_ACCESS);
    }

    private void uploadFilesToServer(Intent data) {
        if(data != null) {
            Log.i(TAG, "Image Data Is Not NULL");
            Uri selectedImageUri = data.getData();
            Glide.with(this).load(selectedImageUri).into(img);
            Log.i(TAG,"ImageUri: " + selectedImageUri.toString());
            File file = FileUtils.getFile(this, selectedImageUri);

            // create RequestBody instance from file
            final RequestBody requestFile =
                    RequestBody.create(
                            MediaType.parse(Objects.requireNonNull(getContentResolver().getType(selectedImageUri))),
                            file
                    );

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData(
                            "file[]",
                            file.getName(),
                            requestFile
                    );

            // add another part within the multipart request
            String descriptionString = "hello, this is description speaking";
            RequestBody description =
                    RequestBody.create(
                            okhttp3.MultipartBody.FORM,
                            descriptionString);

            Api api = Common.getApi();
            Call<ResponseBody> call = api.uploadSingleFile(description, body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()){
                        Log.i(TAG,"Response is Successful");
                        Toast.makeText(MainActivity.this, "Image Upload Successful.", Toast.LENGTH_SHORT).show();
                    }else {
                        Log.i(TAG,"Response Failed");
                        Toast.makeText(MainActivity.this, "Image Upload Failed.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i(TAG,"Error: " + t.getMessage());
                }
            });

        }else {
            Log.i(TAG, "Image Data Is Null");
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "requestCode: " + requestCode + "\n" +
                "resultCode: " + resultCode);
        if (requestCode == REQUEST_CODE_FOR_GALLERY_ACCESS && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Image Received", Toast.LENGTH_SHORT).show();
            uploadFilesToServer(data);
        }else {
            Log.i(TAG, "onActivityResult Failed");
        }
    }

    //Request all the permission at once.
    public void requestAllPermission(Activity activity) {
        //In permissionNeeded Arraylist we will put the permission for which we did not get user
        //approval yet.
        ArrayList<String> permissionNeeded = new ArrayList<>();
        //In permissionsAvailable Arraylist we will put all the permissions which
        // is required by this application
        ArrayList<String> permissionsAvailable = new ArrayList<>();
        permissionsAvailable.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionsAvailable.add(Manifest.permission.INTERNET);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            permissionsAvailable.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }


        //Above we did not added camera permission. We will ask for camera permission
        //to user when we truly need it. Otherwise user will get angry.

        for (String permission : permissionsAvailable){
            if (ContextCompat.checkSelfPermission(activity,permission) !=
                    PackageManager.PERMISSION_GRANTED){
                //We will check each permission in permissionsAvailable Arraylist, whether user already
                //approved the permission or not.
                //Execution will come to this block if user did not approved the permission. So we will
                //add this specefic permission in our permissionNeeded Arraylist.
                permissionNeeded.add(permission);
            }
        }
        requestGroupPermission(permissionNeeded, activity);
        Log.i("Check", "Permission Needed: " + permissionNeeded.toString());
    }

    private final int Request_GROUP_PERMISSION = 10000;

    private void requestGroupPermission(ArrayList<String> permissions, Activity activity){
        String[]permissionList = new String[permissions.size()];
        permissions.toArray(permissionList);
        Log.i("Check","Permission List Size: " + permissions.size());
        if (permissions.size() >= 1){
            ActivityCompat.requestPermissions(activity,permissionList,
                    Request_GROUP_PERMISSION);
        }

    }


    public boolean checkHowManyPermissionAllowed(Context mContext) {
        boolean allPermissionAllowed = false;
        //In permissionNeeded Arraylist we will put the permission for which we did not get user
        //approval yet.
        ArrayList<String> permissionNeeded = new ArrayList<>();
        //In permissionsAvailable Arraylist we will put all the permissions which is required by this
        //application
        ArrayList<String> permissionsAvailable = new ArrayList<>();
        ArrayList<String> permissionsAllowed = new ArrayList<>();
        permissionsAvailable.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionsAvailable.add(Manifest.permission.INTERNET);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            permissionsAvailable.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        for (String permission : permissionsAvailable){
            if (ContextCompat.checkSelfPermission(mContext,permission) !=
                    PackageManager.PERMISSION_GRANTED){
                //We will check each permission in permissionsAvailable Arraylist, whether user already
                //approved the permission or not.
                //Execution will come to this block if user did not approved the permission. So we will
                //add this specific permission in our permissionNeeded Arraylist.
                permissionNeeded.add(permission);
            } else {
                permissionsAllowed.add(permission);
            }
        }
        if (permissionsAvailable.size() == permissionsAllowed.size()){
            allPermissionAllowed = true;
        } else {
            allPermissionAllowed = false;
        }
        Log.i(TAG, "Permission Needed: " + permissionNeeded.toString() +
                "\nPermission Allowed: " + permissionsAllowed.toString() +
                "\nPermission Allowed Length: " + permissionsAllowed.size());
        return allPermissionAllowed;
    }



}