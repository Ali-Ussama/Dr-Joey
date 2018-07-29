package com.example.aliosama.sillynamemaker.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.aliosama.sillynamemaker.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.rgb;

public class HistoryDayStatisticsActivity extends AppCompatActivity {
    float [] statusPercent = {40, 30, 30};
    String[] statusLabels = {"Happy", "Normal", "Sad"};
    String Day;
    PieChart pieChart;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_history_day_statistics);

            Init();
            loadPieChart();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void Init(){
        Intent intent = getIntent();

        Day = intent.getStringExtra("day");
        toolbar = findViewById(R.id.activity_history__statistics_toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(Day + " Overview");
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadPieChart() {
        try {
            List<PieEntry> pieEntries = new ArrayList<>();
            for(int i = 0 ; i < statusPercent.length; i++){
                pieEntries.add(new PieEntry(statusPercent[i],statusLabels[i]));
            }

            PieDataSet pieDataSet = new PieDataSet(pieEntries,"");
            final int[] COLORFUL_COLORS = {
                    rgb(118,255,3), rgb(255, 102, 0),rgb(193, 37, 82)
            };
            pieDataSet.setColors(COLORFUL_COLORS);
            pieDataSet.setSliceSpace(3f);
            pieDataSet.setSelectionShift(5f);

            PieData pieData = new PieData(pieDataSet);
            pieData.setValueTextSize(15f);

            pieChart = findViewById(R.id.activity_history__statistics_PieChart);
            pieChart.setData(pieData);
            pieChart.setUsePercentValues(true);
            pieChart.setDragDecelerationFrictionCoef(0.99f);
            pieChart.setTransparentCircleRadius(55f);
            pieChart.setDrawHoleEnabled(true);
            pieChart.getDescription().setEnabled(false);
            Description description = new Description();
            description.setText(Day);
            description.setTextSize(25f);
            description.setPosition(400,500);
            description.setTextColor(Color.BLACK);
            pieChart.setDescription(description);
            pieChart.animateY(1000, Easing.EasingOption.EaseInOutCirc);
            pieChart.invalidate();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
