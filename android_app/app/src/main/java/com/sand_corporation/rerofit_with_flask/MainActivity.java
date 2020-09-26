package com.sand_corporation.rerofit_with_flask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sand_corporation.rerofit_with_flask.api.Api;
import com.sand_corporation.rerofit_with_flask.api.GithubClient;
import com.sand_corporation.rerofit_with_flask.api.model.GithubRepo;
import com.sand_corporation.rerofit_with_flask.api.model.User;
import com.sand_corporation.rerofit_with_flask.api.utils.APIError;
import com.sand_corporation.rerofit_with_flask.api.utils.ErrorUtils;
import com.sand_corporation.rerofit_with_flask.global.Common;
import com.sand_corporation.rerofit_with_flask.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private static final int REQUEST_CODE_FOR_PICKING_SINGLE_IMAGE = 100;
    private static final int REQUEST_CODE_FOR_PICKING_MULTIPLE_IMAGES = 200;
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
        //getSingleImageFromFileExplorer();

        //4. Passing Multiple Parts Along a File with @PartMap
        //getSingleImageFromFileExplorer();

        //5. Upload Multiple Files to Server.
        //getMultipleImageFromFileExplorer();

        //6. Custom Request Headers
        //customRequestHeaders();

        //7. Dynamic Urls for Requests
        //dynamicUrlsForRequests();

        // 8. Download Files from Server
        //downloadFileFromServer1();
        //downloadFileFromServer2();

        // 9. Error Handling.
        errorHandlingInRetrofit();
    }

    // 9. Error Handling.
    private void errorHandlingInRetrofit() {
        Api api = Common.getApi();
        Call<ResponseBody>call = api.getSpecificError(
                403
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Log.i(TAG,"Response Is Successful.");
                    Toast.makeText(MainActivity.this,
                            "Response Is Successful.",
                            Toast.LENGTH_LONG)
                            .show();

                }else {
                    APIError error = ErrorUtils.parseError(response);

                    Log.i(TAG,"Response Failed: " + error.getMessage());
                    Toast.makeText(MainActivity.this,
                            "Response Failed: " + error.getMessage(),
                            Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG,"Error: " + t.getMessage());
                Toast.makeText(MainActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    // 8. Download Files from Server
    private boolean writeResponseBodyToDisk(ResponseBody body, String fileName) {

        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    fileName
            );

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.i(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private void downloadFileFromServer1() {
        final String fileName = "lena.jpg";

        Api api = Common.getApi();
        Call<ResponseBody> call = api.getFile(
                fileName
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Log.i(TAG,"Received File.");
                    Toast.makeText(MainActivity.this,
                            "Received File.",
                            Toast.LENGTH_LONG)
                            .show();
                    boolean writtenToDisk = writeResponseBodyToDisk(response.body(),fileName);

                    Log.i(TAG, "file download was a success? " + writtenToDisk);
                }else {
                    Log.i(TAG,"Response Failed");
                    Toast.makeText(MainActivity.this,
                            "Response Failed",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG,"Error: " + t.getMessage());
                Toast.makeText(MainActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void downloadFileFromServer2() {
        final String fileName = "lena.jpg";

        Api api = Common.getApi();
        Call<ResponseBody> call = api.getFileStream(
                fileName
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Log.i(TAG,"Received File.");
                    Toast.makeText(MainActivity.this,
                            "Received File.",
                            Toast.LENGTH_LONG)
                            .show();
                    boolean writtenToDisk = writeResponseBodyToDisk(response.body(),fileName);

                    Log.i(TAG, "file download was a success? " + writtenToDisk);
                }else {
                    Log.i(TAG,"Response Failed");
                    Toast.makeText(MainActivity.this,
                            "Response Failed",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG,"Error: " + t.getMessage());
                Toast.makeText(MainActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    //7. Dynamic Urls for Requests
    private void dynamicUrlsForRequests() {
        // My Github profile pic url.
        String profile_pic_url = "https://avatars3.githubusercontent.com/u/42904620?s=400&u=37ebcc83bfac93b312711469519c0043a2cceab6&v=4";
        Api api = Common.getApi();
        Call<ResponseBody> call = api.getProfilePic(
                profile_pic_url
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Log.i(TAG,"Received Profile Pic.");
                    Toast.makeText(MainActivity.this,
                            "Received Profile Pic.",
                            Toast.LENGTH_LONG)
                            .show();
                    writeResponseBodyToDisk(response.body(),"Github Profile.jpg");
                }else {
                    Log.i(TAG,"Response Failed");
                    Toast.makeText(MainActivity.this,
                            "Response Failed",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG,"Error: " + t.getMessage());
                Toast.makeText(MainActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    //6. Custom Request Headers
    private void customRequestHeaders() {
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
        //Call<User> call = api.customRequestHeaders(user);
        Call<User> call = api.customRequestHeaders(
                "mujahid7292",
                "max-age-4800",
                user
        );
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    User user1 = response.body();
                    Log.i(TAG,"User Id: " + user1.getId());
                    Toast.makeText(MainActivity.this,
                            "User Id: " + user1.getId(),
                            Toast.LENGTH_LONG)
                            .show();
                }else {
                    Log.i(TAG,"Response Failed");
                    Toast.makeText(MainActivity.this,
                            "Response Failed",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i(TAG,"Error: " + t.getMessage());
                Toast.makeText(MainActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getMultipleImageFromFileExplorer() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else.
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        // Always shows the chooser. (If there is multiple
        // options available)
        startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                REQUEST_CODE_FOR_PICKING_MULTIPLE_IMAGES
        );
    }

    private void getSingleImageFromFileExplorer() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE_FOR_PICKING_SINGLE_IMAGE);
    }

    //5. Upload Multiple Files to Server.
    private void uploadMultipleFiles(Intent data){
        ClipData clipData = data.getClipData();
        ArrayList<Uri> fileUris = new ArrayList<>();
        for(int i = 0; i < clipData.getItemCount(); i++){
            ClipData.Item item = clipData.getItemAt(i);
            Uri uri = item.getUri();
            fileUris.add(uri);
        }

        MultipartBody.Part file1 = prepareFilePart("file[]", fileUris.get(0));
        MultipartBody.Part file2 = prepareFilePart("file[]", fileUris.get(1));

        Api api = Common.getApi();
        Call<ResponseBody> call = api.uploadMultipleFiles(
                createPartFromString("This is description"),
                file1,
                file2
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Log.i(TAG,"Response is Successful");
                    Toast.makeText(MainActivity.this, "Images Upload Successful.", Toast.LENGTH_SHORT).show();
                }else {
                    Log.i(TAG,"Response Failed");
                    Toast.makeText(MainActivity.this, "Images Upload Failed.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG,"Error: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void uploadDynamicAmountOfFiles(Intent data){
        ClipData clipData = data.getClipData();
        ArrayList<Uri> fileUris = new ArrayList<>();
        for(int i = 0; i < clipData.getItemCount(); i++){
            ClipData.Item item = clipData.getItemAt(i);
            Uri uri = item.getUri();
            fileUris.add(uri);
        }

        List<MultipartBody.Part> files = new ArrayList<>();

        for(int i = 0; i < fileUris.size(); i++){
            files.add(prepareFilePart(
                    "file[]",
                    fileUris.get(i)
            ));
        }

        Api api = Common.getApi();
        Call<ResponseBody> call = api.uploadDynamicAmountOfFiles(
                createPartFromString("This is description."),
                files
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Log.i(TAG,"Response is Successful");
                    Toast.makeText(MainActivity.this, "Images Upload Successful.", Toast.LENGTH_SHORT).show();
                }else {
                    Log.i(TAG,"Response Failed");
                    Toast.makeText(MainActivity.this, "Images Upload Failed.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG,"Error: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //4. Passing Multiple Parts Along a File with @PartMap
    private void uploadSingleFileWithMultiParts(Intent data){
        if (data != null){
            Uri selectedFile = data.getData();
            Api api = Common.getApi();
            Call<ResponseBody>call = api.uploadSingleFileWithMultiParts(
                    createPartFromString("This is description"),
                    createPartFromString("Dhaka"),
                    createPartFromString("Saifullah Al Mujahid"),
                    createPartFromString("2020"),
                    prepareFilePart("file[]", selectedFile)
            );
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
        }
    }

    private void uploadSingleFileWithPartMap(Intent data){
        if (data != null){
            Uri selectedFile = data.getData();

            Map<String, RequestBody> map = new HashMap<>();
            map.put("Platform", createPartFromString("Android"));
            map.put("Country", createPartFromString("Bangladesh"));
            map.put("Time", createPartFromString("5:19 pm"));

            Api api = Common.getApi();
            Call<ResponseBody> call = api.uploadSingleFileWithPartMap(
                    map,
                    prepareFilePart("file[]", selectedFile)
            );
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
        }
    }

    // Helper method
    @NonNull
    private RequestBody createPartFromString(String str){
        return RequestBody.create(
                okhttp3.MultipartBody.FORM,
                str
        );
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri){
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(this, fileUri);

        // Create RequestBody instances from file.
        RequestBody requestFile = RequestBody.create(
                MediaType.parse(getContentResolver().getType(fileUri)),
                file
        );

        // MultipartBody.Part is used to send also
        // the actual file name.
        return MultipartBody.Part.createFormData(
                partName,
                file.getName(),
                requestFile
        );
    }

    //3. Upload Files to Server
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
        if (requestCode == REQUEST_CODE_FOR_PICKING_SINGLE_IMAGE
                && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Image Received", Toast.LENGTH_SHORT).show();
            //3. Upload Files to Server
            //uploadFilesToServer(data);
            //4. Passing Multiple Parts Along a File with @PartMap
            //uploadSingleFileWithMultiParts(data);
            //uploadSingleFileWithPartMap(data);
        }else if(requestCode == REQUEST_CODE_FOR_PICKING_MULTIPLE_IMAGES
                && resultCode == Activity.RESULT_OK
                && data != null){
            //5. Upload Multiple Files to Server.
            //uploadMultipleFiles(data);
            uploadDynamicAmountOfFiles(data);
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