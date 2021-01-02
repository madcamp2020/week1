package com.example.madcamp2020;

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

import java.util.ArrayList;

public class Fragment1 extends Fragment {
    private RecyclerView recyclerView;
    private FragmentAdapter adapter;
    private ArrayList<Contacts> list = new ArrayList<>();
    Button loadBtn;
    Contacts item;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_1, container, false);
//        loadBtn = (Button) rootView.findViewById(R.id.button1);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler1);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        list = Contacts.createContactsList(getActivity());
        Bundle bundle = getArguments();
        if (bundle == null) {
            list = Contacts.createContactsList(getActivity(), null);
            Log.i("Fragment1 item", "item null");
        }
        else {
            item = bundle.getParcelable("Contacts");
            Log.i("Fragment1 item", item.nickname);
            list = Contacts.createContactsList(getActivity(), item);
            Log.i("Fragment1 item", item.nickname);

        }
        adapter = new FragmentAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);
        Log.i("Frag", "MainFragment");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        Intent intent = new Intent(getActivity(), ContactsEditActivity.class);
//        startActivity(intent);
//        loadBtn = (Button) rootView.findViewById(R.id.button1);
        return rootView;
    }
}