package com.example.myinsta.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myinsta.R;
import com.example.myinsta.classes.MySharedPrefrence;
import com.example.myinsta.data.RetrofitClient;
import com.example.myinsta.model.JsonResponseModel;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.button.MaterialButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Date;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class RegisterActivity extends AppCompatActivity {
    private EditText name, family, username, password, repeatPassword;
    private RadioButton male, famale;
    private MaterialButton create, cancel;
    private CircleImageView simpleUser;
    private TextView imgtext;

    private boolean checkImage=false;
    private Bitmap bitmap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        clicksButton();

    }


    private void init() {

        name = findViewById(R.id.register_name);
        family = findViewById(R.id.register_family);
        username = findViewById(R.id.register_username);
        password = findViewById(R.id.register_password);
        repeatPassword = findViewById(R.id.register_repeat_password);
        simpleUser = findViewById(R.id.register_simple_user);
        imgtext = findViewById(R.id.register_img_text);

        male = findViewById(R.id.register_gender1);
        famale = findViewById(R.id.register_gender2);

        create = findViewById(R.id.register_create);
        cancel = findViewById(R.id.register_cancel);

        name.requestFocus();

        clicksRadio();

    }

    private void clicksRadio() {

        male.setOnClickListener(v -> {
            male.setChecked(true);
            famale.setChecked(false);
        });

        famale.setOnClickListener(v -> {
            famale.setChecked(true);
            male.setChecked(false);
        });
    }

    private void clicksButton() {

        cancel.setOnClickListener(v->{
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });


        create.setOnClickListener(v -> {

            String picname = new SimpleDateFormat("_yyyyMMdd_hhmmss", Locale.ENGLISH).format(new Date());

            if (checkImage==false){
                Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show();
                return;
            }


            if (name.getText().toString().equals("")) {
                Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
                name.requestFocus();
            } else if (family.getText().toString().equals("")) {
                Toast.makeText(this, "Please enter family", Toast.LENGTH_SHORT).show();
                family.requestFocus();
            } else if (username.getText().toString().equals("")) {
                Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
                username.requestFocus();
            } else if (password.getText().toString().equals("")) {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
                password.requestFocus();
            } else if (repeatPassword.getText().toString().equals("")) {
                Toast.makeText(this, "Please enter repeat password", Toast.LENGTH_SHORT).show();
                repeatPassword.requestFocus();
            } else if (!password.getText().toString().equals(repeatPassword.getText().toString())) {
                Toast.makeText(this, "repeat password incorrect", Toast.LENGTH_SHORT).show();
                repeatPassword.requestFocus();
                repeatPassword.setText("");
            } else {
                String n = name.getText().toString();
                String f = family.getText().toString();
                String u = username.getText().toString();
                String p = password.getText().toString();
                String g;
                if (male.isChecked())
                    g = "male";
                else
                    g = "famale";
                RetrofitClient.getInstance(this).getApi().
                        registerUser(n, f, g, u, p,toBase64(bitmap),u).enqueue(new Callback<JsonResponseModel>() {
                    @Override
                    public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Register", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else if (response.code() == 409) {
                            Toast.makeText(RegisterActivity.this, "User exist", Toast.LENGTH_SHORT).show();
                            username.requestFocus();
                            username.setText("");
                        } else {
                            Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, "Failure", Toast.LENGTH_SHORT).show();

                    }
                });
            }


        });

        imgtext.setOnClickListener(v -> {

            if(checkPermissin()!= PackageManager.PERMISSION_GRANTED) {
                if(ActivityCompat.shouldShowRequestPermissionRationale
                        (RegisterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    showExplanation();
                }else if(!MySharedPrefrence.getInstance(RegisterActivity.this).getWriteExternal()){
                    requestPermission();
                    MySharedPrefrence.getInstance(RegisterActivity.this).setWriteExternal();
                }else{
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri= Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                    Toast.makeText(this, "please allowed permission", Toast.LENGTH_SHORT).show();

                }
            }else {


                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("image ");
                builder.setMessage("select image from gallery :");

                builder.setNegativeButton("gallery", (dialog, b) -> {
                    selectFromGallery();
                    dialog.dismiss();
                });
                builder.setNeutralButton("cancel", (dialog, b) -> {
                    dialog.dismiss();
                });
                builder.setCancelable(false);
                builder.show();
            }
        });

    }

    private String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, b);
        byte[] bytes = b.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void selectFromGallery() {

        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "select an image"), 123);
    }

    private int checkPermissin(){
        return ActivityCompat.checkSelfPermission(RegisterActivity.this, WRITE_EXTERNAL_STORAGE);
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(RegisterActivity.this,new String[]{WRITE_EXTERNAL_STORAGE},111);
    }

    private void showExplanation(){
        AlertDialog.Builder builder=new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("permission needed");
        builder.setMessage("please allow this permission");
        builder.setPositiveButton("allow",(a,b)->
                requestPermission());
        builder.setNegativeButton("dont allow",(a,b)->
                a.dismiss());
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK)
            if (requestCode == 123 && data != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert");
                builder.setMessage("are you sure?");
                builder.setPositiveButton("yes", (a, b) -> {

                    CropImage.activity(data.getData())
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1)
                            .setAutoZoomEnabled(true)
                            .setAllowRotation(true)
                            .setAllowCounterRotation(true)
                            .setAllowFlipping(true)
                            .setActivityTitle("بریدن عکس")
                            .setCropShape(CropImageView.CropShape.OVAL)
                            .setFixAspectRatio(true)
                            .setMaxCropResultSize(2000,2000)
                            .setMinCropResultSize(100,100)
                            .setBackgroundColor(getResources().getColor(R.color.colorMirror))
                            .setBorderLineColor(getResources().getColor(R.color.colorRed))
                            .setGuidelinesColor(getResources().getColor(R.color.colorGreenDark))
                            .setBorderCornerColor(getResources().getColor(R.color.colorBlue))

                            .start(this);


                });
                builder.setNegativeButton("select again", (dialog, b) -> {
                    selectFromGallery();
                    dialog.dismiss();
                });
                builder.setNeutralButton("cancel", (dialog, b) -> {
                    dialog.dismiss();
                });
                builder.setCancelable(false);
                builder.show();


            }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                    simpleUser.setImageBitmap(bitmap);
                    checkImage=true;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

}
