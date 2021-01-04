package com.example.madcamp2020;

import android.Manifest;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Fragment1 extends Fragment {
    private RecyclerView recyclerView;
    private FragmentAdapter adapter;
    private ArrayList<Contacts> list = ContactsList.getInstance();
    Button loadBtn;
    Contacts item;

//    private Boolean isPermission = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

 //       tedPermission();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_1, container, false);
//        loadBtn = (Button) rootView.findViewById(R.id.button1);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler1);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        list = Contacts.createContactsList(getActivity());
        AscendingName ascending = new AscendingName();

        Bundle bundle = getArguments();
        if (bundle == null) {
            if (list.isEmpty())
                list = Contacts.createContactsList(list, getActivity(), null);
            Log.i("Fragment1 item", "item null");
        }
        else {
            item = bundle.getParcelable("Contacts");
            Log.i("Fragment1 item", item.nickname);
            list = Contacts.modifyContactsList(list, getActivity(), item);
            Log.i("Fragment1 item", item.nickname);

        }
        Collections.sort(list, ascending);
        adapter = new FragmentAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);
        Log.i("Frag", "MainFragment");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        Intent intent = new Intent(getActivity(), ContactsEditActivity.class);
//        startActivity(intent);
//        loadBtn = (Button) rootView.findViewById(R.id.button1);
        return rootView;
    }
    static class AscendingName implements Comparator<Contacts> {

        @Override
        public int compare(Contacts o1, Contacts o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
/*
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

        TedPermission.with(getContext().getApplicationContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.READ_CONTACTS)
                .check();

    }
    */
}