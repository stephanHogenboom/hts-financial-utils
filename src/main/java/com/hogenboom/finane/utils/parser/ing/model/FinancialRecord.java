package com.hogenboom.finane.utils.parser.ing.model;

import com.hogenboom.finane.utils.parser.ing.INGParser;
import org.apache.commons.csv.CSVRecord;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class FinancialRecord {

    private LocalDate date;
    private String account;
    private String counterAccount;
    private double amount;
    private String description;
    private String mutation;
    private String comments;

    public FinancialRecord() {
    }

    public FinancialRecord(LocalDate date, String account, String counterAccount, double amount,
                           String description, String mutation, String comments) {
        this.date = date;
        this.account = account;
        this.counterAccount = counterAccount;
        this.amount = amount;
        this.description = description;
        this.mutation = mutation;
        this.comments = comments;
    }

    public static FinancialRecord toFinancialRecord(CSVRecord record) {
        LocalDate date = LocalDate.parse(record.get(INGParser.date), DateTimeFormatter.ofPattern("yyyyMMdd"));
        double amount = Double.parseDouble(record.get(INGParser.amount)
                .replace(",", ".")) * (record.get(INGParser.sign).equals("Bij") ? 1 : -1);

        return new FinancialRecord(
                date,
                record.get(INGParser.account),
                record.get(INGParser.otherAccount),
                amount,
                record.get(INGParser.description),
                record.get(INGParser.mutationKind),
                record.get(INGParser.comments)
        );
    }


    public Month getMonth() {
        return date.getMonth();
    }

    public Integer getYear() {
        return date.getYear();
    }

    public LocalDate getDate() {
        return date;
    }

    public String getAccount() {
        return account;
    }

    public String getCounterAccount() {
        return counterAccount;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getMutation() {
        return mutation;
    }

    public String getComments() {
        return comments;
    }

    @Override
    public String toString() {
        return "FinancialRecord{" +
                ", date=" + date +
                ", account='" + account + '\'' +
                ", counterAccount='" + counterAccount + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", mutation='" + mutation + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }
}

