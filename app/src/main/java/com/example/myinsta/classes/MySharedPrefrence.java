package com.example.myinsta.classes;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPrefrence {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private static MySharedPrefrence instance;

    public static MySharedPrefrence getInstance(Context context){
        if(instance==null)
            instance=new MySharedPrefrence(context);
        return instance;
    }


    private MySharedPrefrence(Context context){
        sp=context.getSharedPreferences("myApp",Context.MODE_PRIVATE);
        editor=sp.edit();
    }

    public void setIsLogin(){
        editor.putBoolean("isLogin",true).commit();
    }

    public boolean getIsLogin(){
        return sp.getBoolean("isLogin",false);

    }

    public void setUsername(String username){
        editor.putString("isUsername",username).commit();
    }

    public String getUsername(){
        return sp.getString("isUsername","");

    }

    public void clearSharedPrefrence(){
        editor.clear().commit();
    }

    public void setWriteExternal(){
        editor.putBoolean("writeExternal",true).commit();
    }

    public boolean getWriteExternal(){
        return sp.getBoolean("writeExternal",false);

    }

    public void setUsernameBio(String username){
        editor.putString("isUsernameBio",username).commit();
    }

    public String getUsernameBio(){
        return sp.getString("isUsernameBio","");

    }



}
