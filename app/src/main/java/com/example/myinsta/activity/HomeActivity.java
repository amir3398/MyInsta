package com.example.myinsta.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myinsta.R;
import com.example.myinsta.classes.MySharedPrefrence;
import com.example.myinsta.adapter.PostAdapter;
import com.example.myinsta.data.RetrofitClient;
import com.example.myinsta.model.JsonResponseModel;
import com.example.myinsta.model.PostModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class HomeActivity extends AppCompatActivity {
    private ImageView exit,addPost,setting;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        getData();

    }

    private void init() {
        exit = findViewById(R.id.toolbar_exit);
        addPost = findViewById(R.id.toolbar_new);
        setting = findViewById(R.id.toolbar_setting);
        recyclerView = findViewById(R.id.home_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        onclicks();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        version();
    }

    private void version(){
        RetrofitClient.getInstance(this).getApi()
                .verify(MySharedPrefrence.getInstance(this).getUsername(),"android")
                .enqueue(new Callback<JsonResponseModel>() {
                    @Override
                    public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {
                        if(response.isSuccessful()){
                            try {
                                PackageInfo info=getPackageManager().getPackageInfo(getPackageName(),0);
                                int myVersion=info.versionCode;
                                if (myVersion<Integer.parseInt(response.body().getMessage())){
                                    Toast.makeText(HomeActivity.this, "new version is available", Toast.LENGTH_SHORT).show();
                                }
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<JsonResponseModel> call, Throwable t) {

                    }
                });

    }

    private void onclicks() {
        exit.setOnClickListener(v -> {
            MySharedPrefrence.getInstance(HomeActivity.this).clearSharedPrefrence();
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            HomeActivity.this.finish();
        });

        addPost.setOnClickListener(v->{
            if(checkPermissin()!=PackageManager.PERMISSION_GRANTED) {
                if(ActivityCompat.shouldShowRequestPermissionRationale
                        (HomeActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    showExplanation();
                }else if(!MySharedPrefrence.getInstance(HomeActivity.this).getWriteExternal()){
                    requestPermission();
                    MySharedPrefrence.getInstance(HomeActivity.this).setWriteExternal();
                }else{
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri= Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                    Toast.makeText(this, "please allowed permission", Toast.LENGTH_SHORT).show();

                }
            }else
                goToDialog();
        });

        setting.setOnClickListener(v->
                startActivity(new Intent(HomeActivity.this, SettingActivity.class)) );
    }

    private void showExplanation(){
        AlertDialog.Builder builder=new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("permission needed");
        builder.setMessage("please allow this permission");
        builder.setPositiveButton("allow",(a,b)->
                requestPermission());
        builder.setNegativeButton("dont allow",(a,b)->
                a.dismiss());
        builder.setCancelable(false);
        builder.show();
    }

    private  void getData(){
        RetrofitClient.getInstance(this).getApi().getPost().enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if(response.isSuccessful()) {
                    PostAdapter adapter = new PostAdapter(HomeActivity.this, response.body().getData());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {

            }
        });
    }

    private int checkPermissin(){
       return ActivityCompat.checkSelfPermission(HomeActivity.this, WRITE_EXTERNAL_STORAGE);
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(HomeActivity.this,new String[]{WRITE_EXTERNAL_STORAGE},123);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==123)
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                goToDialog();
    }

    private void goToDialog() {
        startActivity(new Intent(HomeActivity.this, NewPostActivity.class));

    }
}
