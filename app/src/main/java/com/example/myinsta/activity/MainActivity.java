package com.example.myinsta.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.myinsta.R;
import com.example.myinsta.classes.MySharedPrefrence;
import com.example.myinsta.fragment.LoginFragment;

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
