package com.example.marcelolongen.expensemanager;

import android.view.View;

import java.util.ArrayList;

public class Item {
    private int id;
    private String description;
    private Double value;
    private Integer month;
    private Integer day;
    private Integer year;
    private String category;

    private View.OnClickListener requestBtnClickListener;


    public Item(int id, String description, Double value, Integer month, Integer day, Integer year, String category) {
        this.id = id;
        this.description = description;
        this.value = value;
        this.day = day;
        this.month = month;
        this.year = year;
        this.category = category;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public View.OnClickListener getRequestBtnClickListener() {
        return requestBtnClickListener;
    }

    public void setRequestBtnClickListener(View.OnClickListener requestBtnClickListener) {
        this.requestBtnClickListener = requestBtnClickListener;
    }

}
