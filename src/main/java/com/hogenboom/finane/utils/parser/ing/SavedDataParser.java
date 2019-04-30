package com.hogenboom.finane.utils.parser.ing;

import com.hogenboom.finane.utils.FinancialParser;
import com.hogenboom.finane.utils.parser.ing.model.FinancialRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.Charset.defaultCharset;

public class SavedDataParser implements FinancialParser {

  public List<FinancialRecord> parse(String path, Boolean skipHeader) throws IOException {
    CSVFormat format = CSVFormat.DEFAULT.withHeader(
        "Datum",
        "Rekening",
        "omschrijving",
        "Tegen Rekening",
        "bedrag",
        "mutatie",
        "opmerkingen",
        "Categorie"
    );
    List<FinancialRecord> records = new ArrayList<>();
    try (CSVParser parser = CSVParser.parse(new File(path), defaultCharset(), format)) {
      records = parser.getRecords()
          .stream()
          .skip(skipHeader ? 1 : 0)
          .map(csvRecord -> new FinancialRecord(
                LocalDate.parse(csvRecord.get("Datum"), DateTimeFormatter.ofPattern("yyyyMMdd")),
                csvRecord.get( "Rekening"),
                csvRecord.get("Tegen Rekening"),
                Double.parseDouble(csvRecord.get("bedrag")),
                csvRecord.get("omschrijving"),
                csvRecord.get("mutatie"),
                csvRecord.get("opmerkingen"),
               null
               // csvRecord.get("Categorie")
            )).collect(Collectors.toList());
    }
    return records;
  }


}
