package com.example.campusexpensemanager;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.DataBase.ExpensesData;
import com.example.campusexpensemanager.Model.Expenses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class PieChart extends AppCompatActivity {

    private List<String> xData = new ArrayList<>();

    ArrayList pieEntries;

    private ExpensesData expensesData;

    HashMap<String, String> map;

    PieChartView pieChartView;
    List<SliceValue> pieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        pieChartView = findViewById(R.id.chart);

        expensesData = new ExpensesData(PieChart.this);

        addData();
        getEntries();


        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
        pieChartData.setHasCenterCircle(true).setCenterText1("Expenses").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
        pieChartView.setPieChartData(pieChartData);

    }

    private void addData() {
        List<Expenses> expenseModelList = expensesData.getAllIncome();

        for (Expenses model : expenseModelList) {
            xData.add(model.getName());
        }

        map = new HashMap<>();
        for (Expenses model : expenseModelList) {
            int amount = Integer.parseInt(model.getAmount());
            if (map.containsKey(model.getName())) {
                int a = Integer.parseInt(map.get(model.getName()));
                map.put(model.getName(), String.valueOf(a + amount));
            } else {
                map.put(model.getName(), model.getAmount());
            }
        }
    }

    private void getEntries() {
        pieEntries = new ArrayList<>();
        int i = 0;

        pieData = new ArrayList<>();

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.MAGENTA);
        colors.add(Color.BLUE);
        colors.add(Color.YELLOW);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.GRAY);

        for (String type : xData) {
            pieData.add(new SliceValue(Float.parseFloat(map.get(type)), colors.get(i % 6)).setLabel(type));
            i++;
        }
    }


}