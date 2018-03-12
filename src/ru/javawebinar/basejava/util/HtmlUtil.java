package ru.javawebinar.basejava.util;

import ru.javawebinar.basejava.model.ContactType;

public class HtmlUtil {
    public static String contactToHtml(ContactType type, String value) {
        String summery = "";
        switch (type) {
            case PHONE:
            case MOBILE:
            case HOME_PHONE:
                summery = value == null ? "" : type.getTitle() + ": " + value;
                break;
            case SKYPE:
                summery = type.getTitle() + ": " + toLink("skype:" + value, value);
                break;
            case MAIL:
                summery = type.getTitle() + ": " + toLink("mailto:" + value, value);
                break;
            case LINKEDIN:
            case GITHUB:
            case STATCKOVERFLOW:
            case HOME_PAGE:
                summery = toLink(value, type.getTitle());
                break;
            default:
                throw new IllegalArgumentException();
        }
        return summery;
    }

    private static String toLink(String href, String title) {
        return "<a href='" + href + "'>" + title + "</a>";
    }
}
