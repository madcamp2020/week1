package com.example.madcamp2020;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
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
        ImageView profileimage = findViewById(R.id.profile);
        Intent intent = getIntent();

//        String name = intent.getExtras().getString("name");


//        String phNumbers = intent.getExtras().getString("phNumbers");

        Contacts item = intent.getParcelableExtra("Contacts");
        tx1.setText(item.name);
        tx2.setText(item.phNumbers);
        Bitmap profile = loadContactPhoto(getApplicationContext().getContentResolver(), item.id, item.photo_id);
        if (profile != null) {
            if (Build.VERSION.SDK_INT>= 21) {
                profileimage.setBackground(new ShapeDrawable(new OvalShape()));
                profileimage.setClipToOutline(true);
            }
            profileimage.setImageBitmap(profile);
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                profileimage.setClipToOutline(false);
            }
        }
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
    public Bitmap loadContactPhoto(ContentResolver cr, long id, long photo_id) {
//        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
//        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
//        if (input != null)
//            return BitmapFactory.decodeStream(input);
//        else
//            Log.d("PHOTO", "first try failed to load photo");
        byte[] photoBytes = null;
        Uri photoUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, photo_id);
        Cursor c = cr.query(photoUri, new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO}, null, null, null);
        try {
            if (c.moveToFirst())
                photoBytes = c.getBlob(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }

        if (photoBytes != null)
            return BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);

        else
            Log.d("PHOTO", "second try also failed");
        return null;
    }

    public Bitmap resizingBitmap(Bitmap oBitmap) {
        if (oBitmap == null)
            return null;
        float width = oBitmap.getWidth();
        float height = oBitmap.getHeight();
        float resizing_size = 300;
        Bitmap rBitmap = null;
        if (width > resizing_size) {
            float mWidth = (float) (width / 300);
            float fScale = (float) (resizing_size / mWidth);
            width *= (fScale /100);
            height *= (fScale / 100);
        } else if (height > resizing_size) {
            float mHeight = (float) (height / 100);
            float fScale = (float) (resizing_size / mHeight);
            width *= (fScale /100);
            height *= (fScale / 100);
        }

//        Log.d("rBitmap : " + width + ", " + height);
        rBitmap = Bitmap.createScaledBitmap(oBitmap, (int) width, (int) height, true);
        return rBitmap;
    }
}
