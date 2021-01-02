package com.example.madcamp2020;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends RecyclerView.Adapter<FragmentAdapter.Holder> {

    private Context context;
    private List<Contacts> list = new ArrayList<>();

    public FragmentAdapter(Context context, List<Contacts> list) {
        this.context = context;
        this.list = list;
    }

    // ViewHolder 생성
    // row layout을 화면에 뿌려주고 holder에 연결
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frag1_item, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    /*
     * Todo 만들어진 ViewHolder에 data 삽입 ListView의 getView와 동일
     *
     * */
    @Override
    public void onBindViewHolder(Holder holder, int position) {
        // 각 위치에 문자열 세팅
        int itemposition = position;
        holder.wordText.setText(list.get(itemposition).name);
        holder.meaningText.setText(list.get(itemposition).phNumbers);
        holder.nicknameText.setText(list.get(itemposition).nickname);
        Log.e("StudyApp", "onBindViewHolder" + itemposition);
    }

    // 몇개의 데이터를 리스트로 뿌려줘야하는지 반드시 정의해줘야한다
    @Override
    public int getItemCount() {
        return list.size(); // RecyclerView의 size return
    }


    // ViewHolder는 하나의 View를 보존하는 역할을 한다
    public class Holder extends RecyclerView.ViewHolder{
        public TextView wordText;
        public TextView meaningText;
        public TextView nicknameText;
        public Holder(View view){
            super(view);
            wordText = (TextView) view.findViewById(R.id.wordText);
            meaningText = (TextView) view.findViewById(R.id.meaningText);
            nicknameText = (TextView) view.findViewById(R.id.nicknameText);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                // TODO Auto-generated method stub
                    // 추가 기능 넣어줄 것들
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        // TODO : use pos.
                        Intent intent = new Intent(v.getContext(), ContactsEditActivity.class);
                        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                        Contacts item = list.get(pos) ;
                        intent.putExtra("name", item.name);
                        intent.putExtra("phNumbers", item.phNumbers);
                        intent.putExtra("Contacts", item);
                        v.getContext().startActivity(intent);
                    }

//                list = Contacts.createContactsList(getActivity());
//                adapter = new FragmentAdapter(getActivity(), list);
//                recyclerView.setAdapter(adapter);
//                loadBtn.setVisibility(rootView.GONE);
                }
            });
        }
    }
}