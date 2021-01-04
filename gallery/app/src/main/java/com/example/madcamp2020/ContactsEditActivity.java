package com.example.madcamp2020;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;


public class ContactsEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_edit);
        TextView tx1 = (TextView) findViewById(R.id.wordText);
        TextView tx2 = (TextView) findViewById(R.id.meaningText);
//        TextView tx3 = (TextView) findViewById(R.id.nicknameText);
        EditText editId = (EditText) findViewById(R.id.editText1);
//        String nickname = editId.getText().toString();
//        Log.i("EditActivity", nickname);
        Intent intent = getIntent();

//        String name = intent.getExtras().getString("name");


//        String phNumbers = intent.getExtras().getString("phNumbers");

        Contacts item = intent.getParcelableExtra("Contacts");
        tx1.setText(item.name);
        tx2.setText(item.phNumbers);
        Log.i("EditActivity", item.name);
        Log.i("EditActivity", item.phNumbers);


        Button sub = (Button) findViewById((R.id.save_button));

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = editId.getText().toString();
                item.nickname = nickname;
//                Fragment1 fragment = new Fragment1();
                Bundle bundle = new Bundle();
                Log.i("ContactsEditActivity", "ClickButton");
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_1,fragment).commit();

                Log.i("AfterClick", item.nickname);
                bundle.putParcelable("Contacts", item);
//                fragment.setArguments(bundle);
                Log.i("AfterClick", "hi");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.putExtra("nickname", nickname);
//                intent.putExtra("Contacts", item);
                intent.putExtras(bundle);
                Log.i("AfterClick", "sendIntent");
                startActivity(intent);
            }
        });
    }
}
