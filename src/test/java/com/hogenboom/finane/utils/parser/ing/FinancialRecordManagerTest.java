package com.hogenboom.finane.utils.parser.ing;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FinancialRecordManagerTest {

    private Path testDir = Paths.get(String.format("target/test/%s", getClass().getSimpleName()));
    private String testAccount = "NL00testaccount";
    private String testCounterAccount = "NL00testCounterAccount";
    private String testName = "the taker";
    private String code = "a";
    private String subtract = "Af";
    private String add = "Bij";
    private String kind = "incasso";


    @Before
    public void setup() throws IOException {
        Files.createDirectories(testDir);
    }

    @Test
    public void doTest() throws IOException {
        Path resourceFolder = Files.createDirectories(testDir.resolve("testFiles"));
        try (PrintWriter pw = new PrintWriter(resourceFolder.resolve("file1.csv").toString())) {
            List<String> lines = createCSVLines();
            lines.forEach(pw::println);
        }
        FinancialRecordManager manager = FinancialRecordManager.fromStringAndParser(resourceFolder.resolve("file1.csv").toString(), new INGParser());
        System.out.println(manager.getRecords());
    }

    private List<String> createCSVLines() {
        return Arrays.asList((new INGParser().getHeaders().stream().collect(Collectors.joining(","))),
                createCsvLine()
        );
    }

    private String createCsvLine() {
        return Stream.
                of(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), testName, testAccount, testAccount, code, add, "30,20", testName, "testing")
                .collect(Collectors.joining(","));
    }


}