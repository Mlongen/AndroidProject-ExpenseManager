package com.example.marcelolongen.expensemanager;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Database {
    private ArrayList<Entry> entryObjects;
    private ArrayList categories;

    public Database() {
        entryObjects = new ArrayList<>();
    }

    public ArrayList<Entry> getEntryObjects() {
        return entryObjects;
    }

    public void setEntryObjects(ArrayList<Entry> entryObjects) {
        this.entryObjects = entryObjects;
    }

    public ArrayList getCategories() {
        return categories;
    }

    public void setCategories(ArrayList categories) {
        this.categories = categories;
    }


    public void readContentsFromFile() throws FileNotFoundException {
        String MYFILE = Environment.getExternalStorageDirectory().toString();
        String fileName = "entries.txt";
        File f = new File(MYFILE, fileName);
        Scanner scan2 = new Scanner(f);
        String text = scan2.nextLine();
        String[] splitted = text.split(";");
        for (int i = 0; i < splitted.length; i += 4) {
            Entry a = new Entry(1, splitted[i], Double.valueOf(splitted[i + 1]), Integer.valueOf(splitted[i + 2].substring(0, 1)), Integer.valueOf(splitted[i + 2].substring(2, 4)), Integer.valueOf(splitted[i + 2].substring(5, 9)), splitted[i + 3]);
            entryObjects.add(a);
        }
        scan2.close();
    }
}
