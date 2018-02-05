package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.List;

public class BlockSection extends Section {
    private List<TextBlock> list;

    public BlockSection() {
        this.list = new ArrayList<>();
    }

    public List<TextBlock> getList() {
        return list;
    }

    public void addNewItem() {
        list.add(new TextBlock());
    }

    public void removeItem(TextBlock textBlock) {
        list.remove(textBlock);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (TextBlock s : list) {
            result.append(s).append("\n");
        }
        return result.toString();
    }
}
