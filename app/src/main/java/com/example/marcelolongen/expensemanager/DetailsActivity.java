package com.example.marcelolongen.expensemanager;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

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

    private ArrayList<Item> displayedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        // get our list view
        ListView theListView = (ListView)findViewById(R.id.mainListView);


        // prepare elements to display
        db = new Database();
        try {
            db.readContentsFromFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

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


        // add custom btn handler to first list item
        deleteButtonClick(adapter);
        RelativeLayout details = findViewById(R.id.leftTitle);

        final Spinner monthSpinner = findViewById(R.id.monthSpinner);


        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (monthSpinner.getSelectedItem().toString().equals("All")) {
                    displayedItems.clear();
                    displayedItems.addAll(db.getItemObjects());
                    adapter.notifyDataSetChanged();



                }
                else if (monthSpinner.getSelectedItem().toString().equals("January")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 1) {
                            displayedItems.add(db.getItemObjects().get(i));
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("February")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 2) {
                            displayedItems.add(db.getItemObjects().get(i));
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("March")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 3) {
                            displayedItems.add(db.getItemObjects().get(i));
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("April")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 4) {
                            displayedItems.add(db.getItemObjects().get(i));
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("May")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 5) {
                            displayedItems.add(db.getItemObjects().get(i));
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("June")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 6) {
                            displayedItems.add(db.getItemObjects().get(i));
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("July")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 7) {
                            displayedItems.add(db.getItemObjects().get(i));
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("August")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 8) {
                            displayedItems.add(db.getItemObjects().get(i));
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("September")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 9) {
                            displayedItems.add(db.getItemObjects().get(i));
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("October")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 10) {
                            displayedItems.add(db.getItemObjects().get(i));
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("November")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 11) {
                            displayedItems.add(db.getItemObjects().get(i));
                        }

                    }
                }

                else if (monthSpinner.getSelectedItem().toString().equals("December")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 12) {
                            displayedItems.add(db.getItemObjects().get(i));
                        }

                    }
                }
                Collections.reverse(displayedItems);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void deleteButtonClick(final FoldingCellListAdapter adapter) {
        for (int i = 0; i < db.getItemObjects().size();i++) {
            final int finalI = i;
            db.getItemObjects().get(i).setRequestBtnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    db.getItemObjects().remove(finalI);
                    String formatted = db.getObjectsAsFormattedString();
                    String MYFILE = Environment.getExternalStorageDirectory().toString();

                    String fileName = "entries.txt";
                    File f = new File(MYFILE,fileName);

                    try (FileOutputStream fop = new FileOutputStream(f, false)) {

                        // if file doesn't exists, then create it
                        if (!f.exists()) {
                            f.createNewFile();
                        }

                        // get the content in bytes
                        byte[] contentInBytes = formatted.getBytes();

                        fop.write(contentInBytes);
                        fop.flush();
                        fop.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    toast("Item removed.");
                    displayedItems.remove(finalI);
                    adapter.notifyDataSetChanged();
                    deleteButtonClick(adapter);
                }



            });
        }
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
