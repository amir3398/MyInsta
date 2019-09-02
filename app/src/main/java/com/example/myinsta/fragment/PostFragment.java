package com.example.myinsta.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class PostFragment extends Fragment {
    private RecyclerView recyclerViewMyPost;
    private PostAdapter adapter;
    private List<PostItem> data;
    private String id;


    public PostFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post, container, false);

        init(v);

        return v;
    }

    private void init(View v) {
        recyclerViewMyPost = v.findViewById(R.id.fragment_post_recycler);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setReverseLayout(true);
        lm.setStackFromEnd(false);
        recyclerViewMyPost.setLayoutManager(lm);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction==ItemTouchHelper.RIGHT){
                    id=data.get(viewHolder.getAdapterPosition()).getId();
                    deleteMyPost(id);

                }else{
                   // Toast.makeText(CommentActivity.this, "ItemTouchHelper.LEFT", Toast.LENGTH_SHORT).show();

                }

            }
        }).attachToRecyclerView(recyclerViewMyPost);

        getdata();
    }

    private void deleteMyPost(String id) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Delete post");
        builder.setMessage("are you sure?");
        builder.setPositiveButton("yes",(a,b)->
                deletePostOk(id));
        builder.setNegativeButton("no",(a,b)->{
            a.dismiss();
            getdata();
        });
        builder.setCancelable(false);
        builder.show();

    }

    private void deletePostOk(String id) {
        RetrofitClient.getInstance(getContext()).getApi().deletePost(id)
                .enqueue(new Callback<JsonResponseModel>() {
                    @Override
                    public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {
                        if (response.code()==201) {
                            Toast.makeText(getContext(), "deleted", Toast.LENGTH_SHORT).show();
                            getdata();
                        } else {

                            Toast.makeText(getContext(), "try again", Toast.LENGTH_SHORT).show();
                            getdata();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                        Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();

                    }
                });


    }

    private void getdata() {
        String u=MySharedPrefrence.getInstance(getContext()).getUsername();
        RetrofitClient.getInstance(getContext()).getApi().
                getMyPost(u)
                .enqueue(new Callback<PostModel>() {
                    @Override
                    public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                        if (response.isSuccessful()) {
                            data = response.body().getData();
                             adapter = new PostAdapter(getContext(),data);
                            recyclerViewMyPost.setAdapter(adapter);
                        } else {

                            Toast.makeText(getContext(), "not found any post", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PostModel> call, Throwable t) {
                        Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();

                    }
                });
    }

}
