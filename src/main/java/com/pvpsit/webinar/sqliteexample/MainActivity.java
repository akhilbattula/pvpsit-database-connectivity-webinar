package com.pvpsit.webinar.sqliteexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHandler db = new DatabaseHandler(this);
        //Add contacts - SQLite
        db.addContact(new Contact(1,"Akhil", "1234567890"));
        db.addContact(new Contact(2,"Raj", "0987654321"));
        db.addContact(new Contact(3,"Ravi", "7890123456"));

        //Get All Contacts - SQLite
        List<Contact> list = db.getAllContacts();
        for (int i=0;i<list.size();i++) {
            Log.d("pvpsit", i+" Contact ID: "+list.get(i).id);
            Log.d("pvpsit", i+" Contact Name: "+list.get(i).name);
            Log.d("pvpsit", i+" Contact Phone: "+list.get(i).phone_number);
        }

        //Get single contact - SQLite
        Contact contact = db.getContactByID(1);
        Log.d("pvpsit", "Contact ID: "+contact.id);
        Log.d("pvpsit", "Contact Name: "+contact.name);
        Log.d("pvpsit", "Contact Phone: "+contact.phone_number);

        //Update contact - SQLite
        db.updateContact(new Contact(1, "ABC", "1223334444"));

        //Delete contact - SQLite
        db.deleteContact(2);

        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

        //Writing the data - Firebase Firestore
        //instance -> collection -> document -> set -> success/failed
        Map<String, Object> contactValues = new HashMap<>();
        contactValues.put("id", "2");
        contactValues.put("name", "Raj");
        contactValues.put("phone", "1111111111");
        firestoreDB.collection("collection_name")
                .document("document_id")
                .set(contactValues)
                .addOnSuccessListener(unused -> {
                    Log.e("pvpsit", "success");
                })
                .addOnFailureListener(e -> {
                    Log.e("pvpsit", "failed");
                    e.printStackTrace();
                });


        //Update the data - Firebase Firestore
        //instance -> collection -> document -> update -> success/failed
        Map<String, Object> contactUpdateValues = new HashMap<>();
        contactValues.put("id", "2");
        contactValues.put("name", "Ravi");
        contactValues.put("phone", "1111111111");

        firestoreDB.collection("pvpsit_contacts")
                .document("2")
                .update(contactUpdateValues)
                .addOnSuccessListener(unused -> {
                    Log.e("pvpsit", "success");
                })
                .addOnFailureListener(e -> {
                    Log.e("pvpsit", "failed");
                    e.printStackTrace();
                });

        //Reading the data  - Firebase Firestore
        //instance -> collection -> document -> insert -> success/failed
        //You can read all documents
        //or you can read particular doc
        firestoreDB.collection("pvpsit_contacts")
                .document("1")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot!=null) {
                        String id = documentSnapshot.getString("id");
                        String name = documentSnapshot.getString("name");
                        String phone = documentSnapshot.getString("phone");
                        Log.e("pvpsit", "id: "+id);
                        Log.e("pvpsit", "name: "+name);
                        Log.e("pvpsit", "phone: "+phone);

                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("pvpsit", "failed");
                    e.printStackTrace();
                });

        //Delete the data - Firebase Firestore
        firestoreDB.collection("pvpsit_contacts")
                .document("1")
                .delete()
                .addOnSuccessListener(unused -> {
                    Log.e("pvpsit", "success");
                })
                .addOnFailureListener(e -> {
                    Log.e("pvpsit", "failed");
                    e.printStackTrace();
                });

    }
}