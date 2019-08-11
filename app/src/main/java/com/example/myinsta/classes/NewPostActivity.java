package com.example.myinsta.classes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myinsta.HomeActivity;
import com.example.myinsta.R;
import com.google.android.material.button.MaterialButton;

public class NewPostActivity extends AppCompatActivity {
    private MaterialButton select;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newpost);

        select = findViewById(R.id.newpost_select);
        img = findViewById(R.id.newpost_img);


        select.setOnClickListener(v -> {
            selectFromGallery();
        });
    }

    private void selectFromGallery() {

        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "select an app"), 123);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK)
            if (requestCode == 123 && data != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewPostActivity.this);
                builder.setTitle("Alert");
                builder.setMessage("are you sure?");
                builder.setPositiveButton("yes", (a, b) ->
                        img.setImageURI(data.getData()));
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
    }
}
