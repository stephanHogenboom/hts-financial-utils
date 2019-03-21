package com.hogenboom.finane.utils.parser.ing;

import com.hogenboom.finane.utils.parser.ing.model.FinancialRecord;
import com.hogenboom.finane.utils.parser.ing.model.MonthDetails;

import java.io.IOException;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FinancialRecordManager {

    private List<FinancialRecord> records;
    private Map<Integer, Map<Month, List<FinancialRecord>>> recordsByYearAndMonth;

    public List<String> getAccounts() {
        return records
                .stream()
                .map(FinancialRecord::getAccount)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<MonthDetails> getMonthDetailsForAccount(String account) {
        List<FinancialRecord> recordsForAccount = getRecordsForAccount(account);
        Map<Integer, Map<Month, List<FinancialRecord>>> recordsByYearAndMonthForAccount = getRecordsByYearAndMonth(recordsForAccount);
        List<MonthDetails> details = new ArrayList<>();
        addDetailsToList(recordsByYearAndMonthForAccount, details);
        return details;
    }

    private void addDetailsToList(Map<Integer, Map<Month, List<FinancialRecord>>> recordsByYearAndMonthForAccount, List<MonthDetails> details) {
        for (Integer year : recordsByYearAndMonthForAccount.keySet()) {
            for (Month m : recordsByYearAndMonthForAccount.get(year).keySet()) {
                List<FinancialRecord> records = recordsByYearAndMonthForAccount
                        .get(year)
                        .get(m);
                details.add(toMonthDetails(records));
            }
        }
    }

    public List<MonthDetails> getMonthDetails() {
        List<MonthDetails> details = new ArrayList<>();
        addDetailsToList(recordsByYearAndMonth, details);
        return details;
    }


    public double getNetGainsForMonth(List<FinancialRecord> records) {
        return records
                .stream()
                .mapToDouble(FinancialRecord::getAmount)
                .sum();
    }


    public double getNetGainsForMonth(int year, Month m) {
        return getNetGainsForMonth(getRecordsForYearAndMonth(year, m));
    }

    public double getTotalGainsForMonth(List<FinancialRecord> records) {
        return records
                .stream()
                .filter(record -> record.getAmount() > 0)
                .mapToDouble(FinancialRecord::getAmount)
                .sum();
    }

    public double getTotalGainsForMonth(int year, Month m) {
        return getTotalGainsForMonth(getRecordsForYearAndMonth(year, m));
    }

    public double getExpenditureTotalForMonth(List<FinancialRecord> records) {
        return records
                .stream()
                .filter(record -> record.getAmount() < 0)
                .mapToDouble(rec -> rec.getAmount() * -1)
                .sum();
    }

    public double getExpenditureTotalForMonth(int year, Month m) {
        return getExpenditureTotalForMonth(getRecordsForYearAndMonth(year, m));
    }

    public List<FinancialRecord> getRecordsForAccount(String account) {
        return records
                .stream()
                .filter(record -> record.getAccount()
                        .equals(account))
                .collect(Collectors.toList());
    }

    public static FinancialRecordManager fromString(String fileLocation, boolean skipHeaders) throws IOException {
        INGParser parser = new INGParser();
        return new FinancialRecordManager(parser.parse(fileLocation, skipHeaders));
    }

    public static FinancialRecordManager fromString(String fileLocation) throws IOException {
        return fromString(fileLocation, false);
    }

    private FinancialRecordManager(List<FinancialRecord> records) {
        this.records = records;
        this.recordsByYearAndMonth = getRecordsByYearAndMonth();
    }

    public List<FinancialRecord> getRecords() {
        return records;
    }

    public List<FinancialRecord> getRecordsForYear(int year) {
        return records.stream()
                .filter(record -> record.getDate().getYear() == year)
                .collect(Collectors.toList());
    }

    public List<FinancialRecord> getRecordsForYearAndMonth(int year, Month month) {
        return records.stream()
                .filter(record -> record.getDate().getYear() == year)
                .filter(record -> record.getDate().getMonth() == month)
                .collect(Collectors.toList());
    }

    public Map<Integer, List<FinancialRecord>> getRecordsByYear(List<FinancialRecord> records) {
        return records.stream()
                .collect(Collectors.groupingBy(FinancialRecord::getYear, Collectors.mapping(record -> record, Collectors.toList())));
    }

    public Map<Integer, List<FinancialRecord>> getRecordsByYear() {
        return getRecordsByYear(records);
    }

    public Map<Integer, Map<Month, List<FinancialRecord>>> getRecordsByYearAndMonth(List<FinancialRecord> records) {
        Map<Integer, Map<Month, List<FinancialRecord>>> recordsByYearAndMonth = new HashMap<>();
        for (Map.Entry<Integer, List<FinancialRecord>> yearAndRecords : getRecordsByYear(records).entrySet()) {
            Integer year = yearAndRecords.getKey();
            List<FinancialRecord> recordsForYear = yearAndRecords.getValue();
            Map<Month, List<FinancialRecord>> recordsByMonth = getRecordsByMonth(recordsForYear);
            recordsByYearAndMonth.put(year, recordsByMonth);
        }
        this.recordsByYearAndMonth = recordsByYearAndMonth;
        return this.recordsByYearAndMonth;
    }


    public Map<Integer, Map<Month, List<FinancialRecord>>> getRecordsByYearAndMonth() {
        if (this.recordsByYearAndMonth == null) {
            Map<Integer, Map<Month, List<FinancialRecord>>> recordsByYearAndMonth = new HashMap<>();
            for (Map.Entry<Integer, List<FinancialRecord>> yearAndRecords : getRecordsByYear().entrySet()) {
                Integer year = yearAndRecords.getKey();
                List<FinancialRecord> records = yearAndRecords.getValue();
                Map<Month, List<FinancialRecord>> recordsByMonth = getRecordsByMonth(records);
                recordsByYearAndMonth.put(year, recordsByMonth);
            }
            this.recordsByYearAndMonth = recordsByYearAndMonth;
        }
        return this.recordsByYearAndMonth;
    }

    public Map<Month, List<FinancialRecord>> getRecordsByMonth() {
        return records.stream()
                .collect(Collectors.groupingBy(FinancialRecord::getMonth, Collectors.mapping(record -> record, Collectors.toList())));
    }

    public Map<Month, List<FinancialRecord>> getRecordsByMonth(List<FinancialRecord> records) {
        return records.stream()
                .collect(Collectors.groupingBy(FinancialRecord::getMonth, Collectors.mapping(record -> record, Collectors.toList())));
    }

    public MonthDetails toMonthDetails(List<FinancialRecord> records) {
        Month month = records.stream()
                .map(FinancialRecord::getMonth)
                .findFirst().orElseThrow(() -> new IllegalStateException("no month in list"));
        int year = records.stream()
                .mapToInt(FinancialRecord::getYear).findFirst()
                .orElseThrow(() -> new IllegalStateException("no year in list"));
        double expenses = getExpenditureTotalForMonth(records);
        double gains = getTotalGainsForMonth(records);
        double netGains = getNetGainsForMonth(records);
        return new MonthDetails(year, month, gains, expenses, netGains);
    }
}
