package com.hogenboom.finane.utils.gui;

import com.hogenboom.finane.utils.parser.ing.FinancialRecordManager;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;

import java.time.Month;
import java.util.Set;

public class TotalPerMonthBarChart extends BorderPane {


    public static TotalPerMonthBarChart createTotalPerMonth(FinancialRecordManager manager) {
        TotalPerMonthBarChart root = new TotalPerMonthBarChart();
        ChoiceBox<String> accountSelector = new ChoiceBox<>();
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String, Number> bc = new BarChart<>(xAxis, yAxis);
        if (manager != null && !manager.getRecords().isEmpty()) {
            accountSelector
                    .getItems()
                    .addAll(manager.getAccounts());
            root.setTop(accountSelector);
        }
        if (manager != null) {
            for (int year : manager.getRecordsByYearAndMonth().keySet()) {
                Set<Month> months = manager.getRecordsByYearAndMonth().get(year).keySet();
                for (Month m : months) {
                    XYChart.Series<String, Number> monthlyChart = new XYChart.Series<>();
                    double expendituresForMonth = manager.getExpenditureTotalForMonth(year, m);
                    double gainsForMonth = manager.getTotalGainsForMonth(year, m);
                    monthlyChart.getData().add(new XYChart.Data<>(String.format("%s-%s: gained", year, m), gainsForMonth));
                    monthlyChart.getData().add(new XYChart.Data<>(String.format("%s-%s: expenses", year, m), expendituresForMonth));
                    bc.getData()
                            .addAll(monthlyChart);
                }
            }

        }
        root.setCenter(bc);
        return root;
    }

}
