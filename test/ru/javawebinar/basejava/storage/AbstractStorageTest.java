package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

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

        RESUME_1.addContacts(ContactsType.PHONE, "77-77-77");
        RESUME_1.addContacts(ContactsType.EMAIL, "mail@gmail.com");

        RESUME_1.addSections(SectionType.OBJECTIVE, new TextSection("Java"));
        RESUME_1.addSections(SectionType.PERSONAL, new TextSection("Аналитический склад ума"));

        RESUME_1.addSections(
                SectionType.ACHIEVEMENT,
                new ListSection(Arrays.asList("Реализация протоколов", "Налаживание процесса"))
        );

        RESUME_1.addSections(
                SectionType.QUALIFICATIONS,
                new ListSection(Arrays.asList("Git", "Java 8", "Bootstrap.js", "Maven", "Tomcat")))
        ;

        RESUME_1.addSections(SectionType.EXPERIENCE,
                new QualificationSection(Arrays.asList(new Qualification(
                        "Java.ru",
                        LocalDate.of(2015, Month.APRIL, 1),
                        LocalDate.of(2016, Month.DECEMBER, 1),
                        "Java",
                        "Проектирование"
                ), new Qualification(
                        "Java.ru",
                        LocalDate.of(2017, Month.JANUARY, 1),
                        LocalDate.of(2017, Month.DECEMBER, 1),
                        "Java",
                        "Разработка"
                ))));


        RESUME_1.addSections(SectionType.EDUCATION,
                new QualificationSection(Arrays.asList(new Qualification(
                        "Школа №5",
                        LocalDate.of(2000, Month.SEPTEMBER, 1),
                        LocalDate.of(2010, Month.JUNE, 1),
                        "Ученик",
                        "Отличник"
                ), new Qualification(
                        "Московский университет",
                        LocalDate.of(2010, Month.SEPTEMBER, 1),
                        LocalDate.of(2015, Month.JUNE, 1),
                        "Студент",
                        "Староста"
                ))));
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
}