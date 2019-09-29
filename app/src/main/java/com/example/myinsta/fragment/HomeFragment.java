package com.example.myinsta.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myinsta.R;
import com.example.myinsta.adapter.PostAdapter;
import com.example.myinsta.data.RetrofitClient;
import com.example.myinsta.model.PostItem;
import com.example.myinsta.model.PostModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerPost;
    private List<PostItem> data;


    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        init(v);

        return v;
    }


    private void init(View v) {

        recyclerPost = v.findViewById(R.id.home_fragment_recycler);
        recyclerPost.setLayoutManager(new LinearLayoutManager(getContext()));


        getData();
    }


    private void getData() {
        RetrofitClient.getInstance(getContext()).getApi().getPost().enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if (response.isSuccessful()) {
                    data = response.body().getData();
                    PostAdapter adapter = new PostAdapter(getContext(), data);
                    recyclerPost.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
