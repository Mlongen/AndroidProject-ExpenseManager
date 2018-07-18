package com.example.marcelolongen.expensemanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.EnumsAlign;
import com.anychart.anychart.LegendLayout;
import com.anychart.anychart.Pie;
import com.anychart.anychart.ValueDataEntry;
import com.anychart.anychart.chart.common.Event;
import com.anychart.anychart.chart.common.ListenersInterface;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Overview extends AppCompatActivity {
    public Database db;
    private String userName;

    private TabLayout myTab;
    private ViewPager myPager;


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
        db = new Database();
        db.readContentsFromFile(userName);

        myTab = findViewById(R.id.tabLayout);
        myPager = findViewById(R.id.pager);

        myPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

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



        class MyPagerAdapter extends FragmentPagerAdapter {
            String[] data = {"Overview", "Details"};

            public MyPagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        myPager.getAdapter().notifyDataSetChanged();
                        return OverviewFragment.newInstance(userName);
                    case 1:
                        myPager.getAdapter().notifyDataSetChanged();
                        return DetailsFragment.newInstance(userName);

                }
                return null;
            }

            @Override
            public int getCount() {
                return data.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return data[position];
            }

        }


//        root = FirebaseDatabase.getInstance().getReference();
//        user = root.child("users").child(userName).child("Expenses");
//        System.out.println(user);
//
//
//        db = new Database();
//        db.readContentsFromFile(userName);
//
//
//        final ArrayList<Class> classes = new ArrayList<>();
//        classes.add(DetailsActivity.class);
//        classes.add(GraphView.class);
//        String[] names = {"Detailed list", "Settings"};
//
//        ImageButton addNew = findViewById(R.id.addExpense);
//        addNew.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addExpense();;
//
//            }
//        });
//        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
//
//        Pie pie = AnyChart.pie();
//
//        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
//            @Override
//            public void onClick(Event event) {
//
//            }
//        });
//
//
//        updateData();
//
//
//        List<DataEntry> data = new ArrayList<>();
//        data.add(new ValueDataEntry("Food", sum[0]));
//        data.add(new ValueDataEntry("Bills", sum[1]));
//        data.add(new ValueDataEntry("Housing", sum[2]));
//        data.add(new ValueDataEntry("Health", sum[3]));
//        data.add(new ValueDataEntry("Social Life", sum[4]));
//        data.add(new ValueDataEntry("Apparel", sum[5]));
//        data.add(new ValueDataEntry("Beauty", sum[6]));
//        data.add(new ValueDataEntry("Education", sum[7]));
//        data.add(new ValueDataEntry("Other", sum[8]));
//
//        pie.setData(data);
//
//        pie.setTitle("Detailed expenses for this month:");
//
//        pie.getLabels().setPosition("outside");
//
//        pie.getLegend().getTitle().setEnabled(true);
//        pie.getLegend().getTitle()
//                .setText("Categories")
//                .setPadding(0d, 0d, 10d, 0d);
//
//        pie.getLegend()
//                .setPosition("center-bottom")
//                .setItemsLayout(LegendLayout.HORIZONTAL)
//                .setAlign(EnumsAlign.CENTER);
//        anyChartView.setChart(pie);
//        //Adding BMB
//
//        bmb = (BoomMenuButton) findViewById(R.id.bmb);
//        assert bmb != null;
//
//
//
//        ArrayList<Integer> images = new ArrayList<>();
//        images.add(R.drawable.listing_option);
//        images.add(R.drawable.settings);
//        bmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_2);
//        bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_2);
//        bmb.setButtonTopMargin(1000);
//        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
//            HamButton.Builder builder = new HamButton.Builder()
//                    .normalImageRes(images.get(i))
//                    .normalText(names[i])
//                    .shadowEffect(true)
//                    .listener(new OnBMClickListener() {
//                        @Override
//                        public void onBoomButtonClick(int index) {
//                            // When the boom-button corresponding this builder is clicked.
//                            Fragment newInstance = DetailsFragment.newInstance(userName);
//                            FragmentManager fragmentManager = getSupportFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.add(R.id.fragmentContainer, newInstance)
//                                    .addToBackStack(null)
//                                    .commit(); // just do it
//
////
////                                Intent intent = new Intent(getApplicationContext(), classes.get(index));
////                                if (index == 0) {
////                                    intent.putExtra("user", userName);
////                                }
////                                startActivity(intent);
//                        }
//                    })
//                    ;
//®
//            bmb.addBuilder(builder);
//        }
    }

//
//    private void updateData() {
//        String[] categories = {"Food", "Bills", "Housing", "Health", "Social Life", "Apparel", "Beauty", "Education", "Other"};
//
//        @SuppressLint("SimpleDateFormat") String thisMonth = new SimpleDateFormat("M").format(Calendar.getInstance().getTime());
//
//        //clearing data
//        for (int i = 0; i < sum.length; i++) {
//            sum[i] = 0.0;
//        }
//        for (int i = 0; i< db.getItemObjects().size(); i++ ) {
//            System.out.println(db.getItemObjects().size());
//            for (int j = 0; j < categories.length;j++) {
//                if (db.getItemObjects().get(i).getCategory().equals(categories[j])&& db.getItemObjects().get(i).getMonth().toString().equals(thisMonth) ) {
//                    sum[j] += db.getItemObjects().get(i).getValue();
//                }
//            }
//        }
//
//
//        TextView highestSpending = findViewById(R.id.highestCategory);
//        TextView highestName = findViewById(R.id.highestName);
//
//        TextView totalSum = findViewById(R.id.totalSum);
//        double sumTotal = sum[0] + sum[1] + sum[2] + sum[3] + sum[4] + sum[5] + sum[6] + sum[7] + sum[8];
//        totalSum.setText("CAD: " + String.valueOf(sumTotal) + "0");
//        double highestValue = sum[0];
//        int highestIndex = 0;
//        for (int i = 0; i < sum.length; i++) {
//            if (sum[i] > highestValue) {
//                highestValue = sum[i];
//                highestIndex = i;
//            }
//        }
//        highestSpending.setText("CAD: " + String.valueOf(highestValue) + "0");
//        highestName.setText(categories[highestIndex]);
//    }
//
//    private void addExpense() {
//        DialogPlus dialog = DialogPlus.newDialog(Overview.this)
//                .setGravity(Gravity.TOP)
//                .setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(DialogPlus dialog, View view) {
//                        switch (view.getId()) {
//                            case R.id.submitButton:
//                                TextView descriptionText = findViewById(R.id.description);
//                                final Spinner spinner = findViewById(R.id.spinner);
//                                TextView amountText = findViewById(R.id.amount);
//                                DatePicker datePicker = findViewById(R.id.date);
//                                String description = descriptionText.getText().toString();
//                                //FIGURE OUT WHAT IS CAUSING THE DATEPICKER BUG (THAT I HAD TO USE +1 TO FIX)
//                                String date = String.valueOf(
//                                        (datePicker.getMonth() +1) + "/" +
//                                                datePicker.getDayOfMonth() + "/" +
//                                                datePicker.getYear());
//                                String amount = amountText.getText().toString();
//                                String category = spinner.getSelectedItem().toString();
//
//
//                                    if (description.matches("[A-Za-z]{1,20}") &&
//                                            amount.matches("[0-9.]{1,10}")) {
//
//                                        String id = user.push().getKey();
//                                        Item newItem = new Item(id, description, Double.valueOf(amount), datePicker.getMonth() + 1, datePicker.getDayOfMonth(), datePicker.getYear(), category);
//                                        user.child(id).setValue(newItem);
//
//
//                                        db.getItemObjects().add(newItem);
//
//                                        dialog.dismiss();
//                                       toast("Entry added.");
//                                       db.getItemObjects().clear();
//
//                                       db.readContentsFromFile(userName);
//
//                                      updateData();
//                                      InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                                      imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//                                    } else {
//                                     toast("Please input valid data.");
//                                }
//
//
//
//
//
//                        }
//
//                    }
//                })
//                .setExpanded(true, 700)  // This will enable the expand feature, (similar to android L share dialog)
//                .setContentHolder(new ViewHolder(R.layout.add))
//                .setCancelable(true)
//                .create();
//        dialog.show();
//        TextView descriptionText = findViewById(R.id.description);
//        descriptionText.requestFocus();
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(descriptionText, InputMethodManager.SHOW_IMPLICIT);
//    }
//
//    private void toast(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }
//
//






