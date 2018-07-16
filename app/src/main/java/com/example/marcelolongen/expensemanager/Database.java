package com.example.marcelolongen.expensemanager;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Database {
    private ArrayList<Item> itemObjects;
    private ArrayList categories;

    public Database() {
        itemObjects = new ArrayList<>();
    }

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


    public void readContentsFromFile() throws FileNotFoundException {
        String MYFILE = Environment.getExternalStorageDirectory().toString();
        String fileName = "entries.txt";
        File f = new File(MYFILE, fileName);
        if (f.exists()) {
            Scanner scan2 = new Scanner(f);
            if (scan2.hasNextLine()) {
                String text = scan2.nextLine();
                String[] splitted = text.split(";");
                if (splitted.length > 3) {
                    for (int i = 0; i < splitted.length; i += 4) {
                        Item a = new Item(1, splitted[i], Double.valueOf(splitted[i + 1]), Integer.valueOf(splitted[i + 2].substring(0, 1)), Integer.valueOf(splitted[i + 2].substring(2, 3)), Integer.valueOf(splitted[i + 2].substring(4, 8)), splitted[i + 3]);
                        itemObjects.add(a);
                    }
                }
            }
            scan2.close();
        }

    }
    public void deleteContentsFromFile() throws FileNotFoundException {
        String MYFILE = Environment.getExternalStorageDirectory().toString();
        String fileName = "entries.txt";
        File f = new File(MYFILE, fileName);
        Scanner scan2 = new Scanner(f);
        String text = scan2.nextLine();
        String[] splitted = text.split(";");
        for (int i = 0; i < splitted.length; i += 4) {
            Item a = new Item(1, splitted[i], Double.valueOf(splitted[i + 1]), Integer.valueOf(splitted[i + 2].substring(0, 1)), Integer.valueOf(splitted[i + 2].substring(2, 4)), Integer.valueOf(splitted[i + 2].substring(5, 9)), splitted[i + 3]);
            itemObjects.add(a);
        }
        scan2.close();
    }

    public String getObjectsAsFormattedString() {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < itemObjects.size(); i++) {
            result.add(itemObjects.get(i).getDescription() + ";" + itemObjects.get(i).getValue() + ";" + itemObjects.get(i).getMonth() + "/" + itemObjects.get(i).getDay() + "/" + itemObjects.get(i).getYear() + ";" + itemObjects.get(i).getCategory());
        }
        return String.join(";", result);
    }
}
