package com.example.marcelolongen.expensemanager;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ramotion.foldingcell.FoldingCell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class DetailsActivity extends AppCompatActivity {
    private Database db;
    private DatabaseReference root;
    private DatabaseReference user;
    private int finalI;

    private ArrayList<Item> displayedItems;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        // get our list view
        ListView theListView = (ListView)findViewById(R.id.mainListView);

        Intent intent = getIntent();
        userName = intent.getStringExtra("user");

        root = FirebaseDatabase.getInstance().getReference();
        user = root.child("users").child(userName).child("Expenses");
        // prepare elements to display
        db = new Database();
        db.readContentsFromFile(userName);


        displayedItems = new ArrayList<>();
        displayedItems.addAll(db.getItemObjects());




        // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
        final FoldingCellListAdapter adapter = new FoldingCellListAdapter(this, displayedItems);

        // set elements to adapter
        theListView.setAdapter(adapter);

        // set on click event listener to list view
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // toggle clicked cell state
                ((FoldingCell) view).toggle(false);
                // register in adapter that state for selected cell is toggled
                adapter.registerToggle(pos);
            }
        });



        RelativeLayout details = findViewById(R.id.leftTitle);

        final Spinner monthSpinner = findViewById(R.id.monthSpinner);


        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (monthSpinner.getSelectedItem().toString().equals("All")) {
                    displayedItems.clear();
                    System.out.println(db.getItemObjects().size());
                    displayedItems.addAll(db.getItemObjects());
                    adapter.notifyDataSetChanged();
                    deleteButtonClick(adapter);



                }
                else if (monthSpinner.getSelectedItem().toString().equals("January")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 1) {
                            displayedItems.add(db.getItemObjects().get(i));
                            deleteButtonClick(adapter);
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("February")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 2) {
                            displayedItems.add(db.getItemObjects().get(i));
                            deleteButtonClick(adapter);
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("March")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 3) {
                            displayedItems.add(db.getItemObjects().get(i));
                            deleteButtonClick(adapter);
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("April")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 4) {
                            displayedItems.add(db.getItemObjects().get(i));
                            deleteButtonClick(adapter);
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("May")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 5) {
                            displayedItems.add(db.getItemObjects().get(i));
                            deleteButtonClick(adapter);
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("June")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 6) {
                            displayedItems.add(db.getItemObjects().get(i));
                            deleteButtonClick(adapter);
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("July")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 7) {
                            displayedItems.add(db.getItemObjects().get(i));
                            deleteButtonClick(adapter);
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("August")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 8) {
                            displayedItems.add(db.getItemObjects().get(i));
                            deleteButtonClick(adapter);
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("September")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 9) {
                            displayedItems.add(db.getItemObjects().get(i));
                            deleteButtonClick(adapter);
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("October")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 10) {
                            displayedItems.add(db.getItemObjects().get(i));
                            deleteButtonClick(adapter);
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("November")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 11) {
                            displayedItems.add(db.getItemObjects().get(i));
                            deleteButtonClick(adapter);
                        }

                    }
                }

                else if (monthSpinner.getSelectedItem().toString().equals("December")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 12) {
                            displayedItems.add(db.getItemObjects().get(i));
                            deleteButtonClick(adapter);
                        }

                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        deleteButtonClick(adapter);

    }

    private void deleteButtonClick(final FoldingCellListAdapter adapter) {
        if (!db.getItemObjects().isEmpty()) {
            for (int i = 0; i < db.getItemObjects().size();i++) {
                finalI = i;
                db.getItemObjects().get(i).setRequestBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        String id = db.getItemObjects().get(finalI).getId();
                        user.child(id).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    Toast.makeText(getApplicationContext(), "Successfully deleted.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error deleted.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        db.getItemObjects().remove(finalI);
                        displayedItems.remove(finalI);
                        adapter.notifyDataSetChanged();
                        deleteButtonClick(adapter);
                    }



                });
            }
        }
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
