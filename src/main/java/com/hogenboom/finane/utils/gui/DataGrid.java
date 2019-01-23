package com.hogenboom.finane.utils.gui;


import com.hogenboom.finane.utils.parser.ing.model.FinancialRecord;
import com.hogenboom.finane.utils.parser.ing.FinancialRecordManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DataGrid {


    public static TableView<FinancialRecord> createTableView(FinancialRecordManager manager) {
        TableView<FinancialRecord> tableView = new TableView<>();

        TableColumn<FinancialRecord, String>  dateCol = new TableColumn<>("Datum");
        dateCol.setCellValueFactory(new PropertyValueFactory<FinancialRecord, String>("date"));

        TableColumn<FinancialRecord, String>  accountCol = new TableColumn<>("Rekening");
        accountCol.setCellValueFactory(new PropertyValueFactory<FinancialRecord, String>("account"));

        TableColumn<FinancialRecord, String>  counterAccountCol = new TableColumn<>("Tegen Rekening");
        counterAccountCol.setCellValueFactory(new PropertyValueFactory<FinancialRecord, String>("counterAccount"));

        TableColumn<FinancialRecord, String>  amountCol = new TableColumn<>("bedrag");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<FinancialRecord, String>  descriptionCol = new TableColumn<>("omschrijving");
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<FinancialRecord, String> mutationCol = new TableColumn<>("mutatie");
        mutationCol.setCellValueFactory(new PropertyValueFactory<FinancialRecord, String>("mutation"));

        TableColumn<FinancialRecord, String>  commentsCol = new TableColumn<>("opmerkingen");
        commentsCol.setCellValueFactory(new PropertyValueFactory<FinancialRecord, String>("comments"));

        tableView.getColumns().addAll(
                dateCol,
                accountCol,
                descriptionCol,
                counterAccountCol,
                amountCol,
                mutationCol,
                commentsCol
        );
        if (manager != null && !manager.getRecords().isEmpty()) {
            ObservableList<FinancialRecord> recordObservableList = FXCollections.observableArrayList(manager.getRecords());
            tableView.setItems(recordObservableList);
        }
        return tableView;
    }

}
