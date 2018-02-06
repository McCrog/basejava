package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.List;

public class QualificationSection extends Section {
    private List<Qualification> list;

    public QualificationSection() {
        this.list = new ArrayList<>();
    }

    public List<Qualification> getList() {
        return list;
    }

    public void addNewItem() {
        list.add(new Qualification());
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
