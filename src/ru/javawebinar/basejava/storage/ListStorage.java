package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {

    private List<Resume> storage = new ArrayList<>();

    @Override
    protected void updateResume(Resume r) {
        if (!storage.contains(r)) {
            throw new NotExistStorageException(r.getUuid());
        }

        storage.remove(getIndex(r.getUuid()));
        storage.add(r);
    }

    @Override
    protected void clearStorage() {
        storage.clear();
    }

    @Override
    protected void saveResume(Resume r) {
        if (storage.contains(r)) {
            throw new ExistStorageException(r.getUuid());
        }

        storage.add(r);
    }

    @Override
    protected Resume getResume(String uuid) {
        Resume resume = new Resume(uuid);
        if (!storage.contains(resume)) {
            throw new NotExistStorageException(uuid);
        }

        return storage.get(getIndex(uuid));
    }

    @Override
    protected void deleteResume(String uuid) {
        Resume resume = new Resume(uuid);
        if (!storage.contains(resume)) {
            throw new NotExistStorageException(uuid);
        }

        storage.remove(getIndex(uuid));
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[size]);
    }

    @Override
    protected int getIndex(String uuid) {
        Resume resume = new Resume(uuid);

        return storage.indexOf(resume);
    }
}
