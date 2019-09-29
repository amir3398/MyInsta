package com.example.myinsta.fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myinsta.R;
import com.example.myinsta.activity.LoginActivity;
import com.example.myinsta.adapter.ViewPagerAdapter;
import com.example.myinsta.classes.MySharedPrefrence;
import com.example.myinsta.data.RetrofitClient;
import com.example.myinsta.model.JsonResponseModel;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private TabLayout tabLayout;

    private MaterialButton exit;
    private SimpleDraweeView imageUser;
    private Bitmap bitmap;

    public ProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_profile, container, false);

        init(v);

        return v;
    }

    private void init(View v) {
        exit=v.findViewById(R.id.fragment_profille_exit);
        imageUser=v.findViewById(R.id.fragment_setting_image_user);
//item.getImage_user()


        tabLayout=v.findViewById(R.id.profile_tabloyout);
        viewPager=v.findViewById(R.id.profile_viewpager);

        if(getActivity()!=null) {
            adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
            adapter.addFragments(new PostFragment(), getResources().getString(R.string.posts));
            adapter.addFragments(new PasswordFragment(), getResources().getString(R.string.password));
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        }

        imageUser.setImageURI(Uri.parse(getContext().
                getString(R.string.image_address_user,MySharedPrefrence.getInstance(getActivity()).getUsername())));

        clicks();

    }


    private void clicks() {
        exit.setOnClickListener(v->{
            MySharedPrefrence.getInstance(getContext()).clearSharedPrefrence();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });

        imageUser.setOnClickListener(v->{

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.update);
            builder.setMessage(R.string.select_image_from_gallery);

            builder.setNegativeButton(R.string.gallery, (dialog, b) -> {
                selectFromGallery();
                dialog.dismiss();
            });
            builder.setNeutralButton(R.string.cancel, (dialog, b) -> {
                dialog.dismiss();
            });
            builder.setCancelable(false);
            builder.show();

        });

    }


    private void selectFromGallery() {

        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, getResources().getString(R.string.open_image)), 123);
    }


    private void updateImage() {
        String u=MySharedPrefrence.getInstance(getContext()).getUsername();
        String picname = new SimpleDateFormat("_yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());

        RetrofitClient.getInstance(getContext()).getApi().updateImageUser
                (u,toBase64(bitmap),u+picname).enqueue(new Callback<JsonResponseModel>() {
            @Override
            public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {
                if(response.body().getMessage().equals("updated")){
                    Toast.makeText(getContext(), R.string.updated, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                Toast.makeText(getContext(), R.string.failure, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, b);
        byte[] bytes = b.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
            if (requestCode == 123 && data != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.alert);
                builder.setMessage(R.string.are_you_sure);
                builder.setPositiveButton(R.string.yes, (a, b) -> {

                    CropImage.activity(data.getData())
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1)
                            .setAutoZoomEnabled(true)
                            .setAllowRotation(true)
                            .setAllowCounterRotation(true)
                            .setAllowFlipping(true)
                            .setActivityTitle("بریدن عکس")
                            .setCropShape(CropImageView.CropShape.OVAL)
                            .setFixAspectRatio(true)
                            .setMaxCropResultSize(2000,2000)
                            .setMinCropResultSize(100,100)
                            .setBackgroundColor(getResources().getColor(R.color.colorMirror))
                            .setBorderLineColor(getResources().getColor(R.color.colorRed))
                            .setGuidelinesColor(getResources().getColor(R.color.colorGreenDark))
                            .setBorderCornerColor(getResources().getColor(R.color.colorBlue))

                            .start(getActivity());
                });

                builder.setNegativeButton(R.string.select_again, (dialog, b) -> {
                    selectFromGallery();
                    dialog.dismiss();
                });
                builder.setNeutralButton(R.string.cancel, (dialog, b) -> {
                    dialog.dismiss();
                });
                builder.setCancelable(false);
                builder.show();


            }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
                    imageUser.setImageBitmap(bitmap);
                    updateImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }



}
