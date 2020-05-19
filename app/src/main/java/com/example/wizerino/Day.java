package com.example.wizerino;

public class Day {
    public String valid_date;
    public String min_temp;
    public String max_temp;
    public String icon;
    public String description;
    public String humidity;

    public Day(String valid_date, String min_temp, String max_temp, String icon, String description, String humidity) {
        this.valid_date = valid_date;
        this.min_temp = min_temp;
        this.max_temp = max_temp;
        this.icon = icon;
        this.description = description;
        this.humidity = humidity;
    }
}