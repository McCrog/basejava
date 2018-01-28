package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

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
            storage.save(new Resume("uuid1" + i));
        }

        exception.expect(StorageException.class);
        storage.save(new Resume("uuid0"));
    }
}