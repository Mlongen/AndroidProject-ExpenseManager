package com.example.marcelolongen.expensemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.EnumsAlign;
import com.anychart.anychart.LegendLayout;
import com.anychart.anychart.Pie;
import com.anychart.anychart.ValueDataEntry;
import com.anychart.anychart.chart.common.Event;
import com.anychart.anychart.chart.common.ListenersInterface;

import java.util.ArrayList;
import java.util.List;

public class GraphView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphview);

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);

        Pie pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {

            }
        });

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Clothes", 664));
        data.add(new ValueDataEntry("Utilities", 522));
        data.add(new ValueDataEntry("Rent", 721));
        data.add(new ValueDataEntry("Beer", 148));
        data.add(new ValueDataEntry("Food", 420));

        pie.setData(data);

        pie.setTitle("Detailed expenses in the month of May:");

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
    }
}
