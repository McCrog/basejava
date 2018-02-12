package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.List;

public class ListSection extends Section {
    private final List<String> list;

    public ListSection(List<String> list) {
        this.list = new ArrayList<>(list);
    }

    public List<String> getList() {
        return list;
    }

    public void addItem(String item) {
        list.add(item);
    }

    public void removeItem(String item) {
        list.remove(item);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (String s : list) {
            result.append(s).append("\n");
        }
        return result.toString();
    }
}
