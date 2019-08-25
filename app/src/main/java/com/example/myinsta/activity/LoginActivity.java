package com.example.myinsta.activity;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.EditText;
import android.widget.Toast;

import com.example.myinsta.R;
import com.example.myinsta.classes.MySharedPrefrence;
import com.example.myinsta.data.RetrofitClient;
import com.example.myinsta.model.JsonResponseModel;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private MaterialButton login, create;
    private EditText user, pass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(MySharedPrefrence.getInstance(this).getIsLogin()) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            LoginActivity.this.finish();
        }

        init();
    }


    private void init() {
        login = findViewById(R.id.login_login);
        create =findViewById(R.id.login_create);
        user = findViewById(R.id.login_username);
        pass = findViewById(R.id.login_password);
        user.requestFocus();

        Clicks();
    }

    private void Clicks() {
        login.setOnClickListener(v -> {

            String u=user.getText().toString();
            String p=pass.getText().toString();

            if(!u.isEmpty() && !p.isEmpty()) {

                RetrofitClient.getInstance(this).getApi().loginUser(u, p).enqueue(new Callback<JsonResponseModel>() {
                    @Override
                    public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            MySharedPrefrence.getInstance(LoginActivity.this).setIsLogin();
                            MySharedPrefrence.getInstance(LoginActivity.this).setUsername(u);
                            LoginActivity.this.finish();
                        }else
                            switch (response.code()) {
                                case 406:
                                    Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
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
            }else{
                if(user.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "Please enter username", Toast.LENGTH_SHORT).show();
                    user.requestFocus();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    pass.requestFocus();
                }
            }
        });


        create.setOnClickListener(v -> {startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

        });

    }

}
