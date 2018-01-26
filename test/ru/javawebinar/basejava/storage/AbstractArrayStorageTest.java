package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    static final String[] unsortedUuidArray = {UUID_3, UUID_1, UUID_2};
    static final String[] sortedUuidArray = {UUID_1, UUID_2, UUID_3};

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    AbstractArrayStorageTest(Storage storage) {
        super(storage);
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
    public abstract void checkOrderSaveToStorage();

    @Test
    public abstract void checkOrderDeleteFromStorage();
}