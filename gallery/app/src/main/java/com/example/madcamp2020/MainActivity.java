package com.example.madcamp2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Boolean isPermission = true;

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tedPermission();

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
           ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }

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

    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
                isPermission = true;

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
                isPermission = false;

            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS)
                .check();

    }


}