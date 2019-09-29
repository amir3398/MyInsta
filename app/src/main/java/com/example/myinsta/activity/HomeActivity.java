package com.example.myinsta.activity;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myinsta.R;
import com.example.myinsta.classes.MySharedPrefrence;
import com.example.myinsta.data.RetrofitClient;
import com.example.myinsta.fragment.FriendsFragment;
import com.example.myinsta.fragment.HomeFragment;
import com.example.myinsta.fragment.ProfileFragment;
import com.example.myinsta.fragment.SaveFragment;
import com.example.myinsta.model.JsonResponseModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().beginTransaction().add(R.id.home_container, new HomeFragment()).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.home_bottomnavigation);
        getSupportFragmentManager().beginTransaction().add(R.id.home_container, new HomeFragment()).commit();


        bottomNavigationView.setOnNavigationItemReselectedListener(menuItem -> {


        });

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.bn_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_container, new HomeFragment()).addToBackStack(null).commit();
                    break;
                case R.id.bn_friends:
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_container, new FriendsFragment()).addToBackStack(null).commit();
                    break;
                case R.id.bn_add:
                    startActivity(new Intent(HomeActivity.this, NewPostActivity.class));
                    break;
                case R.id.bn_save:
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_container, new SaveFragment()).addToBackStack(null).commit();
                    break;
                case R.id.bn_profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_container, new ProfileFragment()).addToBackStack(null).commit();
                    break;
            }


            return true;
        });

        version();
    }

    private void version() {
        RetrofitClient.getInstance(this).getApi()
                .verify(MySharedPrefrence.getInstance(this).getUsername(), "android")
                .enqueue(new Callback<JsonResponseModel>() {
                    @Override
                    public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {
                        if (response.isSuccessful()) {
                            try {
                                PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
                                int myVersion = info.versionCode;
                                if (myVersion < Integer.parseInt(response.body().getMessage())) {
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

}
