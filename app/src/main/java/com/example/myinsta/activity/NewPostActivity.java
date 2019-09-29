package com.example.myinsta.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.emoji.widget.EmojiEditText;

import com.example.myinsta.R;
import com.example.myinsta.classes.MySharedPrefrence;
import com.example.myinsta.data.RetrofitClient;
import com.example.myinsta.model.JsonResponseModel;
import com.google.android.material.button.MaterialButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;

public class NewPostActivity extends AppCompatActivity {
    private MaterialButton select, save, back;
    private EmojiEditText des;
    private ImageView img;
    private String path;
    private Uri path2;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newpost);

        init();
    }

    private void init() {
        select = findViewById(R.id.newpost_select);
        img = findViewById(R.id.newpost_img);
        save = findViewById(R.id.newpost_save);
        back = findViewById(R.id.newpost_back);
        des = findViewById(R.id.newpost_des);
        onclicks();
    }

    private void onclicks() {
        select.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(NewPostActivity.this);
            builder.setTitle("select image ");
            builder.setMessage("select image from :");
            builder.setPositiveButton("camera", (dialog, b) -> {


                if (checkPermissin() != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale
                            (NewPostActivity.this, CAMERA)) {
                        showExplanation();
                    } else if (!MySharedPrefrence.getInstance(NewPostActivity.this).getWriteExternal()) {
                        requestPermission();
                        MySharedPrefrence.getInstance(NewPostActivity.this).setWriteExternal();
                    } else {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        Toast.makeText(this, "please allowed permission", Toast.LENGTH_SHORT).show();

                    }
                } else {

                    selectFromCamera();
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("gallery", (dialog, b) -> {
                selectFromGallery();
                dialog.dismiss();
            });
            builder.setNeutralButton("cancel", (dialog, b) -> {
                dialog.dismiss();
            });
            builder.setCancelable(false);
            builder.show();
        });

        back.setOnClickListener(v -> {
            startActivity(new Intent(NewPostActivity.this,HomeActivity.class));
            NewPostActivity.this.finish();
        });

        save.setOnClickListener(v -> sendNewPost());
    }

    private File createFile() throws IOException {
        String date = new SimpleDateFormat("_yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());

        File f = File.createTempFile(MySharedPrefrence.getInstance(this)
                .getUsername() + date, ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        //path = f.getAbsolutePath();
        path2 = Uri.fromFile(f);

        return f;
    }

    private void selectFromGallery() {

        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "select an image"), 123);
    }

    private void selectFromCamera() {
        Intent i = new Intent();
        i.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            i.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.
                    getUriForFile(this, "com.example.myinsta.fileprovider", createFile()));
            startActivityForResult(i, 456);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, b);
        byte[] bytes = b.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void sendNewPost() {
        String picname = new SimpleDateFormat("_yyyyMMdd_hhmmss", Locale.ENGLISH).format(new Date());

        String u = MySharedPrefrence.getInstance(this).getUsername();

        //String d = des.getText().toString() + "";

        if (des.getText().toString().equals("")) {
            Toast.makeText(this, "complete the form", Toast.LENGTH_SHORT).show();
            return;
        }


        byte[] d = new byte[0];
        try {
            d = des.getText().toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        RetrofitClient.getInstance(this).getApi().newpost(u, toBase64(bitmap),
                u + picname, Base64.encodeToString(d, Base64.DEFAULT))
                .enqueue(new Callback<JsonResponseModel>() {
                    @Override
                    public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {

                        if (response.isSuccessful()) {
                            Toast.makeText(NewPostActivity.this, "inserted", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            Toast.makeText(NewPostActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                        Toast.makeText(NewPostActivity.this, "failure", Toast.LENGTH_SHORT).show();

                    }
                });
    }


    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewPostActivity.this);
        builder.setTitle("permission needed");
        builder.setMessage("please allow this permission");
        builder.setPositiveButton("allow", (a, b) ->
                requestPermission());
        builder.setNegativeButton("dont allow", (a, b) ->
                a.dismiss());
        builder.setCancelable(false);
        builder.show();
    }

    private int checkPermissin() {
        return ActivityCompat.checkSelfPermission(NewPostActivity.this, CAMERA);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(NewPostActivity.this, new String[]{CAMERA}, 456);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 456)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                selectFromCamera();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK)
            if (requestCode == 123 && data != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewPostActivity.this);
                builder.setTitle("Alert");
                builder.setMessage("are you sure?");
                builder.setPositiveButton("yes", (a, b) -> {

                    CropImage.activity(data.getData())
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .setAutoZoomEnabled(true)
                            .setAllowRotation(true)
                            .setAllowCounterRotation(true)
                            .setAllowFlipping(true)
                            .setActivityTitle("بریدن عکس")
                            .setCropShape(CropImageView.CropShape.RECTANGLE)
                            .setFixAspectRatio(true)
                            //.setMaxCropResultSize(2000,2000)
                            .setMinCropResultSize(100, 100)
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


            } else if (requestCode == 456) {
                img.setImageURI(path2);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path2);

                } catch (IOException e) {
                    // e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                    img.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
}
