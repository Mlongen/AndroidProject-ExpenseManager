package com.example.marcelolongen.expensemanager;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment {
    private double[] sum = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    private Database db;
    private static final String ARG_USER_NAME= "username";
    private PieChart mPieChart;
    private HorizontalBarChart mHBarChart;
    private PieData pieData;
    private BarData barData;
    private View thisView;
    private String currentMonth;
    public GraphFragment() {
        // Required empty public constructor
    }

    public View getThisView() {
        return thisView;
    }

    public static GraphFragment newInstance(String userName) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER_NAME, userName);

        GraphFragment fragment = new GraphFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = Database.getInstance();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thisView = inflater.inflate(R.layout.fragment_graph, container, false);



//        anyChartView = v.findViewById(R.id.any_chart_view);
//
//        Pie pie = AnyChart.pie();
//
//        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
//            @Override
//            public void onClick(Event event) {
//
//            }
//        });
        mPieChart = thisView.findViewById(R.id.piechart);
        mHBarChart = thisView.findViewById(R.id.horizontal_barchart);
        updatePieChart();
        updateBarChart();


        Spinner monthSpinner = thisView.findViewById(R.id.monthSpinner);
        monthSpinnerClickListener(monthSpinner);

        Button changeData = thisView.findViewById(R.id.change_data);
        changeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    sum[2] +=5000;
                      updatePieChart();
                      updateBarChart();
            }
        });



        return thisView;

    }


    public void updateBarChart() {
        String[] categories = {"Food", "Bills", "Housing", "Health", "Social Life", "Apparel", "Beauty", "Education", "Other"};
        float[] monthSum = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};

        for(int i = 0; i < db.getItemObjects().size(); i++) {
            for (int j = 0; j < 9; j++) {
                if (db.getItemObjects().get(i).getCategory().equals(categories[j])) {
                    monthSum[j] += (float)(double)db.getItemObjects().get(i).getValue();
                }
            }

        }

        mHBarChart.getXAxis().setEnabled(false);




        ArrayList<LegendEntry> legendEntries = new ArrayList<>();
        List<BarEntry> entries = new ArrayList<>();
        int current = 0;
        int currentColor = 0;
        if (monthSum[0] > 0) {
            entries.add(new BarEntry((float)current, monthSum[0]));
            LegendEntry l1=new LegendEntry("Food", Legend.LegendForm.DEFAULT,10f,2f,null, ColorTemplate.JOYFUL_COLORS[currentColor]);
            legendEntries.add(l1);
            current += 2;
            currentColor ++;
        }
        if (monthSum[1] > 0) {
            entries.add(new BarEntry((float)current, monthSum[1]));
            LegendEntry l1=new LegendEntry("Bills", Legend.LegendForm.DEFAULT,10f,2f,null, ColorTemplate.JOYFUL_COLORS[currentColor]);
            legendEntries.add(l1);
            current += 2;
            currentColor ++;
        }
        if (monthSum[2] > 0) {
            entries.add(new BarEntry((float)current, monthSum[2]));
            LegendEntry l1=new LegendEntry("Housing", Legend.LegendForm.DEFAULT,10f,2f,null, ColorTemplate.JOYFUL_COLORS[currentColor]);
            legendEntries.add(l1);
            current += 2;
            currentColor ++;
        }
        if (monthSum[3] > 0) {
            entries.add( new BarEntry((float)current, monthSum[3]));
            LegendEntry l1=new LegendEntry("Social Life", Legend.LegendForm.DEFAULT,10f,2f,null, ColorTemplate.JOYFUL_COLORS[currentColor]);
            legendEntries.add(l1);
            current += 2;
            currentColor ++;
        }
        if (monthSum[4] > 0) {
            entries.add(new BarEntry((float)current, monthSum[4]));
            LegendEntry l1=new LegendEntry("Apparel", Legend.LegendForm.DEFAULT,10f,2f,null, ColorTemplate.JOYFUL_COLORS[currentColor]);
            legendEntries.add(l1);
            current += 2;
            currentColor ++;
        }
        if (monthSum[5] > 0) {
            entries.add( new BarEntry((float)current, monthSum[5]));
            LegendEntry l1=new LegendEntry("Beauty", Legend.LegendForm.DEFAULT,10f,2f,null, ColorTemplate.JOYFUL_COLORS[currentColor]);
            legendEntries.add(l1);
            current += 2;
            currentColor ++;
        }
        if (monthSum[6] > 0) {
            entries.add(new BarEntry((float)current, monthSum[6]));
            LegendEntry l1=new LegendEntry("Education", Legend.LegendForm.DEFAULT,10f,2f,null, ColorTemplate.JOYFUL_COLORS[currentColor]);
            legendEntries.add(l1);
            current += 2;
            currentColor ++;
        }
        if (monthSum[7] > 0) {
            entries.add( new BarEntry((float)current, monthSum[7]));
            LegendEntry l1=new LegendEntry("Other", Legend.LegendForm.DEFAULT,10f,2f,null, ColorTemplate.JOYFUL_COLORS[currentColor]);
            legendEntries.add(l1);
            current += 2;
            currentColor ++;
        }
        if (monthSum[8] > 0) {
            entries.add( new BarEntry((float)current, monthSum[8]));
        }

        BarDataSet set = new BarDataSet(entries, "BarDataSet");
        set.setColors(ColorTemplate.JOYFUL_COLORS);



        Legend l = mHBarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(12f);
        l.setTextSize(14f);
        l.setXEntrySpace(8f);

        l.setCustom(legendEntries);





        barData = new BarData(set);
        barData.setValueTextSize(13f);
        barData.setValueTypeface(Typeface.DEFAULT);

        barData.setValueFormatter(new DefaultValueFormatter(2));
        mHBarChart.animateY(5000, Easing.EasingOption.Linear);
        mHBarChart.setData(barData);

        mHBarChart.invalidate();



    }



    public void updatePieChart() {
        String[] categories = {"Food", "Bills", "Housing", "Health", "Social Life", "Apparel", "Beauty", "Education", "Other"};

        if (currentMonth == null) {
            currentMonth = new SimpleDateFormat("M").format(Calendar.getInstance().getTime());
        }

        for (int i = 0; i < sum.length; i++) {
            sum[i] = 0.0;
        }

        for (int i = 0; i< db.getItemObjects().size(); i++ ) {

            for (int j = 0; j < categories.length;j++) {
                if (db.getItemObjects().get(i).getCategory().equals(categories[j])&& db.getItemObjects().get(i).getMonth().toString().equals(currentMonth) ) {
                    sum[j] += db.getItemObjects().get(i).getValue();
                }
            }
        }

        float totalSum = (float) sum[0] + (float) sum[1]
                + (float) sum[2] + (float) sum[3]
                + (float) sum[4] + (float) sum[5]
                + (float) sum[6] + (float) sum[7] + (float) sum[8];


        float foodSum = (float) ((sum[0] / totalSum) * 100);
        float billsSum = (float) ((sum[1] / totalSum) * 100);
        float housingSum = (float) ((sum[2] / totalSum) * 100);
        float healthSum = (float) ((sum[3] / totalSum) * 100);
        float socialLifeSum = (float) ((sum[4] / totalSum) * 100);
        float apparelSum = (float) ((sum[5] / totalSum) * 100);
        float beautySum = (float) ((sum[6] / totalSum) * 100);
        float educationSum = (float) ((sum[7] / totalSum) * 100);
        float otherSum = (float) ((sum[8] / totalSum) * 100);



        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(5, 10, 5, 5);

        mPieChart.setDragDecelerationFrictionCoef(0.95f);

        mPieChart.setCenterTextTypeface(Typeface.DEFAULT);

        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);

        mPieChart.setTransparentCircleColor(Color.WHITE);
        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setHoleRadius(58f);
        mPieChart.setTransparentCircleRadius(61f);

        mPieChart.setDrawCenterText(true);

        mPieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(true);


        List<PieEntry> entries = new ArrayList<>();

        if (foodSum > 0) {
            entries.add(new PieEntry(foodSum, "Food"));
        }
        if (billsSum > 0) {
            entries.add(new PieEntry(billsSum, "Bills"));
        }
        if (housingSum > 0) {
            entries.add(new PieEntry(housingSum, "Housing"));
        }
        if (healthSum > 0) {
            entries.add(new PieEntry(healthSum, "Health"));
        }
        if (socialLifeSum > 0) {
            entries.add(new PieEntry(socialLifeSum, "Social Life"));
        }
        if (apparelSum > 0) {
            entries.add(new PieEntry(apparelSum, "Apparel"));
        }
        if (beautySum > 0) {
            entries.add(new PieEntry(beautySum, "Beauty"));
        }
        if (educationSum > 0) {
            entries.add(new PieEntry(educationSum, "Education"));
        }
        if (otherSum > 0) {
            entries.add(new PieEntry(otherSum, "Other"));
        }


        PieDataSet set = new PieDataSet(entries, "");
        set.setColors(ColorTemplate.JOYFUL_COLORS);
        set.setHighlightEnabled(true); // allow highlighting for DataSet

        // set this to false to disable the drawing of highlight indicator (lines)
        pieData = new PieData(set);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(13f);
        pieData.setValueTextColor(Color.WHITE);
        pieData.setValueTypeface(Typeface.DEFAULT);
        mPieChart.setData(pieData);

        mPieChart.animateY(5000, Easing.EasingOption.Linear);
        // mPieChart.spin(2000, 0, 360);

        SpannableString s = new SpannableString("Monthly\ndetails by category");
        s.setSpan(new RelativeSizeSpan(1.4f), 0, 15, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 7, s.length() , 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 7, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.2f), 7, s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 21, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 12, s.length(), 0);
        mPieChart.setCenterText(s);
        Legend l = mPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mPieChart.setEntryLabelColor(Color.WHITE);
        mPieChart.setEntryLabelTypeface(Typeface.DEFAULT);
        mPieChart.setEntryLabelTextSize(12f);


        mPieChart.invalidate(); //
    }


    public void monthSpinnerClickListener(final Spinner monthSpinner) {
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (monthSpinner.getSelectedItem().toString().equals("January")) {
                    currentMonth = String.valueOf(1);
                    updatePieChart();
                }
                else if (monthSpinner.getSelectedItem().toString().equals("February")) {
                    currentMonth = String.valueOf(2);
                    updatePieChart();
                }
                else if (monthSpinner.getSelectedItem().toString().equals("March")) {
                    currentMonth = String.valueOf(3);
                    updatePieChart();
                }
                else if (monthSpinner.getSelectedItem().toString().equals("April")) {
                    currentMonth = String.valueOf(4);
                    updatePieChart();
                }
                else if (monthSpinner.getSelectedItem().toString().equals("May")) {
                    currentMonth = String.valueOf(5);
                    updatePieChart();
                }
                else if (monthSpinner.getSelectedItem().toString().equals("June")) {
                    currentMonth = String.valueOf(6);
                    updatePieChart();
                }
                else if (monthSpinner.getSelectedItem().toString().equals("July")) {
                    currentMonth = String.valueOf(7);
                    updatePieChart();
                }
                else if (monthSpinner.getSelectedItem().toString().equals("August")) {
                    currentMonth = String.valueOf(8);
                    updatePieChart();
                }
                else if (monthSpinner.getSelectedItem().toString().equals("September")) {
                    currentMonth = String.valueOf(9);
                    updatePieChart();
                }
                else if (monthSpinner.getSelectedItem().toString().equals("October")) {
                    currentMonth = String.valueOf(10);
                    updatePieChart();
                }
                else if (monthSpinner.getSelectedItem().toString().equals("November")) {
                    currentMonth = String.valueOf(11);
                    updatePieChart();
                }

                else if (monthSpinner.getSelectedItem().toString().equals("December")) {
                    currentMonth = String.valueOf(12);
                    updatePieChart();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

}
