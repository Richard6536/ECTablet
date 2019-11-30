package com.example.richard.ectablet.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.richard.ectablet.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class StatsFragment extends Fragment {
    LineChart chart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        chart = view.findViewById(R.id.chart);

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 0));
        entries.add(new Entry(10, 30));
        entries.add(new Entry(20, 46));
        entries.add(new Entry(30, 55));
        entries.add(new Entry(40, 40));
        entries.add(new Entry(50, 23));
        entries.add(new Entry(60, 0));
        entries.add(new Entry(70, 30));
        entries.add(new Entry(80, 46));
        entries.add(new Entry(90, 65));
        entries.add(new Entry(100, 80));

        LineDataSet dataSet = new LineDataSet(entries, "Customized values");
        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.quantum_white_text));
        dataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.quantum_white_text));
        dataSet.setValueTextSize(20);
        //****
        // Controlling X axis
        ArrayList xVals = new ArrayList();
        xVals.add("January");
        xVals.add("February");
        xVals.add("March");
        xVals.add("April");

        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisMinimum(4);
        xAxis.setTextSize(20);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(xVals.size());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
        //***
        // Controlling right side of y axis
        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setEnabled(false);

        //***
        // Controlling left side of y axis
        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setGranularity(1f);
        yAxisLeft.setTextSize(20);

        // Setting Data
        LineData data = new LineData(dataSet);
        chart.setData(data);
        chart.animateX(2500);
        //refresh
        chart.invalidate();

        return view;
    }
}
