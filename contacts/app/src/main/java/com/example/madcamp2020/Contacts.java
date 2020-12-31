package com.example.madcamp2020;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
//import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.os.Bundle;

public class Contacts {
    public String name;
    public String phNumbers;

    //    final private Context ctx;
    // 화면에 표시될 문자열 초기화
    public Contacts(String name, String contacts) {
        this.name = name;
        this.phNumbers = contacts;
    }
    public static ArrayList<Contacts> createContactsList(Context context) {
        ArrayList<Contacts> contacts = new ArrayList<Contacts>();
        Cursor c = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                "starred=?", new String[] {"1"}, null);
        while (c.moveToNext()) {

            String contactName = c
                    .getString(c
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phNumber = c
                    .getString(c
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            contacts.add(new Contacts(contactName, phNumber));

        }
        c.close();

        return contacts;
    }
}
    // 입력받은 숫자의 리스트생성


//        for (int i = 1; i <= numContacts; i++) {
//            contacts.add(new WordItemData("Person ", "test"+i));
//        }
//
//        return contacts;
//    }
//
//}
