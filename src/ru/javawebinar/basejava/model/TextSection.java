package ru.javawebinar.basejava.model;

public class TextSection {
    private final SectionType sectionType;
    private String text;

    public TextSection(SectionType sectionType) {
        this.sectionType = sectionType;
        this.text = null;
    }

    public SectionType getSectionType() {
        return sectionType;
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
        return sectionType.getTitle() + '(' + text + ')';
    }
}
