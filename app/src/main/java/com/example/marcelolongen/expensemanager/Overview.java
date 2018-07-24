package com.example.marcelolongen.expensemanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class Overview extends AppCompatActivity {
    private Database db;
    private String userName;
    private DatabaseReference root;
    private DatabaseReference user;
    private TabLayout myTab;
    private ViewPager myPager;
    private Button addButton;
    private EditText descriptionText;
    private Spinner spinner;
    private EditText amountText;
    private DatePicker datePicker;
    private DetailsFragment detailsFragment;
    private OverviewFragment overviewFragment;
    private GraphFragment graphFragment;
    private MyPagerAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        Intent intent = getIntent();
        userName = intent.getStringExtra("user");

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.fragmentContainer, newInstance)
//                .addToBackStack(null)
//                .commit(); // just do it
        db = Database.getInstance();
                root = FirebaseDatabase.getInstance().getReference();
        user = root.child("users").child(userName).child("Expenses");




        myTab = findViewById(R.id.tabLayout);
        myPager = findViewById(R.id.pager);
        Toasty.success(this, "Size: " + db.getItemObjects().size(), Toast.LENGTH_SHORT).show();

        myAdapter = new MyPagerAdapter(getSupportFragmentManager());
        myPager.setAdapter(myAdapter);

        myTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                myPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }



    class MyPagerAdapter extends FragmentStatePagerAdapter {
        String[] data = {"Overview", "Details", "Graph"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    overviewFragment = OverviewFragment.newInstance(userName);
                    return overviewFragment;
                case 1:
                    detailsFragment = DetailsFragment.newInstance(userName);
                    return detailsFragment;
                case 2:
                    graphFragment = GraphFragment.newInstance(userName);
                    return graphFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return data[position];
        }

    }


        public void addExpense(MenuItem item) {
        DialogPlus dialog = DialogPlus.newDialog(Overview.this)
                .setGravity(Gravity.TOP)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        switch (view.getId()) {
                            case R.id.submitButton:
                                descriptionText = findViewById(R.id.add_description);
                                spinner = findViewById(R.id.spinner);
                                amountText = findViewById(R.id.amount);
                                datePicker = findViewById(R.id.date);
                                String description = descriptionText.getText().toString();
                                //FIGURE OUT WHAT IS CAUSING THE DATEPICKER BUG (THAT I HAD TO USE +1 TO FIX)
                                String date = String.valueOf(
                                        (datePicker.getMonth() +1) + "/" +
                                                datePicker.getDayOfMonth() + "/" +
                                                datePicker.getYear());
                                String amount = amountText.getText().toString();
                                String category = spinner.getSelectedItem().toString();


                                    if (description.matches("[A-Za-z]{1,20}") &&
                                            amount.matches("[0-9.]{1,10}")) {

                                        String id = user.push().getKey();
                                        Item newItem = new Item(id, description, Double.valueOf(amount), datePicker.getMonth() + 1, datePicker.getDayOfMonth(), datePicker.getYear(), category);
                                        user.child(id).setValue(newItem);

                                        db.getItemObjects().add(newItem);
                                        Toasty.success(getApplicationContext(), description + " added succesfully.", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();


                                      InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                      imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                      ArrayList<Item> items = detailsFragment.getDisplayedItems();
                                      items.add(0, newItem);
                                      //updating details fragment
                                      detailsFragment.getAdapter().notifyDataSetChanged();

                                      //updating overview fragment

                                       View overView = overviewFragment.getThisView();
                                       overviewFragment.updateData(overView);

                                      //trying to update graphfragment




                                    }





                        }

                    }
                })
                .setExpanded(true, 700)  // This will enable the expand feature, (similar to android L share dialog)
                .setContentHolder(new ViewHolder(R.layout.add))
                .setCancelable(true)
                .create();
        dialog.show();
            Toasty.Config.getInstance().setInfoColor(getResources().getColor(R.color.colorPrimary)).apply();
        descriptionText = (EditText) dialog.findViewById(R.id.add_description);
        descriptionText.requestFocus();
        InputMethodManager imm = (InputMethodManager)  getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(descriptionText, InputMethodManager.SHOW_IMPLICIT);
    }




}



