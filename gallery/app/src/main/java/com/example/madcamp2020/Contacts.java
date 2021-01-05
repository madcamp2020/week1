package com.example.madcamp2020;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;
//import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.os.Bundle;
import android.util.Log;

public class Contacts implements Parcelable {
    public String name;
    public String phNumbers;
    public String nickname;
    public String photo_id;

    //    final private Context ctx;
    // 화면에 표시될 문자열 초기화

    public Contacts(Parcel parcel) {
        this.name = parcel.readString();
        this.phNumbers = parcel.readString();
        this.nickname = parcel.readString();
    }
    public Contacts(String name, String contacts, String nickname) {
        this.name = name;
        this.phNumbers = contacts;
        this.nickname = nickname;

    }
    public static ArrayList<Contacts> createContactsList( ArrayList<Contacts> contacts, Context context, Contacts item) {
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
            if (item!= null) {
                int pos = contacts.indexOf(item);
                if (item.name.equals(contactName) && item.phNumbers.equals(phNumber)) {

                    contacts.set(pos ,new Contacts(contactName, phNumber, item.nickname));
                    Log.i("Contacts", item.nickname);
                }
                else {
                    contacts.set(pos, new Contacts(contactName, phNumber, "Blank"));
                    Log.i("Contacts", "Blank nickname");
                }
            }
            else {
                contacts.add(new Contacts(contactName, phNumber, "Blank"));
                Log.i("Contacts", "Blank nickname");
            }
        }
        c.close();

        return contacts;
    }
    //
    public static ArrayList<Contacts> modifyContactsList(ArrayList<Contacts> contacts, Context context, Contacts item) {
        int count = 0;
        while (contacts.size() > count) {
            Contacts listitem = contacts.get(count);
            if (item!= null) {
                if (item.name.equals(listitem.name) && item.phNumbers.equals(listitem.phNumbers)) {
                    listitem.nickname = item.nickname;
                    contacts.set(count, listitem);
                    Log.i("Contacts", item.nickname);
                }
                else {
//                    contacts.add(new Contacts(contactName, phNumber, "Blank"));
                    Log.i("Contacts", "ModifyContactsElse");
                }
            }
            count++;
        }
        return contacts;
    }

    public static final Parcelable.Creator<Contacts> CREATOR = new Creator<Contacts>() {
        @Override
        public Contacts createFromParcel(Parcel parcel) {
            return new Contacts(parcel);
        }
        @Override
        public Contacts[] newArray(int size) {
            return new Contacts[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.phNumbers);
        dest.writeString(this.nickname);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhNumbers() {
        return phNumbers;
    }
    public void setPhNumbers(String phNumbers) {
        this.phNumbers = phNumbers;
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
