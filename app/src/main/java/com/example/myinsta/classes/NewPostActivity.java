package com.example.myinsta.classes;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.myinsta.HomeActivity;
import com.example.myinsta.R;
import com.example.myinsta.data.RetrofitClient;
import com.example.myinsta.model.JsonResponseModel;
import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPostActivity extends AppCompatActivity {
    private MaterialButton select;
    private ImageView img;
    private String path;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newpost);

        select = findViewById(R.id.newpost_select);
        img = findViewById(R.id.newpost_img);


        select.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(NewPostActivity.this);
            builder.setTitle("select image :");
            builder.setMessage("select image from :");
            builder.setPositiveButton("camera", (dialog, b) ->{
                        selectFromCamera();
                        dialog.dismiss();
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
    }



    private File createFile() throws IOException {
        String date=new SimpleDateFormat("_yyyymmdd_hhMMss", Locale.ENGLISH).format(new Date());

        File f= (File) File.createTempFile(MySharedPrefrence.getInstance(this)
                .getUsername()+date, ".jpg",getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        path=f.getAbsolutePath();

        return  f;
    }

    private void selectFromGallery() {

        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "select an app"), 123);
    }

    private void selectFromCamera() {
        Intent i=new Intent();
        i.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            i.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.
                    getUriForFile(this,"com.example.android.fileprovider",createFile()));
            startActivityForResult(i,456);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String toBase64(Bitmap bitmap){
        ByteArrayOutputStream b=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,70,b);
        byte[] bytes=b.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }

    private void sendNewPost(){
        String picname=new SimpleDateFormat("_yyyymmdd_hhMMss", Locale.ENGLISH).format(new Date());


        RetrofitClient.getInstance(this).getApi().newpost(toBase64(bitmap),
                MySharedPrefrence.getInstance(this).getUsername()+picname)
                .enqueue(new Callback<JsonResponseModel>() {
            @Override
            public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {

                if(response.isSuccessful())
                    Toast.makeText(NewPostActivity.this, "inserted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(NewPostActivity.this, "error1", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                Toast.makeText(NewPostActivity.this, "failure", Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK)
            if (requestCode == 123 && data != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewPostActivity.this);
                builder.setTitle("Alert");
                builder.setMessage("are you sure?");
                builder.setPositiveButton("yes", (a, b) ->{
                        img.setImageURI(data.getData());
                    try {
                        bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                        sendNewPost();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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


            }else if(requestCode == 123){
                img.setImageURI(Uri.parse(path));
                try {
                    bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),Uri.parse(path));
                    sendNewPost();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

    }
}
