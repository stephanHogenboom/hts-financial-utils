package com.hogenboom.finane.utils.parser.ing;

import com.hogenboom.finane.utils.parser.ing.model.FinancialRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.File;
import java.io.IOException;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.nio.charset.Charset.defaultCharset;


public class INGParser {
    public static String date = "Datum";
    public static String description ="Naam / Omschrijving";
    public static String account = "Rekening";
    public static String otherAccount = "Tegenrekening";
    public static String code = "Code";
    public static String sign = "Af Bij";
    public static String amount = "Bedrag  = EUR";
    public static String mutationKind = "MutatieSoort";
    public static String comments = "Mededelingen";

    public INGParser() {

    }

    public List<FinancialRecord> parse(String path, Boolean skipHeader) throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withHeader(date, description, account, otherAccount, code,
                sign, amount, mutationKind, comments);
        List<FinancialRecord> records = new ArrayList<>();
        try (CSVParser parser = CSVParser.parse(new File(path), defaultCharset(), format)) {
            records = parser.getRecords()
                    .stream()
                    .skip(skipHeader ? 1 : 0)
                    .map(FinancialRecord::toFinancialRecord)
                    .collect(Collectors.toList());
        }
        return records;
    }

    public static void main(String[] args) throws IOException {
        FinancialRecordManager manager = FinancialRecordManager
                .fromString("src/main/resources/Alle_rekeningen_01-01-2018_18-12-2018.csv", true);
        Map<Month, List<FinancialRecord>> recordByMonth = manager.getRecordsByMonth();
        recordByMonth.get(Month.JANUARY).forEach(System.out::println);
    }

}