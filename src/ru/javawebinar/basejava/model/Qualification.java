package ru.javawebinar.basejava.model;

public class Qualification {
    private String title;
    private String startDate;
    private String endDate;
    private String header;
    private String text;

    public Qualification() {
        this.title = null;
        this.startDate = null;
        this.endDate = null;
        this.header = null;
        this.text = null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void removeTitle() {
        this.title = null;
    }

    public void removeStartDate() {
        this.startDate = null;
    }

    public void removeEndDate() {
        this.endDate = null;
    }

    public void removeHeader() {
        this.header = null;
    }

    public void removeText() {
        this.text = null;
    }

    @Override
    public String toString() {
        return title + "\n"
                + startDate + " - " + endDate + "\n"
                + header + "\n"
                + text;
    }
}
