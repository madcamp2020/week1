package com.example.madcamp2020;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnClickListener;
import java.util.ArrayList;

public class Fragment1 extends Fragment {
    private RecyclerView recyclerView;
    private FragmentAdapter adapter;
    private ArrayList<Contacts> list = new ArrayList<>();
    Button loadBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_1, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler1);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        list = Contacts.createContactsList(getActivity());
        list = Contacts.createContactsList(getActivity());
        adapter = new FragmentAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);
        Log.e("Frag", "MainFragment");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        loadBtn = (Button) rootView.findViewById(R.id.button1);
//        loadBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                list = Contacts.createContactsList(getActivity());
//                adapter = new FragmentAdapter(getActivity(), list);
//                recyclerView.setAdapter(adapter);
//                loadBtn.setVisibility(rootView.GONE);
//            }
//        });

        return rootView;
    }
}