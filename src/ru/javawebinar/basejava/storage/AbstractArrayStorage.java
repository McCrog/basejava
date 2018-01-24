package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10000;
    protected int size = 0;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];

    @Override
    protected void clearStorage() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    protected void doUpdate(Resume r) {
        int index = getIndex(r.getUuid());

        storage[index] = r;
    }

    @Override
    protected void doSave(Resume r) {
        int index = getIndex(r.getUuid());

        if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", r.getUuid());
        }

        saveElement(r, index);
        size++;
    }

    @Override
    protected Resume doGet(Resume searchKey) {
        int index = getIndex(searchKey.getUuid());
        return storage[index];
    }

    @Override
    protected void doDelete(String uuid) {
        int index = getIndex(uuid);

        deleteElement(index);
        storage[size - 1] = null;
        size--;
    }

    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    protected boolean checkSearchKeyExist(Resume searchKey) {
        int index = getIndex(searchKey.getUuid());
        return index >= 0;
    }

    protected abstract void saveElement(Resume resume, int index);

    protected abstract void deleteElement(int index);
}