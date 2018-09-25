package com.marcelolongen.expensemanager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Database {
    private ArrayList<Item> itemObjects;
    private ArrayList categories;

    public Database() {
        itemObjects = new ArrayList<>();
    }

    private static Database db;

    public ArrayList<Item> getItemObjects() {
        return itemObjects;
    }

    public void setItemObjects(ArrayList<Item> itemObjects) {
        this.itemObjects = itemObjects;
    }

    public ArrayList getCategories() {
        return categories;
    }

    public void setCategories(ArrayList categories) {
        this.categories = categories;
    }


    public static Database getInstance(){
        if (db == null){ //if there is no instance available... create new one
           db = new Database();
        }

        return db;
    }
    private DatabaseReference root;
    private DatabaseReference user;

    public void readContentsFromFile(String userName) {

        root = FirebaseDatabase.getInstance().getReference();
        user = root.child("users").child(userName).child("Expenses");


        user.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot
                                             dataSnapshot) {
                itemObjects.clear();
                for (DataSnapshot artistSnapshot: dataSnapshot.getChildren()) {
                    Item item = artistSnapshot.getValue(Item.class); // {id: .., name: ..., genre: ...)
                    itemObjects.add(0, item); // add a new artist from DB to an arrayList
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


}
