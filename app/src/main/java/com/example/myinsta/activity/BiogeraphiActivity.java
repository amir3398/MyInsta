package com.example.myinsta.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myinsta.R;
import com.example.myinsta.adapter.PostAdapter;
import com.example.myinsta.classes.MySharedPrefrence;
import com.example.myinsta.data.RetrofitClient;
import com.example.myinsta.model.JsonResponseModel;
import com.example.myinsta.model.PostItem;
import com.example.myinsta.model.PostModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BiogeraphiActivity extends AppCompatActivity {
    private RecyclerView biogeraphiRecycler;
    private List<PostItem> data;
    private String username;
    private PostAdapter adapter;
    private TextView biogeraphiAddFriend;
    private TextView biogeraphiBlock;
    private ImageView biogeraphiBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biogeraphi);

        init();
        clicks();
    }


    private void init() {


        biogeraphiRecycler=findViewById(R.id.biogeraphi_recyler);
        biogeraphiRecycler.setLayoutManager(new LinearLayoutManager(this));
        biogeraphiAddFriend=findViewById(R.id.biogeraphi_addfriend);
        biogeraphiBlock=findViewById(R.id.biogeraphi_block);
        biogeraphiBack=findViewById(R.id.biogeraphi_back);

        username = getIntent().getExtras().getString("username", "0");
        if (username.isEmpty() || username.equals("0")) {
            Toast.makeText(this, "try again", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        getdata();
    }


    private void getdata() {
        RetrofitClient.getInstance(this).getApi().
                getMyPost(username)
                .enqueue(new Callback<PostModel>() {
                    @Override
                    public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                        if (response.isSuccessful()) {
                            data = response.body().getData();
                            adapter = new PostAdapter(BiogeraphiActivity.this,data);
                            biogeraphiRecycler.setAdapter(adapter);
                        } else {

                            Toast.makeText(BiogeraphiActivity.this, "not found any post", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PostModel> call, Throwable t) {
                        Toast.makeText(BiogeraphiActivity.this, "Failure", Toast.LENGTH_SHORT).show();

                    }
                });
    }


    private void clicks() {
        biogeraphiBack.setOnClickListener(v-> onBackPressed());

        biogeraphiAddFriend.setOnClickListener(v->{
            RetrofitClient.getInstance(this).getApi().
                    newFriend(username,MySharedPrefrence.getInstance(this).getUsername())
                    .enqueue(new Callback<JsonResponseModel>() {
                        @Override
                        public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {
                            if (response.body().getMessage().equals("added")) {
                                Toast.makeText(BiogeraphiActivity.this,response.body().getMessage() , Toast.LENGTH_SHORT).show();

                            } else if (response.body().getMessage().equals("deleted")){

                                Toast.makeText(BiogeraphiActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(BiogeraphiActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                            Toast.makeText(BiogeraphiActivity.this, "failure", Toast.LENGTH_SHORT).show();

                        }
                    });
        });
    }
}
