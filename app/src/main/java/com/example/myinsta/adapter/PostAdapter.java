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

import androidx.annotation.NonNull;
import androidx.emoji.widget.EmojiTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myinsta.activity.CommentActivity;
import com.example.myinsta.R;
import com.example.myinsta.model.PostItem;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private Context context;
    private List<PostItem> list;

    public PostAdapter(Context context, List<PostItem> list){
        this.list=list;
        this.context=context;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.row_post, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostItem item=list.get(position);

        holder.des.setText(new String(Base64.decode(item.getDescription(),Base64.DEFAULT)));
        holder.user.setText(item.getUser_id());
        holder.simple.setImageURI(Uri.parse(context.getString(R.string.image_address,item.getImage())));

        holder.date.setText(item.getDate());
        holder.id.setText(item.getId());
        holder.commentCount.setText(item.getCountComment()+"");

        holder.comment.setOnClickListener(v->{
            Intent in=new Intent(context, CommentActivity.class);
            in.putExtra("postid",item.getId());
            context.startActivity(in);
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView user,date,id,commentCount;
        private EmojiTextView des;
        private SimpleDraweeView simple;
        private ImageView comment;

        public ViewHolder(@NonNull View v) {
            super(v);

            user=v.findViewById(R.id.row_post_user);
            des=v.findViewById(R.id.row_post_des);
            simple=v.findViewById(R.id.row_post_simple);
            id=v.findViewById(R.id.row_post_id);
            date=v.findViewById(R.id.row_post_date);
            commentCount=v.findViewById(R.id.row_post_comment_count);
            comment=v.findViewById(R.id.row_post_comment);
        }
    }
}