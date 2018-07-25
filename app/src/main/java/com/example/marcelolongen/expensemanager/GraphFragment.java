package com.example.marcelolongen.expensemanager;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment {
    private double[] sum = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    private AnyChartView anyChartView;
    private Database db;
    private static final String ARG_USER_NAME= "username";
    private String userName;
    private PieChart mChart;
    private float housingSum;
    private float totalSum;
    private PieData data;
    private View thisView;
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
        mChart = thisView.findViewById(R.id.chart);

        updateData(thisView);



        Button changeData = thisView.findViewById(R.id.change_data);
        changeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    sum[2] +=5000;
                      updateData(v);
            }
        });



        return thisView;

    }

    public void updateData(View v) {
        String[] categories = {"Food", "Bills", "Housing", "Health", "Social Life", "Apparel", "Beauty", "Education", "Other"};

        @SuppressLint("SimpleDateFormat") String thisMonth = new SimpleDateFormat("M").format(Calendar.getInstance().getTime());

        for (int i = 0; i < sum.length; i++) {
            sum[i] = 0.0;
        }

        for (int i = 0; i< db.getItemObjects().size(); i++ ) {

            for (int j = 0; j < categories.length;j++) {
                if (db.getItemObjects().get(i).getCategory().equals(categories[j])&& db.getItemObjects().get(i).getMonth().toString().equals(thisMonth) ) {
                    sum[j] += db.getItemObjects().get(i).getValue();
                }
            }
        }

        totalSum = (float) sum[0] + (float) sum[1]
                + (float) sum[2] + (float) sum[3]
                + (float) sum[4] + (float) sum[5]
                + (float) sum[6] + (float) sum[7] + (float) sum[8];


        float foodSum = (float) ((sum[0] / totalSum) * 100);
        float billsSum = (float) ((sum[1] / totalSum) * 100);
        housingSum = (float) ((sum[2] / totalSum) * 100);
        float healthSum = (float) ((sum[3] / totalSum) * 100);
        float socialLifeSum = (float) ((sum[4] / totalSum) * 100);
        float apparelSum = (float) ((sum[5] / totalSum) * 100);
        float beautySum = (float) ((sum[6] / totalSum) * 100);
        float educationSum = (float) ((sum[7] / totalSum) * 100);
        float otherSum = (float) ((sum[8] / totalSum) * 100);



        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setCenterTextTypeface(Typeface.DEFAULT);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);


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
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setHighlightEnabled(true); // allow highlighting for DataSet

        // set this to false to disable the drawing of highlight indicator (lines)
        data = new PieData(set);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(Typeface.DEFAULT);
        mChart.setData(data);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutElastic);
        // mChart.spin(2000, 0, 360);


        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTypeface(Typeface.DEFAULT);
        mChart.setEntryLabelTextSize(12f);


        mChart.invalidate(); //
    }


}
