package com.example.myinsta.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myinsta.HomeActivity;
import com.example.myinsta.MainActivity;
import com.example.myinsta.R;
import com.example.myinsta.classes.MySharedPrefrence;
import com.example.myinsta.data.RetrofitClient;
import com.example.myinsta.model.JsonResponseModel;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    private MaterialButton login, create;
    private EditText user, pass;


    public LoginFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        init(v);

        return v;
    }

    private void init(View v) {
        login = v.findViewById(R.id.login_login);
        create = v.findViewById(R.id.login_create);
        user = v.findViewById(R.id.login_username);
        pass = v.findViewById(R.id.login_password);
        user.requestFocus();

        onClicks();
    }

    private void onClicks() {
        login.setOnClickListener(v -> {

            String u=user.getText().toString();
            String p=pass.getText().toString();

            if(!u.isEmpty() && !p.isEmpty()) {

                RetrofitClient.getInstance(getContext()).getApi().loginUser(u, p).enqueue(new Callback<JsonResponseModel>() {
                    @Override
                    public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Welcome", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), HomeActivity.class));
                            MySharedPrefrence.getInstance(getContext()).setIsLogin();
                            MySharedPrefrence.getInstance(getContext()).setUsername(u);
                            getActivity().finish();
                        }else
                            switch (response.code()) {
                                case 406:
                                    Toast.makeText(getContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                                    pass.requestFocus();
                                    pass.setText("");
                                    break;
                                case 400:
                                    Toast.makeText(getContext(), "Not found user", Toast.LENGTH_SHORT).show();
                                    user.requestFocus();
                                    user.setText("");
                                    pass.setText("");
                                    break;
                                default:
                                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                    }

                    @Override
                    public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                        Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();

                    }
                });
            }else{
                if(user.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please enter username", Toast.LENGTH_SHORT).show();
                    user.requestFocus();
                }
                else
                {
                    Toast.makeText(getContext(), "Please enter password", Toast.LENGTH_SHORT).show();
                    pass.requestFocus();
                }
            }
        });


        create.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container,new RegisterFragment()).addToBackStack(null)
                    .setCustomAnimations(R.anim.fade_in,R.anim.fade_out).commit();

        });

    }

}
