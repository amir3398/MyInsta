package com.example.myinsta.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.emoji.widget.EmojiEditText;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myinsta.R;
import com.example.myinsta.adapter.CommentAdapter;
import com.example.myinsta.classes.MySharedPrefrence;
import com.example.myinsta.data.RetrofitClient;
import com.example.myinsta.model.JsonResponseModel;
import com.example.myinsta.model.PostItem;
import com.example.myinsta.model.PostModel;

import java.io.UnsupportedEncodingException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {
    private RecyclerView recyclerComment;
    private List<PostItem> data;
    private EmojiEditText comment;
    private ImageView send;
    private String postid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void init() {
        recyclerComment = findViewById(R.id.comment_recycler);
        LinearLayoutManager lm=new LinearLayoutManager(this);
        lm.setReverseLayout(true);
        lm.setStackFromEnd(false);
        recyclerComment.setLayoutManager(lm);

        comment = findViewById(R.id.comment_comment);
        send = findViewById(R.id.comment_send);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction==ItemTouchHelper.RIGHT){
                    Toast.makeText(CommentActivity.this, "ItemTouchHelper.RIGHT", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(CommentActivity.this, "ItemTouchHelper.LEFT", Toast.LENGTH_SHORT).show();

                }

            }
        }).attachToRecyclerView(recyclerComment);

        postid = getIntent().getExtras().getString("postid", "0");
        if (postid.isEmpty() || postid.equals("0")) {
            Toast.makeText(this, "try again", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }


        getData();
        clicks();
    }

    private void clicks() {
        send.setOnClickListener(v->{

            //String c=comment.getText().toString();

            byte[]  c = new byte[0];
            try {
                c = comment.getText().toString().getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String u=MySharedPrefrence.getInstance(this).getUsername();

            RetrofitClient.getInstance(this).getApi().newComment(u,Base64.encodeToString(c, Base64.DEFAULT),postid)
                    .enqueue(new Callback<JsonResponseModel>() {
                        @Override
                        public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {
                            if(response.isSuccessful()){
                                comment.setText("");
                                Toast.makeText(CommentActivity.this, "comment added", Toast.LENGTH_SHORT).show();
                                onResume();
                                //getData();
                            }else{
                                Toast.makeText(CommentActivity.this, "try again", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                            Toast.makeText(CommentActivity.this, "failure", Toast.LENGTH_SHORT).show();

                        }
                    });


        }
        );
    }

    private void getData() {


        RetrofitClient.getInstance(this).getApi().getComment(postid)
                .enqueue(new Callback<PostModel>() {
                    @Override
                    public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                        if (response.isSuccessful()) {
                            data = response.body().getData();
                            CommentAdapter adapter = new CommentAdapter(data,CommentActivity.this);
                            recyclerComment.setAdapter(adapter);
                        } else {

                            Toast.makeText(CommentActivity.this, "try again", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onFailure(Call<PostModel> call, Throwable t) {
                        Toast.makeText(CommentActivity.this, "failure", Toast.LENGTH_SHORT).show();
                        onBackPressed();

                    }
                });
    }
}
