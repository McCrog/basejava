package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.util.List;

public abstract class AbstractStorageTest {
    protected static final File STORAGE_DIR = new File("./file_storage");

    Storage storage;
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";

    private static final String FULLNAME_1 = "name1";
    private static final String FULLNAME_2 = "name2";
    private static final String FULLNAME_3 = "name3";
    private static final String FULLNAME_4 = "name4";

    private static final Resume R1;
    private static final Resume R2;
    private static final Resume R3;
    private static final Resume R4;

    static {
        R1 = new Resume(UUID_1, FULLNAME_1);
        R2 = new Resume(UUID_2, FULLNAME_2);
        R3 = new Resume(UUID_3, FULLNAME_3);
        R4 = new Resume(UUID_4, FULLNAME_4);

//        R1.addContact(ContactType.EMAIL, "mail1@ya.ru");
//        R1.addContact(ContactType.PHONE, "11111");
//        R1.addSection(SectionType.OBJECTIVE, new TextSection("Objective1"));
//        R1.addSection(SectionType.PERSONAL, new TextSection("Personal data"));
//        R1.addSection(SectionType.ACHIEVEMENT, new ListSection("Achivment11", "Achivment12", "Achivment13"));
//        R1.addSection(SectionType.QUALIFICATIONS, new ListSection("Java", "SQL", "JavaScript"));
//        R1.addSection(SectionType.EXPERIENCE,
//                new OrganizationSection(
//                        new Organization("Organization11", "http://Organization11.ru",
//                                new Organization.Position(2005, Month.JANUARY, "position1", "content1"),
//                                new Organization.Position(2001, Month.MARCH, 2005, Month.JANUARY, "position2", "content2"))));
//        R1.addSection(SectionType.EDUCATION,
//                new OrganizationSection(
//                        new Organization("Institute", null,
//                                new Organization.Position(1996, Month.JANUARY, 2000, Month.DECEMBER, "aspirant", null),
//                                new Organization.Position(2001, Month.MARCH, 2005, Month.JANUARY, "student", "IT facultet")),
//                        new Organization("Organization12", "http://Organization12.ru")));
//        R2.addContact(ContactType.SKYPE, "skype2");
//        R2.addContact(ContactType.PHONE, "22222");
//        R1.addSection(SectionType.EXPERIENCE,
//                new OrganizationSection(
//                        new Organization("Organization2", "http://Organization2.ru",
//                                new Organization.Position(2015, Month.JANUARY, "position1", "content1"))));
    }


    AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(R3);
        storage.save(R1);
        storage.save(R2);
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
    }

    @Test
    public void update() {
        Resume newResume = new Resume(UUID_1, "New Name");
//        newResume.addContact(ContactType.EMAIL, "mail1@google.com");
//        newResume.addContact(ContactType.SKYPE, "NewSkype");
//        newResume.addContact(ContactType.MOBILE_PHONE, "+7 921 222-22-22");
        storage.update(newResume);
        Assert.assertTrue(newResume.equals(storage.get(UUID_1)));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(new Resume("dummy"));
    }

    @Test
    public void save() {
        storage.save(R4);
        assertSize(4);
        assertGet(R4);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        assertGet(R1);
        storage.save(new Resume(UUID_1, FULLNAME_1));
    }

    @Test
    public void get() {
        assertGet(R1);
        assertGet(R2);
        assertGet(R3);
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