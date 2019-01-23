package com.hogenboom.finane.utils.parser.ing.model;

import java.time.Month;

public class MonthDetails {
    private Integer year;
    private Month month;
    private Double gains;
    private Double expenditure;
    private Double netGains;

    public MonthDetails(Integer year, Month month, Double gains, Double expenditure, Double netGains) {
        this.year = year;
        this.month = month;
        this.gains = gains;
        this.expenditure = expenditure;
        this.netGains = netGains;
    }

    public Integer getYear() {
        return year;
    }

    public Month getMonth() {
        return month;
    }

    public Double getGains() {
        return gains;
    }

    public Double getExpenditure() {
        return expenditure;
    }

    public Double getNetGains() {
        return netGains;
    }
}
