package com.example.myinsta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.example.myinsta.classes.MySharedPrefrence;
import com.example.myinsta.classes.NewPostDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class HomeActivity extends AppCompatActivity {
    private MaterialButton exit;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        clicksExit();
    }

    private void clicksExit() {
        exit.setOnClickListener(v -> {
            MySharedPrefrence.getInstance(HomeActivity.this).clearSharedPrefrence();
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            HomeActivity.this.finish();
        });

        fab.setOnClickListener(v->{
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

    private void init() {
        exit = findViewById(R.id.home_exit);
        fab = findViewById(R.id.home_fab);
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

    }
}
