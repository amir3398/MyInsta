package com.example.myinsta.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.emoji.widget.EmojiTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myinsta.activity.CommentActivity;
import com.example.myinsta.R;
import com.example.myinsta.activity.LikeActivity;
import com.example.myinsta.classes.MySharedPrefrence;
import com.example.myinsta.data.RetrofitClient;
import com.example.myinsta.model.JsonResponseModel;
import com.example.myinsta.model.PostItem;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private Context context;
    private List<PostItem> list;
    private static int counter = 0;


    public PostAdapter(Context context, List<PostItem> list) {
        this.list = list;
        this.context = context;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_post, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostItem item = list.get(position);

        holder.des.setText(new String(Base64.decode(item.getDescription(), Base64.DEFAULT)));
        holder.user.setText(item.getUser_id());
        holder.simplePost.setImageURI(Uri.parse(context.getString(R.string.image_address, item.getImage())));
        holder.simpleUser.setImageURI(Uri.parse(context.getString(R.string.image_address_user, item.getImage_user())));
        holder.date.setText(item.getDate());
        holder.id.setText(item.getId());
        holder.commentCount.setText(item.getCountComment());
        holder.likeCount.setText(item.getCountLike());

        holder.comment.setOnClickListener(v -> {
            Intent in = new Intent(context, CommentActivity.class);
            in.putExtra("postid", item.getId());
            context.startActivity(in);
        });
        holder.simplePost.setOnClickListener(v -> {
            if (counter == 0) {
                counter++;
            } else if (counter == 1) {
                counter = 0;
                RetrofitClient.getInstance(context).getApi().like(MySharedPrefrence.getInstance(context).getUsername(), item.getId())
                        .enqueue(new Callback<JsonResponseModel>() {
                            @Override
                            public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {
                                if (response.code() == 200) {
                                    Toast.makeText(context, "liked", Toast.LENGTH_SHORT).show();
                                    holder.like.setColorFilter(context.getResources().getColor(R.color.colorRed));
                                    int c = Integer.parseInt(holder.likeCount.getText().toString());
                                    holder.likeCount.setText(++c + "");

                                } else if (response.code() == 201) {
                                    Toast.makeText(context, "disliked", Toast.LENGTH_SHORT).show();
                                    holder.like.setColorFilter(context.getResources().getColor(R.color.colorYellow));
                                    int c = Integer.parseInt(holder.likeCount.getText().toString());
                                    holder.likeCount.setText(--c + "");

                                } else {
                                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                                Toast.makeText(context, "failure", Toast.LENGTH_SHORT).show();

                            }
                        });

            }


        });
        holder.like.setOnClickListener(v -> {
            Intent in = new Intent(context, LikeActivity.class);
            in.putExtra("postid", item.getId());
            context.startActivity(in);
        });
        holder.save.setOnClickListener(v -> {

            RetrofitClient.getInstance(context).getApi().save(MySharedPrefrence.getInstance(context).getUsername(), item.getId())
                    .enqueue(new Callback<JsonResponseModel>() {
                        @Override
                        public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {
                            if (response.code()==200) {
                                holder.save.setColorFilter(context.getResources().getColor(R.color.colorRed));
                                Toast.makeText(context, "seved", Toast.LENGTH_SHORT).show();

                            } else if (response.code()==201) {
                                holder.save.setColorFilter(context.getResources().getColor(R.color.colorYellow));
                                Toast.makeText(context, "un saved", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                            Toast.makeText(context, "not aviliable internet", Toast.LENGTH_SHORT).show();
                        }
                    });
        });


        RetrofitClient.getInstance(context).getApi().getLikeColor(MySharedPrefrence.getInstance(context).getUsername(), item.getId())
                .enqueue(new Callback<JsonResponseModel>() {
                    @Override
                    public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {
                        if (response.body().getMessage().equals("like"))
                            holder.like.setColorFilter(context.getResources().getColor(R.color.colorRed));
                        else if (response.body().getMessage().equals("unlike"))
                            holder.like.setColorFilter(context.getResources().getColor(R.color.colorYellow));
                    }

                    @Override
                    public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                        Toast.makeText(context, "not aviliable internet", Toast.LENGTH_SHORT).show();
                    }
                });

        RetrofitClient.getInstance(context).getApi().getCommentColor(MySharedPrefrence.getInstance(context).getUsername(), item.getId())
                .enqueue(new Callback<JsonResponseModel>() {
                    @Override
                    public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {
                        if (response.body().getMessage().equals("yes"))
                            holder.comment.setColorFilter(context.getResources().getColor(R.color.colorRed));
                        else if (response.body().getMessage().equals("no"))
                            holder.comment.setColorFilter(context.getResources().getColor(R.color.colorYellow));

                    }

                    @Override
                    public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                        Toast.makeText(context, "not aviliable internet", Toast.LENGTH_SHORT).show();

                    }
                });

        RetrofitClient.getInstance(context).getApi().getSaveColor(MySharedPrefrence.getInstance(context).getUsername(), item.getId())
                .enqueue(new Callback<JsonResponseModel>() {
                    @Override
                    public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {
                        if (response.body().getMessage().equals("yes"))
                            holder.save.setColorFilter(context.getResources().getColor(R.color.colorRed));
                        else if (response.body().getMessage().equals("no"))
                            holder.save.setColorFilter(context.getResources().getColor(R.color.colorYellow));

                    }

                    @Override
                    public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                        Toast.makeText(context, "not aviliable internet", Toast.LENGTH_SHORT).show();

                    }
                });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView user, date, id, commentCount, likeCount;
        private EmojiTextView des;
        private SimpleDraweeView simplePost,simpleUser;
        private ImageView comment, like, save;

        public ViewHolder(@NonNull View v) {
            super(v);

            user = v.findViewById(R.id.row_post_user);
            des = v.findViewById(R.id.row_post_des);
            simplePost = v.findViewById(R.id.row_post_simple_post);
            simpleUser = v.findViewById(R.id.row_post_simple_user);
            id = v.findViewById(R.id.row_post_id);
            date = v.findViewById(R.id.row_post_date);
            commentCount = v.findViewById(R.id.row_post_comment_count);
            comment = v.findViewById(R.id.row_post_comment);
            like = v.findViewById(R.id.row_post_like);
            likeCount = v.findViewById(R.id.row_post_like_count);
            save = v.findViewById(R.id.row_post_save);


        }
    }

}
