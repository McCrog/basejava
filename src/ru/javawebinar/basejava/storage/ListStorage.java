package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {

    private List<Resume> storage = new ArrayList<>();

    @Override
    public void update(Resume r) {
        if (!storage.contains(r)) {
            throw new NotExistStorageException(r.getUuid());
        }

        storage.remove(getIndex(r.getUuid()));
        storage.add(r);
    }

    @Override
    public void save(Resume r) {
        if (storage.contains(r)) {
            throw new ExistStorageException(r.getUuid());
        }

        storage.add(r);
        size++;
    }

    @Override
    public Resume get(String uuid) {
        Resume resume = new Resume(uuid);
        if (!storage.contains(resume)) {
            throw new NotExistStorageException(uuid);
        }

        return storage.get(getIndex(uuid));
    }

    @Override
    public void delete(String uuid) {
        Resume resume = new Resume(uuid);
        if (!storage.contains(resume)) {
            throw new NotExistStorageException(uuid);
        }

        storage.remove(getIndex(uuid));
        size--;
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[size]);
    }

    @Override
    protected int getIndex(String uuid) {
        Resume resume = new Resume(uuid);
        int index = storage.indexOf(resume);

        return index;
    }

    @Override
    protected void clearStorage() {
        storage.clear();
    }
}
