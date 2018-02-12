package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class QualificationSection extends Section {
    private List<Qualification> list;

    public QualificationSection(List<Qualification> list) {
        this.list = new ArrayList<>(list);
    }

    public List<Qualification> getList() {
        return list;
    }

    public void addNewItem(String title, LocalDate startDate, LocalDate endDate, String header, String text) {
        list.add(new Qualification(title, startDate, endDate, header, text));
    }

    public void removeItem(Qualification qualification) {
        list.remove(qualification);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Qualification s : list) {
            result.append(s).append("\n");
        }
        return result.toString();
    }
}
