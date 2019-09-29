package com.example.myinsta.fragment;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class BiogeraphiFragment extends Fragment {
    private RecyclerView biogeraphiRecycler;
    private List<PostItem> data;
    private String username;
    private PostAdapter adapter;
    private TextView biogeraphiAddFriend;
    private TextView biogeraphiBlock;
    private ImageView biogeraphiBack;

    public BiogeraphiFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_biogeraphi, container, false);

        init(v);

        return v;
    }


    private void init(View v) {


        biogeraphiRecycler = v.findViewById(R.id.biogeraphi_recyler);
        biogeraphiRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        biogeraphiAddFriend = v.findViewById(R.id.biogeraphi_addfriend);
        biogeraphiBlock = v.findViewById(R.id.biogeraphi_block);
        biogeraphiBack = v.findViewById(R.id.biogeraphi_back);

        username = MySharedPrefrence.getInstance(getContext()).getUsernameBio();
        if (username.isEmpty() || username.equals("0")) {
            Toast.makeText(getContext(), "try again", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStackImmediate();
        }

        clicks();
        getdata();
    }


    private void getdata() {
        RetrofitClient.getInstance(getContext()).getApi().
                getMyPost(username)
                .enqueue(new Callback<PostModel>() {
                    @Override
                    public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                        if (response.isSuccessful()) {
                            data = response.body().getData();
                            adapter = new PostAdapter(getContext(), data);
                            biogeraphiRecycler.setAdapter(adapter);
                        } else {

                            Toast.makeText(getContext(), "not found any post", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PostModel> call, Throwable t) {
                        Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();

                    }
                });

        RetrofitClient.getInstance(getContext()).getApi().
                getBlock(username, MySharedPrefrence.getInstance(getContext()).getUsername()).
                enqueue(new Callback<JsonResponseModel>() {
                    @Override
                    public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {
                        if (response.body().getMessage().equals("unblocked")) {
                            biogeraphiBlock.setText("blocked");
                            biogeraphiBlock.setTextColor(getResources().getColor(R.color.colorBlue));

                        } else if (response.body().getMessage().equals("blocked")) {
                            biogeraphiBlock.setText("unblocked");
                            biogeraphiBlock.setTextColor(getResources().getColor(R.color.colorRed));
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                        Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();

                    }
                });

        RetrofitClient.getInstance(getContext()).getApi().
                getFriend(username, MySharedPrefrence.getInstance(getContext()).getUsername()).
                enqueue(new Callback<JsonResponseModel>() {
                    @Override
                    public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {
                        if (response.body().getMessage().equals("isFriend")) {
                            biogeraphiAddFriend.setText("remove friend");
                            biogeraphiAddFriend.setTextColor(getResources().getColor(R.color.colorRed));

                        } else if (response.body().getMessage().equals("isNotFriend")) {
                            biogeraphiAddFriend.setText("add friend");
                            biogeraphiAddFriend.setTextColor(getResources().getColor(R.color.colorBlue));

                        }
                    }

                    @Override
                    public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                        Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();

                    }
                });
    }


    private void clicks() {
        biogeraphiBack.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStackImmediate();
        });

        biogeraphiAddFriend.setOnClickListener(v -> {

            RetrofitClient.getInstance(getContext()).getApi().newFriend
                    (username, MySharedPrefrence.getInstance(getContext()).getUsername()).enqueue
                    (new Callback<JsonResponseModel>() {
                        @Override
                        public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {

                            if (response.body().getMessage().equals("addFriend")) {
                                Toast.makeText(getContext(), "friend added", Toast.LENGTH_SHORT).show();
                                biogeraphiAddFriend.setText("remove friend");
                                biogeraphiAddFriend.setTextColor(getResources().getColor(R.color.colorRed));
                            } else if (response.body().getMessage().equals("removeFriend")) {
                                Toast.makeText(getContext(), "friend removed", Toast.LENGTH_SHORT).show();
                                biogeraphiAddFriend.setText("add friend");
                                biogeraphiAddFriend.setTextColor(getResources().getColor(R.color.colorBlue));
                            } else {
                                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                            Toast.makeText(getContext(), "failure", Toast.LENGTH_SHORT).show();

                        }
                    });
        });

        biogeraphiBlock.setOnClickListener(v -> {
            RetrofitClient.getInstance(getContext()).getApi().
                    block(username, MySharedPrefrence.getInstance(getContext()).getUsername())
                    .enqueue(new Callback<JsonResponseModel>() {
                        @Override
                        public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {
                            if (response.body().getMessage().equals("blocked")) {
                                Toast.makeText(getContext(), "blocked", Toast.LENGTH_SHORT).show();
                                biogeraphiBlock.setText("unblocked");
                                biogeraphiBlock.setTextColor(getResources().getColor(R.color.colorRed));

                            } else if (response.body().getMessage().equals("unblocked")) {
                                Toast.makeText(getContext(), "unblocked", Toast.LENGTH_SHORT).show();
                                biogeraphiBlock.setText("blocked");
                                biogeraphiBlock.setTextColor(getResources().getColor(R.color.colorBlue));

                            } else {
                                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                            Toast.makeText(getContext(), "failure", Toast.LENGTH_SHORT).show();

                        }
                    });
        });
    }
}
