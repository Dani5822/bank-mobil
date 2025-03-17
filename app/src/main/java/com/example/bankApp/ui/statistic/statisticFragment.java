package com.example.bankApp.ui.statistic;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bankApp.databinding.FragmentStatisticBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class statisticFragment extends Fragment {

    private FragmentStatisticBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStatisticBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init();
        return root;
    }

    public void init() {
        setPieChart();


    }

    private PieData generatePieData() {

        int count = 4;

        ArrayList<PieEntry> entries1 = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            PieEntry entry = new PieEntry((float) ((Math.random() * 60) + 40));
            entries1.add(entry);
        }

        PieDataSet ds1 = new PieDataSet(entries1, "Quarterly Revenues 2015");
        ds1.setColors(ColorTemplate.JOYFUL_COLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.BLACK);
        ds1.setValueTextSize(12f);

        return  new PieData(ds1);
    }

    private SpannableString generateCenterText() {
        SpannableString s = new SpannableString("Kategóriák");
        s.setSpan(new RelativeSizeSpan(2f), 0, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.BLACK), 8, s.length(), 0);
        return s;
    }


    public void setPieChart() {
        PieChart chart = binding.chart1;
        chart.getDescription().setEnabled(false);
        chart.setCenterText(generateCenterText());
        chart.setCenterTextSize(9f);
        chart.setDrawEntryLabels(false);
        chart.setDrawRoundedSlices(true);
        chart.setUsePercentValues(true);
        chart.setHoleRadius(75f);


        Legend l = chart.getLegend();
        l.setEnabled(false);
        chart.setRotation(0);
        chart.setRotationEnabled(false);
        /*
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);*/

        chart.setData(generatePieData());
        chart.getData().setDrawValues(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}