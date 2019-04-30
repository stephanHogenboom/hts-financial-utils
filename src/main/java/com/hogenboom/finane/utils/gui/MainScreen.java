package com.hogenboom.finane.utils.gui;

import com.hogenboom.finane.utils.parser.ing.FinancialRecordManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

//Mainscreen of the application
public class MainScreen extends Application {

    private TabPane rootLayout = new TabPane();
    private FinancialRecordManager manager = null;
    private Tab dataTab = new Tab("DataGrid");
    private Label managerLabel = new Label("Empty");
    private Tab barTab = new Tab("Bar Charts");
    private Tab monthDetailTab = new Tab("Month Details");
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Button importButton = new Button("import");

        FileChooser chooser = new FileChooser();
        importButton.setOnAction(e -> {
            File file = chooser.showOpenDialog(primaryStage);
            if (file != null) {
                openFile(file, primaryStage);
            }
        });

        FlowPane importScreen = new FlowPane();
        Tab importTab = new Tab("Import CSV");

        rootLayout.getTabs().addAll(importTab, dataTab, barTab, monthDetailTab);
        importTab.setContent(importScreen);
        importScreen.getChildren().addAll(importButton, managerLabel);
        Scene scene = new Scene(rootLayout, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Finance tools");
        primaryStage.show();
    }

    private void openFile(File file, Stage primaryStage) {
        try {
            manager = FinancialRecordManager.fromString(file.toString(), true);
            managerLabel.setText(file.getName());
            dataTab.setContent(DataGrid.createTableView(manager));
            barTab.setContent(TotalPerMonthBarChart.createTotalPerMonth(manager));
            monthDetailTab.setContent(MonthlyExpenditureGridView.createMonthlyExpenditureGridView(manager, primaryStage));
        } catch (Exception ex) {
            System.out.println("exception occured:" + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
