package com.example.myinsta.model;

public class PostItem {
    private String id;
    private String user_id;
    private String description;
    private String image;
    private String date;
    private String comment;
    private String countComment;
    private String countLike;
    private String usrename;

    public String getCountLike() {
        return countLike;
    }

    public String getUsrename() {
        return usrename;
    }

    public String getCountComment() {
        return countComment;
    }

    public String getComment() {
        return comment;
    }

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getDate() {
        return date;
    }
}
