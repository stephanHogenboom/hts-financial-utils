package com.hogenboom.finane.utils.gui;

import com.hogenboom.finane.utils.FinancialParser;
import com.hogenboom.finane.utils.export.CSVExporter;
import com.hogenboom.finane.utils.parser.ing.FinancialRecordManager;
import com.hogenboom.finane.utils.parser.ing.INGParser;
import com.hogenboom.finane.utils.parser.ing.SavedDataParser;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//Mainscreen of the application
public class MainScreen extends Application {

  private TabPane rootLayout = new TabPane();
  private FinancialRecordManager manager = null;
  private Tab dataTab = new Tab("DataGrid");
  private Label managerLabel = new Label("Empty");
  private Tab barTab = new Tab("Bar Charts");
  private Tab monthDetailTab = new Tab("Month Details");
  private Path dataPath = Paths.get("data");
  private Path dataFile = dataPath.resolve("data.csv");

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    Button importButton = new Button("import");
    Button saveButton = new Button("save");

    FileChooser chooser = new FileChooser();
    importButton.setOnAction(e -> {
      File file = chooser.showOpenDialog(primaryStage);
      if (file != null) {
        openFile(file, primaryStage, new INGParser());
      }
    });

    saveButton.setOnAction(event -> {
      saveFile();
      managerLabel.setText("saved");
    });

    FlowPane importScreen = new FlowPane();
    Tab importTab = new Tab("Import CSV");

    rootLayout.getTabs().addAll(importTab, dataTab, barTab, monthDetailTab);
    importTab.setContent(importScreen);
    importScreen.getChildren().addAll(importButton, saveButton, managerLabel);
    Scene scene = new Scene(rootLayout, 800, 800);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Finance tools");
    primaryStage.show();
    if (Files.exists(dataFile)){
      openFile(dataFile.toFile(), primaryStage, new SavedDataParser());
    } else {
      managerLabel.setText("No datafile found");
    }
  }

  private void openFile(File file, Stage primaryStage, FinancialParser parser) {
    try {
      manager = FinancialRecordManager.fromStringAndParser(file.toString(), parser);
      managerLabel.setText(file.getName());
      dataTab.setContent(DataGrid.createTableView(manager));
      barTab.setContent(TotalPerMonthBarChart.createTotalPerMonth(manager));
      monthDetailTab.setContent(MonthlyExpenditureGridView.createMonthlyExpenditureGridView(manager, primaryStage));
    }
    catch (Exception ex) {
      System.out.println("exception occured:" + ex.getMessage());
      ex.printStackTrace();
    }
  }

  private void saveFile() {
    try {
      if (manager == null) {
        throw new IllegalStateException("Manager is not initialized");
      }
      if (!Files.exists(dataPath)) {
        Files.createDirectory(dataPath);
      }

      CSVExporter exporter = new CSVExporter();
      exporter.writeData(manager.getRecords(), dataFile.toFile());
    }
    catch (Exception e) {
      managerLabel.setText("Error: " + e.getMessage());
    }
  }
}
