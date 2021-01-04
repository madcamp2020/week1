package com.example.madcamp2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        
        ViewPager vp = findViewById(R.id.viewpager);
        Fragment1 fragment1 = new Fragment1();

        if (bundle != null) {
            fragment1.setArguments(bundle);
        }


        ViewpagerAdapter adapter = new ViewpagerAdapter(getSupportFragmentManager(), fragment1, new Fragment2(), new Fragment3());
        vp.setAdapter(adapter);

        TabLayout tab = findViewById(R.id.tabLayout);
        tab.setupWithViewPager(vp);

/*
        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.contact);
        images.add(R.drawable.photos);
        images.add(R.drawable.health);

        for(int i=0;i<3;i++) tab.getTabAt(i).setIcon(images.get(i));
*/
    }
}