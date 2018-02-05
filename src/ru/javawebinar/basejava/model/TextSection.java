package ru.javawebinar.basejava.model;

public class TextSection extends Section {
    private String text;

    public TextSection() {
        this.text = null;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void removeText() {
        this.text = null;
    }

    @Override
    public String toString() {
        return text;
    }
}
