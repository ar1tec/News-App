package com.example.mansi.newsapp;

import java.util.ArrayList;

public class News extends ArrayList {
    private String title = null;
    private String section = null;
    private String firstName = null;
    private String lastName = null;
    private String date = null;
    private String webUrl = null;

    News(String title, String section, String firstName, String lastName, String webUrl, String date) {
        this.title = title;
        this.section = section;
        this.firstName = firstName;
        this.lastName = lastName;
        this.webUrl = webUrl;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getAuthor() {
        return firstName + " " + lastName;
    }

    public String getDate() {
        return date;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
