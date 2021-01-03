package com.example.madcamp2020;

import java.util.ArrayList;

public class ContactsList {
    private ArrayList<Contacts> contacts;

    public ArrayList<Contacts> getContactList() {
        return contacts;
    }

    private static ArrayList<Contacts> instance = null;

    public static ArrayList<Contacts> getInstance() {
        if (null == instance) {
            synchronized (ContactsList.class) {
                if(instance == null)
                instance = new ArrayList<Contacts>();
            }
        }
        return instance;
    }
}

