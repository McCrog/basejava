package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.basejava.model.ContactsType.*;
import static ru.javawebinar.basejava.model.SectionType.*;

public abstract class AbstractStorageTest {
    Storage storage;
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";

    private static final String FULLNAME_1 = "name1";
    private static final String FULLNAME_2 = "name2";
    private static final String FULLNAME_3 = "name3";

    private static final Resume RESUME_1;
    private static final Resume RESUME_2;
    private static final Resume RESUME_3;

    static {
        RESUME_1 = new Resume(UUID_1, FULLNAME_1);
        RESUME_2 = new Resume(UUID_2, FULLNAME_2);
        RESUME_3 = new Resume(UUID_3, FULLNAME_3);
    }


    AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(RESUME_3);
        storage.save(RESUME_1);
        storage.save(RESUME_2);

        initializeResume(RESUME_1);
        initializeResume(RESUME_2);
        initializeResume(RESUME_3);
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
    }

    @Test
    public void update() {
        storage.update(new Resume(UUID_1, FULLNAME_1));
        assertGet(RESUME_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(new Resume("dummy"));
    }

    @Test
    public void save() {
        Resume[] resumes = {new Resume("1"),
                new Resume("-2"),
                new Resume("Save"),
                new Resume("Сохранение")
        };

        storage.save(resumes[0]);
        storage.save(resumes[1]);
        storage.save(resumes[2]);
        storage.save(resumes[3]);

        assertSize(7);

        assertGet(resumes[0]);
        assertGet(resumes[1]);
        assertGet(resumes[2]);
        assertGet(resumes[3]);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        assertGet(RESUME_1);
        storage.save(new Resume(UUID_1, FULLNAME_1));
    }

    @Test
    public void get() {
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("dummy");
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        storage.delete(UUID_3);
        assertSize(2);
        storage.get(UUID_3);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete("dummy");
    }

    @Test
    public void getAllSorted() {
        List<Resume> existSortedStorage = storage.getAllSorted();
        Assert.assertEquals("name3", existSortedStorage.get(2).getFullName());
    }

    @Test
    public void size() {
        assertSize(3);
    }

    private void assertGet(Resume resume) {
        Assert.assertEquals(resume, storage.get(resume.getUuid()));
    }

    private void assertSize(int size) {
        Assert.assertEquals(size, storage.size());
    }

    private void initializeResume(Resume resume) {
        // Contacts section
        RESUME_1.addContact(PHONE, "344-355-356");
        RESUME_1.addContact(MOBILE_PHONE, "+7 (901) 123-44-55");
        RESUME_1.addContact(HOME_PHONE, "44-55-56");
        RESUME_1.addContact(SKYPE, "petrov.nikola");
        RESUME_1.addContact(EMAIL, "petrov_nikola@gmail.com");
        RESUME_1.addContact(LINKEDIN, "https://www.linkedin.com/petrov_nikola");
        RESUME_1.addContact(GITHUB, "https://github.com/petrov_nikola");
        RESUME_1.addContact(STATCKOVERFLOW, "https://stackoverflow.com/petrov_nikola");
        RESUME_1.addContact(HOME_PAGE, "petrov.nikola.ru");

        // Text section. OBJECTIVE
        resume.addSection(OBJECTIVE, new TextSection("Инженер-программист"));

        // Text section. PERSONAL
        resume.addSection(PERSONAL, new TextSection("Аналитический склад ума, сильная логика, креативность, " +
                "инициативность. Пурист кода и архитектуры."));

        // List section. ACHIEVEMENT
        List<String> achievement = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            achievement.add("Реализация двухфакторной аутентификации" + i);
        }
        resume.addSection(ACHIEVEMENT, new ListSection(achievement));

        // List section. QUALIFICATIONS
        List<String> qualifications = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            qualifications.add("DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle" + i);
        }
        resume.addSection(QUALIFICATIONS, new ListSection(qualifications));

        // Organization section. EXPERIENCE
        List<Organization> experience = new ArrayList<>();

        // First organization
        LocalDate startDateLastPosition = LocalDate.of(2013, Month.APRIL, 1);
        LocalDate endDateLastPosition = LocalDate.of(2017, Month.FEBRUARY, 1);

        List<OrganizationPosition> firstOrganization = new ArrayList<>();
        firstOrganization.add(new OrganizationPosition(
                startDateLastPosition,
                endDateLastPosition,
                "Разработчик Java",
                "Проектирование и разработка."
        ));

        // Second organization
        LocalDate startDateSecondPosition = LocalDate.of(2010, Month.SEPTEMBER, 1);
        LocalDate endDateSecondPosition = LocalDate.of(2013, Month.APRIL, 1);

        List<OrganizationPosition> secondOrganization = new ArrayList<>();
                secondOrganization.add(new OrganizationPosition(
                startDateSecondPosition,
                endDateSecondPosition,
                "Разработчик Java",
                "Проектирование и разработка."
        ));

        experience.add(new Organization("Java Web", "http://javaweb.com/", firstOrganization));
        experience.add(new Organization("Java Online", "http://javaonline.com/", secondOrganization));
        resume.addSection(EXPERIENCE, new OrganizationSection(experience));

        // Organization section. EDUCATION
        List<Organization> education = new ArrayList<>();

        // First organization
        LocalDate startDateLastEducation = LocalDate.of(2008, Month.SEPTEMBER, 1);
        LocalDate endDateLastEducation = LocalDate.of(2013, Month.JUNE, 1);

        List<OrganizationPosition> firstEducation = new ArrayList<>();
        firstEducation.add(new OrganizationPosition(
                startDateLastEducation,
                endDateLastEducation,
                "Студент",
                null
        ));

        // Second organization
        LocalDate startDateLowEducation = LocalDate.of(1998, Month.SEPTEMBER, 1);
        LocalDate endDateLowEducation = LocalDate.of(2006, Month.JUNE, 1);

        List<OrganizationPosition> lowSchoolEducation = new ArrayList<>();
        lowSchoolEducation.add(new OrganizationPosition(
                startDateLowEducation,
                endDateLowEducation,
                "Отличник",
                null
        ));

        education.add(new Organization("Московский университет", "http://mscu.ru/", firstEducation));
        education.add(new Organization("Школа №5", "http://school.com/", lowSchoolEducation));
        resume.addSection(EDUCATION, new OrganizationSection(education));

        education.get(1).addPosition(
                LocalDate.of(2006, Month.SEPTEMBER, 1),
                LocalDate.of(2008, Month.JUNE, 1),
                "Староста класса",
                "Решение организационных вопросов");
    }
}