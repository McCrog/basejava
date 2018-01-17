package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import ru.javawebinar.basejava.model.Resume;

public class SortedArrayStorageTest extends AbstractArrayStorageTest {

    public SortedArrayStorageTest() {
        super(new SortedArrayStorage());
    }

    @Override
    public void checkOrderSaveToStorage() {
        Resume[] resumes = storage.getAll();
        for (int i = 0; i < resumes.length; i++) {
            String uuid = resumes[i].getUuid();
            Assert.assertEquals(uuid, sortedUuidArray[i]);
        }
    }

    @Override
    public void checkOrderDeleteFromStorage() {
        storage.delete(UUID_1);
        Resume[] resumes = storage.getAll();
        String uuid = resumes[0].getUuid();
        Assert.assertEquals(uuid, UUID_2);
    }
}