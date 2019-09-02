package com.example.myinsta.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.myinsta.R;
import com.example.myinsta.adapter.PostAdapter;
import com.example.myinsta.adapter.ViewPagerAdapter;
import com.example.myinsta.fragment.PostFragment;
import com.example.myinsta.fragment.SettingFragment;
import com.google.android.material.tabs.TabLayout;

public class SettingActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        tabLayout=findViewById(R.id.setting_tabloyout);

        viewPager=findViewById(R.id.setting_viewpager);
        adapter=new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragments(new PostFragment(),"Posts");
        adapter.addFragments(new SettingFragment(),"Setting");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
