package com.example.myinsta.adapter;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.emoji.widget.EmojiTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myinsta.R;
import com.example.myinsta.model.PostItem;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<PostItem> list;

    public CommentAdapter( List<PostItem> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostItem item=list.get(position);

        holder.username.setText(item.getUser_id());
        //holder.comment.setText(item.getComment());
        holder.comment.setText(new String(Base64.decode(item.getDescription(), Base64.DEFAULT)));
        holder.date.setText(item.getDate());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView username, date;
        private EmojiTextView comment;

        public ViewHolder(@NonNull View v) {
            super(v);

            username = v.findViewById(R.id.row_comment_username);
            comment = v.findViewById(R.id.row_comment_comment);
            date = v.findViewById(R.id.row_comment_date);
        }
    }
}
