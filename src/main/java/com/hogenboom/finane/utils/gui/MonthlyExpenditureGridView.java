package com.hogenboom.finane.utils.gui;

import com.hogenboom.finane.utils.export.CSVExporter;
import com.hogenboom.finane.utils.parser.ing.FinancialRecordManager;
import com.hogenboom.finane.utils.parser.ing.model.MonthDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class  MonthlyExpenditureGridView<T> extends BorderPane {


    public static MonthlyExpenditureGridView createMonthlyExpenditureGridView(FinancialRecordManager manager, Stage stage) {
        CSVExporter exporter = new CSVExporter();
        MonthlyExpenditureGridView overView = new MonthlyExpenditureGridView();
        TableView<MonthDetails> view = new TableView<>();
        ChoiceBox<String> accountBox = new ChoiceBox<>();

        accountBox.getItems().addAll(manager.getAccounts());
        accountBox.setOnAction(e -> setItems(manager, accountBox.getSelectionModel().getSelectedItem(), view));

        Button exportButton = new Button("export");
        exportButton.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            File selectedFile = chooser.showOpenDialog(stage);
            exporter.exportMonthlyDetails(view.getItems(), selectedFile);

        });

        HBox topBox = new HBox();
        topBox.getChildren().addAll(accountBox, exportButton);
        TableColumn<MonthDetails, String> yearCol = new TableColumn<>("year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<MonthDetails, String> monthCol = new TableColumn<>("month");
        monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));

        TableColumn<MonthDetails, String> gainsCol = new TableColumn<>("gains");
        gainsCol.setCellValueFactory(new PropertyValueFactory<>("gains"));

        TableColumn<MonthDetails, String> expenditureCol = new TableColumn<>("expenditure");
        expenditureCol.setCellValueFactory(new PropertyValueFactory<>("expenditure"));

        TableColumn<MonthDetails, String> netGainsCol = new TableColumn<>("net gains");
        netGainsCol.setCellValueFactory(new PropertyValueFactory<>("netGains"));

        view.getColumns().addAll(
                yearCol,
                monthCol,
                gainsCol,
                expenditureCol,
                netGainsCol
        );
        setItems(manager, view);
        overView.setCenter(view);
        overView.setTop(topBox);
        return overView;
    }

    private static void setItems(FinancialRecordManager manager, TableView<MonthDetails> view) {
        if (manager != null && !manager.getRecords().isEmpty()) {
            view.getItems().clear();
            List<MonthDetails> details = manager.getMonthDetails();
            ObservableList<MonthDetails> recordObservableList = FXCollections.observableArrayList(details);
            view.setItems(recordObservableList);
        }
    }

    private static void setItems(FinancialRecordManager manager, String account, TableView<MonthDetails> view) {
        if (manager != null && !manager.getRecords().isEmpty()) {
            view.getItems().clear();
            List<MonthDetails> details = manager.getMonthDetailsForAccount(account);
            ObservableList<MonthDetails> recordObservableList = FXCollections.observableArrayList(details);
            view.setItems(recordObservableList);
        }
    }





}
