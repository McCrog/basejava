package ru.javawebinar.basejava.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateUtil {

    public static final LocalDate NOW = LocalDate.of(3000, 1, 1);

    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }

    public static String toHtml(LocalDate startDate, LocalDate endDate) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("ru"));
//
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(startDate.format(formatter)).append(" - <br/>");
//
//        if (endDate.equals(NOW)) {
//            stringBuilder.append("по настоящее время");
//        } else {
//            stringBuilder.append(endDate.format(formatter));
//        }
//        return stringBuilder.toString();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getDisplayNameOfMonth(startDate))
                .append(" ")
                .append(startDate.getYear())
                .append(" - <br/>");

        if (endDate.equals(NOW)) {
            stringBuilder.append("Сейчас");
        } else {
            stringBuilder.append(getDisplayNameOfMonth(endDate))
                    .append(" ")
                    .append(endDate.getYear());
        }
        return stringBuilder.toString();
    }

    private static String getDisplayNameOfMonth(LocalDate date) {
        Locale locale = new Locale("ru");
        return date.getMonth().getDisplayName(TextStyle.FULL_STANDALONE , locale);
    }
}
