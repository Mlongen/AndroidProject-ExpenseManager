package com.example.marcelolongen.expensemanager;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ramotion.foldingcell.FoldingCell;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends android.support.v4.app.Fragment{

    private static final String ARG_USER_NAME= "username";
    private Database db;
    private DatabaseReference root;
    private DatabaseReference user;
    private int finalI;
    private FoldingCellListAdapter foldingCellListAdapter;
    private ArrayList<Item> displayedItems;
    private String userName;
    public Spinner monthSpinner;
    private String currentMonth;
    private OverviewFragment overviewFragment;
    private GraphFragment graphFragment;
    private double currentRate;
    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(String userName) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER_NAME, userName);

        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }





    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            monthSpinnerClickListener(monthSpinner);
        }
        else{
            //no
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userName = (String) getArguments().getSerializable(ARG_USER_NAME);

        }
        currentRate = Overview.getCurrentRate();
        root = FirebaseDatabase.getInstance().getReference();
        user = root.child("users").child(userName).child("Expenses");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_details, container, false);


        // get our list view
        ListView theListView = (ListView)v.findViewById(R.id.mainListView);


        // prepare elements to display
        db = Database.getInstance();


        displayedItems = new ArrayList<>();





        // create custom foldingCellListAdapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
        foldingCellListAdapter = new FoldingCellListAdapter(v.getContext(), displayedItems);



        // set elements to foldingCellListAdapter
        theListView.setAdapter(foldingCellListAdapter);

        // set on click event listener to list view
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // toggle clicked cell state
                ((FoldingCell) view).toggle(false);
                // register in foldingCellListAdapter that state for selected cell is toggled
                foldingCellListAdapter.registerToggle(pos);
                finalI = pos;
            }
        });



        RelativeLayout details = v.findViewById(R.id.leftTitle);

        monthSpinner = v.findViewById(R.id.monthSpinner);

        if (currentMonth == null) {
            currentMonth = new SimpleDateFormat("M").format(Calendar.getInstance().getTime());
        }



        for (int i = 0; i < 13;i++) {
            if (Integer.valueOf(currentMonth) == i) {
                monthSpinner.setSelection(i);
            }
        }
        foldingCellListAdapter.notifyDataSetChanged();
        monthSpinnerClickListener(monthSpinner);
        deleteButtonClick(foldingCellListAdapter);


        return v;
    }

    public ArrayList<Item> getDisplayedItems() {
        return displayedItems;
    }

    public void monthSpinnerClickListener(final Spinner monthSpinner) {
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (monthSpinner.getSelectedItem().toString().equals("All")) {
                    displayedItems.clear();
                    displayedItems.addAll(db.getItemObjects());

                    foldingCellListAdapter.notifyDataSetChanged();
                    deleteButtonClick(foldingCellListAdapter);



                }
                else if (monthSpinner.getSelectedItem().toString().equals("January")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 1) {
                            displayedItems.add(db.getItemObjects().get(i));
                            deleteButtonClick(foldingCellListAdapter);
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("February")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 2) {
                            displayedItems.add(db.getItemObjects().get(i));
                            deleteButtonClick(foldingCellListAdapter);
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("March")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 3) {
                            displayedItems.add(db.getItemObjects().get(i));
                            deleteButtonClick(foldingCellListAdapter);
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("April")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 4) {
                            displayedItems.add(db.getItemObjects().get(i));
                            deleteButtonClick(foldingCellListAdapter);
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("May")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 5) {
                            displayedItems.add(db.getItemObjects().get(i));
                            deleteButtonClick(foldingCellListAdapter);
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("June")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 6) {
                            displayedItems.add(db.getItemObjects().get(i));
                            deleteButtonClick(foldingCellListAdapter);
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("July")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 7) {
                            displayedItems.add(db.getItemObjects().get(i));
                            deleteButtonClick(foldingCellListAdapter);
                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("August")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 8) {
                            displayedItems.add(db.getItemObjects().get(i));
                            deleteButtonClick(foldingCellListAdapter);

                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("September")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 9) {
                            displayedItems.add(db.getItemObjects().get(i));
                            deleteButtonClick(foldingCellListAdapter);

                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("October")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 10) {
                            displayedItems.add(db.getItemObjects().get(i));
                            deleteButtonClick(foldingCellListAdapter);

                        }

                    }
                }
                else if (monthSpinner.getSelectedItem().toString().equals("November")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 11) {
                            displayedItems.add(db.getItemObjects().get(i));
                            deleteButtonClick(foldingCellListAdapter);

                        }

                    }
                }

                else if (monthSpinner.getSelectedItem().toString().equals("December")) {
                    displayedItems.clear();
                    for (int i = 0; i < db.getItemObjects().size(); i++) {
                        if (db.getItemObjects().get(i).getMonth() == 12) {
                            displayedItems.add(db.getItemObjects().get(i));
                            deleteButtonClick(foldingCellListAdapter);

                        }

                    }
                }
                foldingCellListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    public  FoldingCellListAdapter getAdapter() {
        return this.foldingCellListAdapter;
    }

    //TODO: FIX DELETE BUTTON CODE
   public void deleteButtonClick(final FoldingCellListAdapter adapter) {
        if (!db.getItemObjects().isEmpty()) {
            for (int i = 0; i < db.getItemObjects().size(); i++) {
                final int finalI1 = i;
                db.getItemObjects().get(i).setRequestBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        String id = db.getItemObjects().get(finalI1).getId();

                        user.child(id).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    Toast.makeText(getApplicationContext(), "Successfully deleted.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error deleting.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        db.getItemObjects().remove(finalI);
                        displayedItems.remove(finalI);
                        adapter.notifyDataSetChanged();
                        Overview.updateFragments();
                        deleteButtonClick(adapter);

                    }



                });
            }
        }
    }


}
