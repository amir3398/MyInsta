package com.example.myinsta.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.myinsta.R;
import com.example.myinsta.adapter.PostAdapter;
import com.example.myinsta.classes.MySharedPrefrence;
import com.example.myinsta.data.RetrofitClient;
import com.example.myinsta.model.PostItem;
import com.example.myinsta.model.PostModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaveActivity extends AppCompatActivity {
    private RecyclerView recyclerSave;
    private List<PostItem> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        init();
    }

    private void init() {

        recyclerSave = findViewById(R.id.save_recycler);
        recyclerSave.setLayoutManager(new LinearLayoutManager(this));

        getData();
    }


    private  void getData(){
        String u= MySharedPrefrence.getInstance(this).getUsername();
        RetrofitClient.getInstance(this).getApi().getSave(u).enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if(response.isSuccessful()) {
                    data = response.body().getData();
                    PostAdapter adapter = new PostAdapter(SaveActivity.this,data );
                    recyclerSave.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                Toast.makeText(SaveActivity.this, "Failure", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
