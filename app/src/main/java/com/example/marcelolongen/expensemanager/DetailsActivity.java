package com.example.marcelolongen.expensemanager;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ramotion.foldingcell.FoldingCell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class DetailsActivity extends AppCompatActivity {
    private Database db;


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




        // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
        final FoldingCellListAdapter adapter = new FoldingCellListAdapter(this, db.getItemObjects());

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
