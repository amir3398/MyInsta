package com.example.myinsta.fragment;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class PasswordFragment extends Fragment {

    private MaterialButton change;
    private TextInputEditText passOld, passNew, passRepeat;
    private TextInputLayout passOldL, passNewL, passRepeatL;
    private ImageView logo;


    public PasswordFragment() {    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_password, container, false);

        init(v);

        return v;
    }

    private void init(View v) {
        logo=v.findViewById(R.id.password_logo);

        change = v.findViewById(R.id.password_change);
        passOld = v.findViewById(R.id.password_old_password);
        passNew = v.findViewById(R.id.password_new_password);
        passRepeat = v.findViewById(R.id.password_repeat_password);
        passOldL = v.findViewById(R.id.password_old_layout);
        passNewL = v.findViewById(R.id.password_new_layout);
        passRepeatL = v.findViewById(R.id.password_repeat_layout);
        passOld.requestFocus();

        Clicks();

    }

    private void Clicks() {
        change.setOnClickListener(v -> {

            String o = passOld.getText().toString();
            String n = passNew.getText().toString();
            String r = passRepeat.getText().toString();

            passOldL.setError("");
            if (o.isEmpty()) {
                passOldL.setError("فیلد نمیتواند خالی باشد");
                passOld.requestFocus();
            } else {
                passOldL.setError("");
                if (n.isEmpty()) {
                    passNewL.setError("فیلد نمیتواند خالی باشد");
                    passNew.requestFocus();
                } else {
                    passNewL.setError("");
                    if (r.isEmpty()) {
                        passRepeatL.setError("فیلد نمیتواند خالی باشد");
                        passRepeat.requestFocus();
                    } else {
                        passRepeatL.setError("");
                        if (!n.equals(r)) {
                            passRepeatL.setError("تکرار رمز اشتباه وارد شده است");
                            passRepeat.setText("");
                            passRepeat.requestFocus();
                        } else {
                            passRepeatL.setError("");
                            checkPassword(o,n);
                        }
                    }
                }
            }
        });
    }

    private void updatePassword(String password) {
        String user = MySharedPrefrence.getInstance(getContext()).getUsername();
        RetrofitClient.getInstance(getContext()).getApi().
                updatePasswordUser(user, password).enqueue(new Callback<JsonResponseModel>() {
            @Override
            public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {
                if (response.isSuccessful()) {
                    passOld.setText("");
                    passNew.setText("");
                    passRepeat.setText("");
                    Toast.makeText(getContext(), R.string.updated, Toast.LENGTH_SHORT).show();
                    ainmationLogo();

                } else
                    Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                Toast.makeText(getContext(), R.string.failure, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void ainmationLogo() {
        ObjectAnimator rotation=ObjectAnimator.ofFloat(
                logo,
                "rotation", 0f,360f);
        rotation.setDuration(1000);
        rotation.start();

        passOld.requestFocus();

    }

    private void checkPassword(String passwordOld,String passwordNew) {

        String user = MySharedPrefrence.getInstance(getContext()).getUsername();
        RetrofitClient.getInstance(getContext()).getApi().getPasswordUser(user, passwordOld)
                .enqueue(new Callback<JsonResponseModel>() {
                    @Override
                    public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {
                        if (response.body().getMessage().equals("true")) {
                            updatePassword(passwordNew);
                        } else {
                            passOld.setText("");
                            passOldL.setError("رمز اشتباه وارد شده است");
                            passOld.requestFocus();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                        Toast.makeText(getContext(), R.string.failure, Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
