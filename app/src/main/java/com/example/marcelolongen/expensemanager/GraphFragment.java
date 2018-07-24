package com.example.marcelolongen.expensemanager;


import android.annotation.SuppressLint;
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
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment {
    private Double[] sum = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    private AnyChartView anyChartView;
    private Database db;
    private static final String ARG_USER_NAME= "username";
    private String userName;

    public GraphFragment() {
        // Required empty public constructor
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
        View v = inflater.inflate(R.layout.fragment_graph, container, false);



        anyChartView = v.findViewById(R.id.any_chart_view);

        Pie pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {

            }
        });


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



//        toast(sum[0] + " " + sum[1] + " " + sum[2] + " " + sum[3] + " " + sum[4] + " " + sum[5] + " " + sum[6] + " " + sum[7] + " " + sum[8]);
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Food", sum[0]));
        data.add(new ValueDataEntry("Bills", sum[1]));
        data.add(new ValueDataEntry("Housing", sum[2]));
        data.add(new ValueDataEntry("Health", sum[3]));
        data.add(new ValueDataEntry("Social Life", sum[4]));
        data.add(new ValueDataEntry("Apparel", sum[5]));
        data.add(new ValueDataEntry("Beauty", sum[6]));
        data.add(new ValueDataEntry("Education", sum[7]));
        data.add(new ValueDataEntry("Other", sum[8]));

        pie.setData(data);

        pie.setTitle("Detailed expenses for this month:");

        pie.getLabels().setPosition("outside");

        pie.getLegend().getTitle().setEnabled(true);
        pie.getLegend().getTitle()
                .setText("Categories")
                .setPadding(0d, 0d, 10d, 0d);

        pie.getLegend()
                .setPosition("center-bottom")
                .setItemsLayout(LegendLayout.HORIZONTAL)
                .setAlign(EnumsAlign.CENTER);
        anyChartView.setChart(pie);

        Button changeData = v.findViewById(R.id.change_data);
        changeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    sum[3] +=5000;
                    Toast.makeText(getContext(), sum[3].toString(), Toast.LENGTH_SHORT).show();


            }
        });



        return v;

    }



}
