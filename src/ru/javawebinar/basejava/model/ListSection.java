package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.List;

public class ListSection {
    private final SectionType sectionType;
    private final List<String> list;

    public ListSection(SectionType sectionType) {
        this.sectionType = sectionType;
        this.list = new ArrayList<>();
    }

    public SectionType getSectionType() {
        return sectionType;
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
        return sectionType.getTitle() + '(' + result + ')';
    }
}
