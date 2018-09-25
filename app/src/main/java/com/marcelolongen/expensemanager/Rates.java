package com.marcelolongen.expensemanager;

import java.util.HashMap;

public class Rates {
    private String base;
    private String date;
    private HashMap<String, Double> rates;


    public Rates() {
    }

    public Rates(String base, String date, HashMap<String, Double> rates) {
        this.base = base;
        this.date = date;
        this.rates = rates;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public HashMap<String, Double> getRates() {
        return rates;
    }

    public void setRates(HashMap<String, Double> rates) {
        this.rates = rates;
    }

    @Override
    public String toString() {
        return "Rates{" +
                "base='" + base + '\'' +
                ", date='" + date + '\'' +
                ", rates=" + rates +
                '}';
    }
}
