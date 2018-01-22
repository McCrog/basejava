package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {
    protected int size = 0;

    public void clear() {
        clearStorage();
        size = 0;
    }

    public void update(Resume r) {
        throw new UnsupportedOperationException();
    }

    public void save(Resume r) {
        throw new UnsupportedOperationException();
    }

    public Resume get(String uuid) {
        throw new UnsupportedOperationException();
    }

    public void delete(String uuid) {
        throw new UnsupportedOperationException();
    }

    public Resume[] getAll() {
        throw new UnsupportedOperationException();
    }

    public int size() {
        return size;
    }

    protected abstract int getIndex(String uuid);
    protected abstract void clearStorage();
}
