package com.example.fsoft_shopee_nhom02.dto;

public class DayToDayDTO {
    private String day;
    private String month;
    private String year;
    private String dayE;
    private String monthE;
    private String yearE;

    public DayToDayDTO() {
    }

    public DayToDayDTO(String day, String month, String year, String dayE, String monthE, String yearE) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.dayE = dayE;
        this.monthE = monthE;
        this.yearE = yearE;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDayE() {
        return dayE;
    }

    public void setDayE(String dayE) {
        this.dayE = dayE;
    }

    public String getMonthE() {
        return monthE;
    }

    public void setMonthE(String monthE) {
        this.monthE = monthE;
    }

    public String getYearE() {
        return yearE;
    }

    public void setYearE(String yearE) {
        this.yearE = yearE;
    }
}
