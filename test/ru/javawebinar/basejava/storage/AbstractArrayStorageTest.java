package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractArrayStorageTest {
    protected Storage storage;
    protected static final String UUID_1 = "uuid1";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid3";

    protected String[] unsortedUuidArray = {UUID_3, UUID_1, UUID_2};
    protected String[] sortedUuidArray = {UUID_1, UUID_2, UUID_3};

    protected AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(new Resume(UUID_3));
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
    }

    @Test(expected = NotExistStorageException.class)
    public void clear() {
        storage.clear();
        storage.get(UUID_1);
        storage.get(UUID_2);
        storage.get(UUID_3);
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void update() {
        storage.update(new Resume(UUID_1));
        storage.get(UUID_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void updateotExist() {
        storage.update(new Resume("dummy"));
    }

    @Test
    public void save() {
        storage.save(new Resume("1"));
        storage.save(new Resume("-2"));
        storage.save(new Resume("Save"));
        storage.save(new Resume("Сохранение"));

        storage.get("1");
        storage.get("-2");
        storage.get("Save");
        storage.get("Сохранение");
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(new Resume(UUID_1));
    }

    @Test(expected = StorageException.class)
    public void checkStorageOverflow() {
        int storageLength = ((AbstractArrayStorage) storage).storage.length;
        Assert.assertEquals(storageLength, 10000);

        for (int i = 3; i < storageLength + 1; i++) {
            storage.save(new Resume(UUID_1 + i));
        }
    }

    @Test
    public void get() {
        storage.get(UUID_1);
        storage.get(UUID_2);
        storage.get(UUID_3);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("dummy");
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        storage.delete(UUID_3);
        storage.get(UUID_3);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.get("dummy");
    }

    @Test
    public void getAll() {
        Resume[] resumes = storage.getAll();
        Assert.assertNotNull(resumes);
        Assert.assertEquals(3, resumes.length);
    }

    @Test
    public void size() {
        Assert.assertEquals(3, storage.size());
    }

    @Test
    public abstract void checkOrderSaveToStorage();

    @Test
    public abstract void checkOrderDeleteFromStorage();
}