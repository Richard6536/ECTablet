package com.example.richard.ectablet.Fragments.d;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.richard.ectablet.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class StatsFragment extends Fragment {
    LineChart mChart, mChartCorriente, mChartRin;
    private boolean moveToLastEntry = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        mChart = view.findViewById(R.id.chart);
        mChartCorriente = view.findViewById(R.id.chartCorriente);
        mChartRin = view.findViewById(R.id.chartRin);

        startChart();
        startChartCorriente();
        startChartRin();

        return view;
    }

    public void startChart(){

        /*
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });*/

        // enable description text
        mChart.getDescription().setEnabled(true);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        // set an alternative background color
        mChart.setBackgroundColor(Color.TRANSPARENT);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        mChart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaximum(80f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        /*
        mChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                moveToLastEntry = false;
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {
            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {
            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
            }
        });
        */
    }
    public void startChartCorriente(){

        /*
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });*/

        // enable description text
        mChartCorriente.getDescription().setEnabled(true);

        // enable touch gestures
        mChartCorriente.setTouchEnabled(true);

        // enable scaling and dragging
        mChartCorriente.setDragEnabled(true);
        mChartCorriente.setScaleEnabled(true);
        mChartCorriente.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChartCorriente.setPinchZoom(false);

        // set an alternative background color
        mChartCorriente.setBackgroundColor(Color.TRANSPARENT);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        mChartCorriente.setData(data);

        // get the legend (only possible after setting data)
        Legend l = mChartCorriente.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis xl = mChartCorriente.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = mChartCorriente.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaximum(30f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChartCorriente.getAxisRight();
        rightAxis.setEnabled(false);

        /*
        mChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                moveToLastEntry = false;
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {
            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {
            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
            }
        });
        */
    }
    public void startChartRin(){

        /*
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });*/

        // enable description text
        mChartRin.getDescription().setEnabled(true);

        // enable touch gestures
        mChartRin.setTouchEnabled(true);

        // enable scaling and dragging
        mChartRin.setDragEnabled(true);
        mChartRin.setScaleEnabled(true);
        mChartRin.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChartRin.setPinchZoom(false);

        // set an alternative background color
        mChartRin.setBackgroundColor(Color.TRANSPARENT);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        mChartRin.setData(data);

        // get the legend (only possible after setting data)
        Legend l = mChartRin.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis xl = mChartRin.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = mChartRin.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaximum(0.035f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChartRin.getAxisRight();
        rightAxis.setEnabled(false);

        /*
        mChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                moveToLastEntry = false;
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {
            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {
            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
            }
        });
        */
    }

    private void addEntry(float voltaje, float corriente, float estimacionSompa, float confIntervalSompa1, float confIntervalSompa2, String fecha) {
        entryVoltaje(voltaje);
        entryCorriente(corriente);

        entryEstimacionRin(estimacionSompa, confIntervalSompa1, confIntervalSompa2);
        //entryConfIntervalRin1(corriente);
    }

    private void entryVoltaje(float val){

        LineData data = mChart.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);

            if (set == null) {
                set = createSetVoltaje();
                data.addDataSet(set);
            }
            set.setDrawFilled(true);
            data.addEntry(new Entry(set.getEntryCount(), val), 0);
            //mChart.setData(data);
            // let the graph know it's data has changed
            mChart.notifyDataSetChanged();
            data.notifyDataChanged();


            //mChart.invalidate();
            // limit the number of visible entries
            //mChart.setVisibleXRangeMaximum(120);

            // move to the latest entry
            mChart.moveViewToX(data.getEntryCount());


        }
    }
    private void entryCorriente(float val){

        LineData data = mChartCorriente.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);

            if (set == null) {
                set = createSetCorriente();
                data.addDataSet(set);
            }
            set.setDrawFilled(true);
            data.addEntry(new Entry(set.getEntryCount(), val), 0);
            //mChart.setData(data);
            // let the graph know it's data has changed
            mChartCorriente.notifyDataSetChanged();
            data.notifyDataChanged();


            //mChart.invalidate();
            // limit the number of visible entries
            //mChart.setVisibleXRangeMaximum(120);

            // move to the latest entry
            mChartCorriente.moveViewToX(data.getEntryCount());

        }
    }

    private void entryEstimacionRin(float estimacionSompa, float confIntervalSompa1, float confIntervalSompa2){

        LineData data = mChartRin.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            ILineDataSet set2 = data.getDataSetByIndex(1);
            ILineDataSet set3 = data.getDataSetByIndex(2);

            if (set == null || set2 == null || set3 == null) {
                // creation of null
                set = createEstimacionSompa();
                set2 = createConfIntervalSompa1();
                set3 = createConfIntervalSompa2();

                data.addDataSet(set);
                data.addDataSet(set2);
                data.addDataSet(set3);
            }

            data.addEntry(new Entry(set.getEntryCount(), estimacionSompa), 0);
            data.addEntry(new Entry(set2.getEntryCount(), confIntervalSompa1), 1);
            data.addEntry(new Entry(set3.getEntryCount(), confIntervalSompa2), 2);

            //mChart.setData(data);
            // let the graph know it's data has changed
            mChartRin.notifyDataSetChanged();
            data.notifyDataChanged();


            //mChart.invalidate();
            // limit the number of visible entries
            //mChart.setVisibleXRangeMaximum(120);

            // move to the latest entry
            mChartRin.moveViewToX(data.getEntryCount());

        }
    }

    private LineDataSet createSetVoltaje() {

        LineDataSet set = new LineDataSet(null, "Voltaje");
        set.setDrawCircles(false);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(ColorTemplate.getHoloBlue());
        set.setLineWidth(10f);
        set.setCircleRadius(4f);
        set.setFillAlpha(255);
        set.setFillColor(Color.parseColor("#86D6F4"));
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }
    private LineDataSet createSetCorriente() {

        LineDataSet set = new LineDataSet(null, "Corriente");
        set.setDrawCircles(false);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.RED);
        set.setCircleColor(Color.RED);
        set.setLineWidth(10f);
        set.setCircleRadius(4f);
        set.setFillAlpha(255);
        set.setFillColor(Color.parseColor("#EC3838"));
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    private LineDataSet createEstimacionSompa() {

        LineDataSet set = new LineDataSet(null, "EstimaciónRin");
        set.setDrawCircles(false);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.GREEN);
        set.setCircleColor(Color.GREEN);
        set.setLineWidth(10f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }
    private LineDataSet createConfIntervalSompa1() {

        LineDataSet set = new LineDataSet(null, "ConfIntervalRin1");
        set.setDrawCircles(false);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(ColorTemplate.getHoloBlue());
        set.setLineWidth(10f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }
    private LineDataSet createConfIntervalSompa2() {

        LineDataSet set = new LineDataSet(null, "ConfIntervalRin2");
        set.setDrawCircles(false);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.RED);
        set.setCircleColor(Color.RED);
        set.setLineWidth(10f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }


    public void putArguments(Bundle args){
        String valueVoltaje = args.getString("VOLTAJE");
        String valueCorriente = args.getString("CORRIENTE");
        String valueFecha = args.getString("FECHA");
        String valueestimacionsompa = args.getString("ESTIMACIONSOMPA");
        String valueconfintervalsompa1 = args.getString("CONFINTERVALSOMPA1");
        String valueconfintervalsompa2 = args.getString("CONFINTERVALSOMPA2");

        float voltaje = Float.parseFloat(valueVoltaje);
        float corriente = Float.parseFloat(valueCorriente);

        float estimacionSompa = Float.parseFloat(valueestimacionsompa);
        float confIntervalSompa1 = Float.parseFloat(valueconfintervalsompa1);
        float confIntervalSompa2 = Float.parseFloat(valueconfintervalsompa2);

        addEntry(voltaje, corriente, estimacionSompa, confIntervalSompa1, confIntervalSompa2, valueFecha);
    }
}
