package com.hogenboom.finane.utils.export;

import com.hogenboom.finane.utils.parser.ing.model.FinancialRecord;
import com.hogenboom.finane.utils.parser.ing.model.MonthDetails;
import javafx.collections.ObservableList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.util.List;

public class CSVExporter {
    CSVPrinter printer;

    public void exportMonthlyDetails(ObservableList<MonthDetails> details, File file) {

        CSVFormat format = CSVFormat.DEFAULT.withHeader("year", "month", "gains", "expenditure", "netGains");

        try (FileOutputStream fos = new FileOutputStream(file);
             PrintWriter writer = new PrintWriter(fos);
             CSVPrinter csvPrinter = printer = new CSVPrinter(writer, format)) {
            for (MonthDetails detail : details)
                csvPrinter.printRecord(detail.getYear(),
                        detail.getMonth(),
                        detail.getGains(),
                        detail.getExpenditure(),
                        detail.getNetGains()
                );
        } catch (IOException e) {
            System.out.println("Error while printing details " + e.getMessage());
        }
    }

    public void writeData(List<FinancialRecord> records, File file) {
        CSVFormat format = CSVFormat.DEFAULT.withHeader("Datum", "Rekening", "omschrijving", "Tegen Rekening", "bedrag", "mutatie", "opmerkingen", "Categorie");
        try (FileOutputStream fos = new FileOutputStream(file);
             PrintWriter writer = new PrintWriter(fos);
             CSVPrinter csvPrinter = printer = new CSVPrinter(writer, format)) {

            for (FinancialRecord record : records) {
                csvPrinter.printRecord(
                        record.getDate(),
                        record.getAccount(),
                        record.getDescription(),
                        record.getCounterAccount(),
                        record.getAmount(),
                        record.getMutation(),
                        record.getComments()
                        //record.getCategory()
                );
            }
        } catch (IOException e) {
            System.out.println("Error while printing details " + e.getMessage());
        }
    }
}
