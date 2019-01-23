package com.hogenboom.finane.utils.export;

import com.hogenboom.finane.utils.parser.ing.model.MonthDetails;
import javafx.collections.ObservableList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;

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
}
