package com.example.myinsta.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.emoji.widget.EmojiEditText;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myinsta.R;
import com.example.myinsta.adapter.CommentAdapter;
import com.example.myinsta.adapter.LikeAdapter;
import com.example.myinsta.data.RetrofitClient;
import com.example.myinsta.model.PostItem;
import com.example.myinsta.model.PostModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LikeActivity extends AppCompatActivity {
    private RecyclerView recyclerComment;
    private List<PostItem> data;
    private String postid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);

        init();
    }

    private void init() {
        recyclerComment = findViewById(R.id.like_recycler);
        recyclerComment.setLayoutManager(new LinearLayoutManager(this));



        postid = getIntent().getExtras().getString("postid", "0");
        if (postid.isEmpty() || postid.equals("0")) {
            Toast.makeText(this, "try again", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }


        getData();
    }

    private void getData() {


        RetrofitClient.getInstance(this).getApi().getLike(postid)
                .enqueue(new Callback<PostModel>() {
                    @Override
                    public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                        if (response.isSuccessful()) {
                            data = response.body().getData();
                            LikeAdapter adapter = new LikeAdapter(data,LikeActivity.this);
                            recyclerComment.setAdapter(adapter);
                        } else {

                            Toast.makeText(LikeActivity.this, "try again", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onFailure(Call<PostModel> call, Throwable t) {
                        Toast.makeText(LikeActivity.this, "failure", Toast.LENGTH_SHORT).show();
                        onBackPressed();

                    }
                });
    }
}
