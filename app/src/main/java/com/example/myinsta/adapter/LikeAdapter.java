package com.example.myinsta.adapter;

import android.content.Context;
import android.net.Uri;
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
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.ViewHolder>  {
    private List<PostItem> list;
    private Context context;

    public LikeAdapter( List<PostItem> list,Context context) {
        this.list = list;
        this.context = context;


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_like, parent, false);
        return new LikeAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostItem item=list.get(position);

        holder.username.setText(item.getUser_id());
        holder.date.setText(item.getDate());
        holder.simpleUser.setImageURI(Uri.parse(context.getString(R.string.image_address_user, item.getImage_user())));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView username, date;
        private SimpleDraweeView simpleUser;

        public ViewHolder(@NonNull View v) {
            super(v);

            username = v.findViewById(R.id.row_like_username);
            date = v.findViewById(R.id.row_like_date);
            simpleUser=v.findViewById(R.id.row_like_simple_user);
        }
    }
}
