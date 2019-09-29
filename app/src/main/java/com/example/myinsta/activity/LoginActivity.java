package com.example.myinsta.activity;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Toast;

import com.example.myinsta.R;
import com.example.myinsta.classes.MySharedPrefrence;
import com.example.myinsta.data.RetrofitClient;
import com.example.myinsta.model.JsonResponseModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private MaterialButton loginMaterialButton, create;
    private TextInputEditText user, pass;
    private TextInputLayout userL, passL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (MySharedPrefrence.getInstance(this).getIsLogin()) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            LoginActivity.this.finish();
        }

        init();
    }


    private void init() {
        loginMaterialButton = findViewById(R.id.login_login);
        create = findViewById(R.id.login_create);
        user = findViewById(R.id.login_username);
        pass = findViewById(R.id.login_password);
        userL = findViewById(R.id.login_username_layout);
        passL = findViewById(R.id.login_password_layout);
        user.requestFocus();

        Clicks();
    }

    private void Clicks() {
        loginMaterialButton.setOnClickListener(v -> {

            String u = user.getText().toString();
            String p = pass.getText().toString();

            if (u.isEmpty()) {
                userL.setError(getResources().getString(R.string.error_empty));
                user.requestFocus();
            } else {
                userL.setError("");
                if (p.isEmpty()) {
                    passL.setError(getResources().getString(R.string.error_empty));
                    pass.requestFocus();
                } else {
                    passL.setError("");
                    login(u, p);
                }
            }
        });


        create.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

        });

    }

    private void login(String u, String p) {
        RetrofitClient.getInstance(this).getApi().loginUser(u, p).enqueue(new Callback<JsonResponseModel>() {
            @Override
            public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this,  R.string.welcome, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    MySharedPrefrence.getInstance(LoginActivity.this).setIsLogin();
                    MySharedPrefrence.getInstance(LoginActivity.this).setUsername(u);
                    LoginActivity.this.finish();
                } else
                    switch (response.code()) {
                        case 406:
                            passL.setError(getResources().getString(R.string.wrong_password));
                            pass.requestFocus();
                            pass.setText("");
                            break;
                        case 400:
                            Toast.makeText(LoginActivity.this, "Not found user", Toast.LENGTH_SHORT).show();
                            user.requestFocus();
                            user.setText("");
                            pass.setText("");
                            break;
                        default:
                            Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Failure", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
