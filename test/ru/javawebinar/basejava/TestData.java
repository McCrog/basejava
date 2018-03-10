package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.*;

import java.time.Month;
import java.util.UUID;

public class TestData {
    public static final String UUID_1 = UUID.randomUUID().toString();
    public static final String UUID_2 = UUID.randomUUID().toString();
    public static final String UUID_3 = UUID.randomUUID().toString();
    public static final String UUID_4 = UUID.randomUUID().toString();

    public static final String FULLNAME_1 = "name1";
    public static final String FULLNAME_2 = "name2";
    public static final String FULLNAME_3 = "name3";
    public static final String FULLNAME_4 = "name4";

    public static final Resume R1;
    public static final Resume R2;
    public static final Resume R3;
    public static final Resume R4;

    static {
        R1 = new Resume(UUID_1, FULLNAME_1);
        R2 = new Resume(UUID_2, FULLNAME_2);
        R3 = new Resume(UUID_3, FULLNAME_3);
        R4 = new Resume(UUID_4, FULLNAME_4);

        init(R1);
    }

    private static void init(Resume... resumes) {
        for (Resume r : resumes) {
            r.addContact(ContactType.MAIL, "mail1@ya.ru");
            r.addContact(ContactType.PHONE, "11111");

            r.addContact(ContactType.PHONE, "44444");
            r.addContact(ContactType.SKYPE, "Skype");

            r.addSection(SectionType.OBJECTIVE, new TextSection("Objective1"));
            r.addSection(SectionType.PERSONAL, new TextSection("Personal data"));
            r.addSection(SectionType.ACHIEVEMENT, new ListSection("Achivment11", "Achivment12", "Achivment13"));
            r.addSection(SectionType.QUALIFICATIONS, new ListSection("Java", "SQL", "JavaScript"));
            r.addSection(SectionType.EXPERIENCE,
                    new OrganizationSection(
                            new Organization("Organization11", "http://Organization11.ru",
                                    new Organization.Position(2005, Month.JANUARY, "position1", "content1"),
                                    new Organization.Position(2001, Month.MARCH, 2005, Month.JANUARY, "position2", "content2"))));
            r.addSection(SectionType.EXPERIENCE,
                    new OrganizationSection(
                            new Organization("Organization2", "http://Organization2.ru",
                                    new Organization.Position(2015, Month.JANUARY, "position1", "content1"))));
            r.addSection(SectionType.EDUCATION,
                    new OrganizationSection(
                            new Organization("Institute", null,
                                    new Organization.Position(1996, Month.JANUARY, 2000, Month.DECEMBER, "aspirant", null),
                                    new Organization.Position(2001, Month.MARCH, 2005, Month.JANUARY, "student", "IT facultet")),
                            new Organization("Organization12", "http://Organization12.ru")));
        }
    }
}
