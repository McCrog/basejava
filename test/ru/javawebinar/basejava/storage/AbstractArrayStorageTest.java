package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractArrayStorageTest {
    protected Storage storage;
    protected static final String UUID_1 = "uuid1";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid3";
    protected static final Resume RESUME_1;
    protected static final Resume RESUME_2;
    protected static final Resume RESUME_3;

    static {
        RESUME_1 = new Resume(UUID_1);
        RESUME_2 = new Resume(UUID_2);
        RESUME_3 = new Resume(UUID_3);
    }

    protected static final String[] unsortedUuidArray = {UUID_3, UUID_1, UUID_2};
    protected static final String[] sortedUuidArray = {UUID_1, UUID_2, UUID_3};

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    protected AbstractArrayStorageTest(Storage storage) {
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
        storage.update(new Resume(UUID_1));
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
        storage.save(new Resume(UUID_1));
    }

    @Test
    public void checkStorageOverflow() {
        int storageLength = AbstractArrayStorage.STORAGE_LIMIT;
        Assert.assertEquals(10000, storageLength);

        for (int i = 3; i < storageLength; i++) {
            storage.save(new Resume(UUID_1 + i));
        }

        exception.expect(StorageException.class);
        storage.save(new Resume("uuid0"));
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
    public void getAll() {
        Resume[] resumes = storage.getAll();
        Assert.assertNotNull(resumes);
        Assert.assertEquals(3, resumes.length);
    }

    @Test
    public void size() {
        assertSize(3);
    }

    @Test
    public abstract void checkOrderSaveToStorage();

    @Test
    public abstract void checkOrderDeleteFromStorage();

    private void assertGet(Resume resume) {
        Assert.assertEquals(resume, storage.get(resume.getUuid()));
    }

    private void assertSize(int size) {
        Assert.assertEquals(size, storage.size());
    }
}