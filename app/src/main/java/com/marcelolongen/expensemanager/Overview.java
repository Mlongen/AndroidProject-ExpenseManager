package com.marcelolongen.expensemanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import com.marcelolongen.expensemanager.R;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class Overview extends AppCompatActivity {
    private Database db;
    private String userName;
    private BoomMenuButton bmb;
    private DatabaseReference root;
    private DatabaseReference user;
    private TabLayout myTab;
    private ViewPager myPager;
    private EditText descriptionText;
    private Spinner spinner;
    private EditText amountText;
    private DatePicker datePicker;
    private static DetailsFragment detailsFragment;
    private static OverviewFragment overviewFragment;
    private static GraphFragment graphFragment;
    private MyPagerAdapter myAdapter;
    private FirebaseAuth mAuth;
    private static String baseString = "CAD";

    private Menu menu;


    public static final String MY_PREFERENCES = "myPrefs";

    private static double currentRate = 1.00;

    private Rates rates;
    private AlertDialog alertDialog;
    private static String jSON;
    private String ratesLastUpdated;

    public static String getBaseString() {
        return baseString;
    }

    public static double getCurrentRate() {
        return currentRate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_overview);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Intent intent = getIntent();
        userName = intent.getStringExtra("user");
        db = Database.getInstance();
        root = FirebaseDatabase.getInstance().getReference();
        user = root.child("users").child(userName).child("Expenses");
        mAuth = FirebaseAuth.getInstance();

        try {
            jSON = fetchContent();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        rates = gson.fromJson(jSON, Rates.class);
        ratesLastUpdated = rates.getDate();

        SharedPreferences prefs = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        String restoredBaseString = prefs.getString("base", null);
        if (restoredBaseString != null) {
            baseString = prefs.getString("base", "CAD");//"No name defined" is the default value.
            currentRate = Double.valueOf(prefs.getString("rate", "1.00"));
        }

        String[] names = {"Settings", "Log out"};


        bmb = findViewById(R.id.bmb);
        assert bmb != null;




        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.settings);
        images.add(R.drawable.logout);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_2);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_2);
        bmb.setButtonTopMargin(1000);
        bmb.setBackgroundEffect(true);

        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            HamButton.Builder builder = new HamButton.Builder()
                    .normalImageRes(images.get(i))
                    .normalText(names[i])
                    .pieceColor(Color.WHITE)
                    .shadowEffect(true)
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            if (index == 1) {
                                FirebaseAuth.getInstance().signOut();
                                LoginManager.getInstance().logOut();
                                Toasty.success(getApplicationContext(), "Successfully logged out.").show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            } else if (index == 0) {
                                showUpdateDialog();
                            }



                        }
                    });

            bmb.addBuilder(builder);

        }









        myTab = findViewById(R.id.tabLayout);
        myPager = findViewById(R.id.pager);

        myAdapter = new MyPagerAdapter(getSupportFragmentManager());
        myPager.setOffscreenPageLimit(3);
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
        updateTitle(menu);
        this.menu = menu;
        return true;
    }


    private void updateTitle(Menu menu) {
        if (!baseString.equals("CAD")) {
            MenuItem currencyText = menu.findItem(R.id.current_currency);
            currencyText.setTitle("Converting to: " + baseString);

        } else {
            MenuItem currencyText = menu.findItem(R.id.current_currency);
            currencyText.setTitle("");
        }
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
            DisplayMetrics metrics = this.getResources().getDisplayMetrics();
            int height = metrics.heightPixels;
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
                                        Toasty.success(getApplicationContext(), "Entry added succesfully.", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();


                                      InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                      imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                      ArrayList<Item> items = detailsFragment.getDisplayedItems();
                                      items.add(0, newItem);

                                        updateFragments();

                                    }





                        }

                    }
                })
                .setExpanded(true, (int)(height / 2.2))  // This will enable the expand feature, (similar to android L share dialog)
                .setContentHolder(new ViewHolder(R.layout.add))
                .setCancelable(true)
                .create();
                 dialog.show();
            Toasty.Config.getInstance().setInfoColor(getResources().getColor(R.color.colorPrimary)).apply();
        descriptionText = (EditText) dialog.findViewById(R.id.add_description);
        descriptionText.requestFocus();
        InputMethodManager imm = (InputMethodManager)  getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(descriptionText, InputMethodManager.SHOW_IMPLICIT);

            TextView alert = (TextView) dialog.findViewById(R.id.currency_alert);
            alert.setVisibility(View.INVISIBLE);
            if (!getBaseString().equals("CAD")) {
                alert.setVisibility(View.VISIBLE);
            }
    }

    public static void updateFragments() {
        //updating details fragment
        detailsFragment.getAdapter().notifyDataSetChanged();
       detailsFragment.deleteButtonClick(detailsFragment.getAdapter());
        //updating overview fragment

        View overView = overviewFragment.getThisView();
        overviewFragment.updateData(overView);

        //trying to update graphfragment
        View graphView = graphFragment.getThisView();
        graphFragment.updatePieChart();
//        graphFragment.updateBarChart();
    }


    private void showUpdateDialog() {
        //1. build the dialog with the custom layout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.settings_dialog, null);
        builder.setView(dialogView);


        TextView rate = dialogView.findViewById(R.id.rateUpdatedOn);
        rate.setText("Rates were last updated on: " + ratesLastUpdated);

        final Spinner currencySpinner = dialogView.findViewById(R.id.currencySpinner);

        Button updateBtn = dialogView.findViewById(R.id.change_currency_btn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currencySpinner.getSelectedItem().toString().trim().equals("None")) {
                    showCurrencyDialog();

                    double thisRate = rates.getRates().get(currencySpinner.getSelectedItem().toString().trim());
                    currentRate = thisRate;
                    baseString = currencySpinner.getSelectedItem().toString().trim();

                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE).edit();
                    editor.putString("base", baseString);
                    editor.putString("rate", String.valueOf(currentRate));
                    editor.apply();




                    //updating details fragment
                    updateFragments();



                    Toasty.success(getApplicationContext(),  "Currency changed to: " + baseString, Toast.LENGTH_SHORT).show();
                    updateTitle(menu);
                } else {
                    currentRate = 1.00;
                    baseString = "CAD";

                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE).edit();
                    editor.putString("base", baseString);
                    editor.putString("rate", String.valueOf(currentRate));
                    editor.apply();
                    //updating details fragment
                    updateFragments();



                    Toasty.success(getApplicationContext(),  "Currency changed to: " + baseString, Toast.LENGTH_SHORT).show();
                    updateTitle(menu);
                    alertDialog.dismiss();
                }

            }
        });
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }


    public static String fetchContent() throws IOException {

        final int OK = 200;
        URL url = new URL("https://frankfurter.app/current?from=CAD");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        int responseCode = connection.getResponseCode();
        if(responseCode == OK){
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            jSON = response.toString();
            return response.toString();
        }

        return null;
    }


    private void showCurrencyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("ATTENTION: When adding a new expense, always use CAD.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alertDialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


}



