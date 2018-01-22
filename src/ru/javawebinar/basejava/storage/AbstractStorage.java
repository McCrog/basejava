package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {
    protected int size = 0;

    public void clear() {
        clearStorage();
        size = 0;
    }

    public void update(Resume r) {
        updateResume(r);
    }

    public void save(Resume r) {
        saveResume(r);
        size++;
    }

    public Resume get(String uuid) {
        return getResume(uuid);
    }

    public void delete(String uuid) {
        deleteResume(uuid);
        size--;
    }

    public int size() {
        return size;
    }

    protected abstract void clearStorage();

    protected abstract void updateResume(Resume r);

    protected abstract void saveResume(Resume r);

    protected abstract Resume getResume(String uuid);

    protected abstract void deleteResume(String uuid);

    protected abstract int getIndex(String uuid);
}
