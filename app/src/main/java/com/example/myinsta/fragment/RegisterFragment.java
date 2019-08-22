package com.example.myinsta.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.myinsta.R;
import com.example.myinsta.data.RetrofitClient;
import com.example.myinsta.model.JsonResponseModel;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterFragment extends Fragment {
    private EditText name, family, username, password, repeatPassword;
    private RadioButton male, famale;
    private MaterialButton create, cancel;


    public RegisterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        init(v);

        clicksButton();


        return v;
    }


    private void init(View v) {

        name = v.findViewById(R.id.register_name);
        family = v.findViewById(R.id.register_family);
        username = v.findViewById(R.id.register_username);
        password = v.findViewById(R.id.register_password);
        repeatPassword = v.findViewById(R.id.register_repeat_password);

        male = v.findViewById(R.id.register_gender1);
        famale = v.findViewById(R.id.register_gender2);

        create = v.findViewById(R.id.register_create);
        cancel = v.findViewById(R.id.register_cancel);

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

        cancel.setOnClickListener(v ->
                getActivity().getSupportFragmentManager().popBackStackImmediate());

        create.setOnClickListener(v -> {

            if (name.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Please enter name", Toast.LENGTH_SHORT).show();
                name.requestFocus();
            } else if (family.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Please enter family", Toast.LENGTH_SHORT).show();
                family.requestFocus();
            } else if (username.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Please enter username", Toast.LENGTH_SHORT).show();
                username.requestFocus();
            } else if (password.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Please enter password", Toast.LENGTH_SHORT).show();
                password.requestFocus();
            } else if (repeatPassword.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Please enter repeat password", Toast.LENGTH_SHORT).show();
                repeatPassword.requestFocus();
            } else if (!password.getText().toString().equals(repeatPassword.getText().toString())) {
                Toast.makeText(getContext(), "repeat password incorrect", Toast.LENGTH_SHORT).show();
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
                RetrofitClient.getInstance(getContext()).getApi().
                        registerUser(n, f, g, u, p).enqueue(new Callback<JsonResponseModel>() {
                    @Override
                    public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Register", Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStackImmediate();
                        } else if (response.code() == 409) {
                            Toast.makeText(getContext(), "User exist", Toast.LENGTH_SHORT).show();
                            username.requestFocus();
                            username.setText("");
                        } else {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                        Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();

                    }
                });
            }


        });

    }

}
