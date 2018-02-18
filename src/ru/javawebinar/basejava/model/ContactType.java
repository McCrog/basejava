package ru.javawebinar.basejava.model;

public enum ContactType {
    PHONE("Тел."),
    MOBILE_PHONE("Мобильный"),
    HOME_PHONE("Домашний тел."),
    SKYPE("Skype"),
    EMAIL("Почта"),
    LINKEDIN("Профиль LinkedIn"),
    GITHUB("Профиль GitHub"),
    STATCKOVERFLOW("Профиль Stackoverflow"),
    HOME_PAGE("Домашняя страница");

    private String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
