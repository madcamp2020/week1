package com.example.madcamp2020;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ViewpagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> items;
    private ArrayList<String> itext = new ArrayList<String>();

    public ViewpagerAdapter(@NonNull FragmentManager fm, Fragment1 fragment1, Fragment2 fragment2, Fragment3 fragment3) {
        super(fm);

        items = new ArrayList<Fragment>();
//        items.add(new Fragment1());
//        items.add(new Fragment2());
//        items.add(new Fragment3());

        items.add(fragment1);
        items.add(fragment2);
        items.add(fragment3);

        itext.add("연락처");
        itext.add("갤러리");
        itext.add("방명록");

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return itext.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
