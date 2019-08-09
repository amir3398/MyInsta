package com.example.myinsta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myinsta.classes.MySharedPrefrence;
import com.example.myinsta.data.RetrofitClient;
import com.example.myinsta.fragment.LoginFragment;
import com.example.myinsta.model.JsonResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(MySharedPrefrence.getInstance(this).getIsLogin()) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            MainActivity.this.finish();
        }

       getSupportFragmentManager().beginTransaction().
               add(R.id.main_container,new LoginFragment()).commit();

    }

}
