package ru.javawebinar.basejava.model;

import java.time.LocalDate;

public class Qualification {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String header;
    private String text;

    public Qualification(String title, LocalDate startDate, LocalDate endDate, String header, String text) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.header = header;
        this.text = text;
    }

    @Override
    public String toString() {
        return title + "\n"
                + startDate + " - " + endDate + "\n"
                + header + "\n"
                + text;
    }
}
