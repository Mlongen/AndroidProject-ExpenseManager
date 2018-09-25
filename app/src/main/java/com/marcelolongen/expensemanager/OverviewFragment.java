package com.marcelolongen.expensemanager;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.marcelolongen.expensemanager.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class OverviewFragment extends Fragment {
    private Database db;


    private static final String ARG_USER_NAME = "userName";

    private View thisView;
    private String currentMonth;

    public OverviewFragment() {
        // Required empty public constructor
    }
    private double currentRate;
    private String currentRateName;

    public static OverviewFragment newInstance(String userName) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER_NAME, userName);

        OverviewFragment fragment = new OverviewFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public View getThisView() {
        return thisView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = Database.getInstance();
        currentRate = Overview.getCurrentRate();
        currentRateName = Overview.getBaseString();

        if (currentMonth == null) {
            currentMonth = new SimpleDateFormat("M").format(Calendar.getInstance().getTime());
        }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thisView = inflater.inflate(R.layout.fragment_overview, container, false);

        Spinner monthSpinner = thisView.findViewById(R.id.monthSpinner);
        if (currentMonth == null) {
            currentMonth = new SimpleDateFormat("M").format(Calendar.getInstance().getTime());
        }



        for (int i = 0; i < 13;i++) {
            if (Integer.valueOf(currentMonth) == i) {
                monthSpinner.setSelection(i - 1);
            }
        }
        monthSpinnerClickListener(monthSpinner);

        return thisView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateData(thisView);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){

        }
        else{
            //no
        }

    }

    public void updateData(View view) {
        currentRate = Overview.getCurrentRate();
        currentRateName = Overview.getBaseString();
        String[] categories = {"Food", "Bills", "Housing", "Health", "Social Life", "Apparel", "Beauty", "Education", "Other"};
        double[] monthSum = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        @SuppressLint("SimpleDateFormat") String thisMonth = new SimpleDateFormat("M").format(Calendar.getInstance().getTime());

        //clearing data
        for(int i = 0; i < db.getItemObjects().size(); i++) {
            for (int j = 0; j < 9; j++) {
                if (db.getItemObjects().get(i).getCategory().equals(categories[j])&& db.getItemObjects().get(i).getMonth().toString().equals(currentMonth)) {
                    monthSum[j] += db.getItemObjects().get(i).getValue();
                }
            }

        }

        double sumTotal = (monthSum[0] + monthSum[1] + monthSum[2] + monthSum[3] + monthSum[4] + monthSum[5] + monthSum[6] + monthSum[7] + monthSum[8]) * currentRate;


        TextView totalSum = view.findViewById(R.id.totalSum);
        totalSum.setText(currentRateName + ": " + String.format("%.2f",currentRate * sumTotal));


        TextView totalLabel = view.findViewById(R.id.totalLabel);

        if (sumTotal == 0) {
            totalLabel.setText("No data");
        } else {
            totalLabel.setText("Total: ");
        }


        LinearLayout foodLayout = view.findViewById(R.id.layoutFood);
        foodLayout.setVisibility(View.GONE);

        LinearLayout billsLayout = view.findViewById(R.id.layoutBills);
        billsLayout.setVisibility(View.GONE);

        LinearLayout housingLayout = view.findViewById(R.id.layoutHousing);
        housingLayout.setVisibility(View.GONE);

        LinearLayout healthLayout = view.findViewById(R.id.layoutHealth);
        healthLayout.setVisibility(View.GONE);

        LinearLayout socialLifeLayout = view.findViewById(R.id.layoutSocialLife);
        socialLifeLayout.setVisibility(View.GONE);

        LinearLayout apparelLayout = view.findViewById(R.id.layoutApparel);
        apparelLayout.setVisibility(View.GONE);


        LinearLayout beautyLayout = view.findViewById(R.id.layoutBeauty);
        beautyLayout.setVisibility(View.GONE);

        LinearLayout educationLayout = view.findViewById(R.id.layoutEducation);
        educationLayout.setVisibility(View.GONE);

        TextView otherSum = view.findViewById(R.id.other_value);
        otherSum.setVisibility(View.GONE);

        if (monthSum[0] > 0) {
            TextView foodSum = view.findViewById(R.id.food_value);
            foodLayout.setVisibility(View.VISIBLE);
            foodSum.setText(currentRateName + ": " + String.format("%.2f",currentRate * monthSum[0]));
        }
        if (monthSum[1] > 0) {
            TextView billsSum = view.findViewById(R.id.bills_value);

            billsLayout.setVisibility(View.VISIBLE);
            billsSum.setText(currentRateName + ": " + String.format("%.2f",currentRate * monthSum[1]));
        }
        if (monthSum[2] > 0) {
            TextView housingSum = view.findViewById(R.id.housing_value);

            housingLayout.setVisibility(View.VISIBLE);
            housingSum.setText(currentRateName + ": " + String.format("%.2f",currentRate * monthSum[2]));
        }
        if (monthSum[3] > 0) {
            TextView healthSum = view.findViewById(R.id.health_value);

            healthLayout.setVisibility(View.VISIBLE);
            healthSum.setText(currentRateName + ": " + String.format("%.2f",currentRate * monthSum[3]));
        }
        if (monthSum[4] > 0) {
            TextView socialLifeSum = view.findViewById(R.id.social_life_value);

            socialLifeLayout.setVisibility(View.VISIBLE);
            socialLifeSum.setText(currentRateName + ": " + String.format("%.2f",currentRate * monthSum[4]));
        }
        if (monthSum[5] > 0) {
            TextView apparelSum = view.findViewById(R.id.apparel_value);

            apparelLayout.setVisibility(View.VISIBLE);
            apparelSum.setText(currentRateName + ": " + String.format("%.2f",currentRate * monthSum[5]));
        }
        if (monthSum[6] > 0) {
            TextView beautySum = view.findViewById(R.id.beauty_value);

            beautyLayout.setVisibility(View.VISIBLE);
            beautySum.setText(currentRateName + ": " + String.format("%.2f",currentRate * monthSum[6]));
        }
        if (monthSum[7] > 0) {
            TextView educationSum = view.findViewById(R.id.education_value);

            educationLayout.setVisibility(View.VISIBLE);
            educationSum.setText(currentRateName + ": " + String.format("%.2f",currentRate * monthSum[7]));
        }
        if (monthSum[8] > 0) {

            otherSum.setVisibility(View.VISIBLE);
            otherSum.setText(currentRateName + ": " + String.format("%.2f",currentRate * monthSum[8]));
        }





    }
    public void monthSpinnerClickListener(final Spinner monthSpinner) {
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (monthSpinner.getSelectedItem().toString().equals("January")) {
                    currentMonth = String.valueOf(1);
                    updateData(thisView);

                }
                else if (monthSpinner.getSelectedItem().toString().equals("February")) {
                    currentMonth = String.valueOf(2);
                    updateData(thisView);
                }
                else if (monthSpinner.getSelectedItem().toString().equals("March")) {
                    currentMonth = String.valueOf(3);
                    updateData(thisView);
                }
                else if (monthSpinner.getSelectedItem().toString().equals("April")) {
                    currentMonth = String.valueOf(4);
                    updateData(thisView);
                }
                else if (monthSpinner.getSelectedItem().toString().equals("May")) {
                    currentMonth = String.valueOf(5);
                    updateData(thisView);
                }
                else if (monthSpinner.getSelectedItem().toString().equals("June")) {
                    currentMonth = String.valueOf(6);
                    updateData(thisView);
                }
                else if (monthSpinner.getSelectedItem().toString().equals("July")) {
                    currentMonth = String.valueOf(7);
                    updateData(thisView);
                }
                else if (monthSpinner.getSelectedItem().toString().equals("August")) {
                    currentMonth = String.valueOf(8);
                    updateData(thisView);
                }
                else if (monthSpinner.getSelectedItem().toString().equals("September")) {
                    currentMonth = String.valueOf(9);
                    updateData(thisView);
                }
                else if (monthSpinner.getSelectedItem().toString().equals("October")) {
                    currentMonth = String.valueOf(10);
                    updateData(thisView);
                }
                else if (monthSpinner.getSelectedItem().toString().equals("November")) {
                    currentMonth = String.valueOf(11);
                    updateData(thisView);
                }

                else if (monthSpinner.getSelectedItem().toString().equals("December")) {
                    currentMonth = String.valueOf(12);
                    updateData(thisView);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

}
