package com.pvpsit.webinar.sqliteexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "pvpsit_db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "contacts_table";

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PHONE_NUMBER = "phone_number";

    //2.Init DatabaseHandler constructor
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //3. Create Contacts Table in onCreate
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Declare statement
        //CREATE TABLE TABLE_NAME ID INTEGER PRIMARY KEY, NAME TEXT, PHONE_NUMBER TEXT

        String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"("
                +ID+" INTEGER PRIMARY KEY, "
                +NAME+" TEXT, "
                +PHONE_NUMBER+" TEXT)";

        //Execute statement
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    //4. onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    //Add contact to the DB
    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID, contact.id);
        values.put(NAME, contact.name);
        values.put(PHONE_NUMBER, contact.phone_number);

        db.insert(TABLE_NAME, null, values);

        //db.close();
    }

    //Get single contact by ID
    public Contact getContactByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{ID, NAME, PHONE_NUMBER},
                ID+"=?",
                new String[] { String.valueOf(id)},
                null,
                null,
                null,
                null
        );

        if(cursor!=null){
            cursor.moveToFirst();
        }

        Contact contact = new Contact(
            Integer.parseInt(cursor.getString(0)),
            cursor.getString(1),
            cursor.getString(2)
        );

        return contact;

    }

    //Get all contacts
    public List<Contact> getAllContacts() {
        ArrayList<Contact> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM "+TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);


        if(cursor!=null) {
            cursor.moveToFirst();
        } //cursor in first position

        do {
            Contact contact = new Contact(
                    Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2)
            );

            contactList.add(contact);
        } while (cursor.moveToNext());

        return contactList;
    }


    //Remove contact from DB
    public void deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID+"=?", new String[]{String.valueOf(id)});
        //db.close();
    }


    //Update contact by ID
    public void updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID, contact.id);
        values.put(NAME, contact.name);
        values.put(PHONE_NUMBER, contact.phone_number);

        db.update(TABLE_NAME, values, ID+"=?", new String[]{String.valueOf(contact.id)});

    }
}
