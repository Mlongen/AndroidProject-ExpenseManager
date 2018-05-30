package com.example.marcelolongen.expensemanager;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Overview extends AppCompatActivity {
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        db = new Database();
        try {
            db.readContentsFromFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ListView lv = findViewById(R.id.lv);
        MyAdapter mAdapter = new MyAdapter(this, R.layout.mylist_layout, db.getItemObjects());
        lv.setAdapter(mAdapter);

        Button detailsButton = findViewById(R.id.detailsButton);

        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), DetailsActivity.class);
                startActivity(intent);
            }
        });

        Button graphButton = findViewById(R.id.graphsButton);

        graphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), GraphView.class);
                startActivity(intent);
            }
        });





        FloatingActionButton fb = findViewById(R.id.fab);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddExpense.class);
                startActivity(intent);
            }
        });
    }




    private class MyAdapter extends ArrayAdapter {
        private int layoutResourceId;
        private ArrayList<Item> data;
        // constructor

        public MyAdapter(@NonNull Context context, int resource,  @NonNull ArrayList<Item> data) {
            super(context, resource, data);
            this.layoutResourceId = resource;
            this.data = data;
        }


        // getView


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //1. inflate our xml layout, use it to convert xml to an actual view
            convertView = getLayoutInflater().inflate(layoutResourceId, parent, false);
            //2. set img view and text view
            TextView date = convertView.findViewById(R.id.listDate);
            TextView description = convertView.findViewById(R.id.listDescription);
            TextView value = convertView.findViewById(R.id.listValue);
            date.setText(data.get(position).getDay() + "/" + data.get(position).getMonth());
            description.setText(data.get(position).getDescription());
            value.setText(data.get(position).getValue().toString());
            return convertView;
        }
    }





    }



