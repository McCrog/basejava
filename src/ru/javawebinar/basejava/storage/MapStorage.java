package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {

    private Map<String, Resume> storage = new HashMap<>();

    @Override
    protected void clearStorage() {
        storage.clear();
    }

    @Override
    protected void updateResume(Resume r) {
        if (!storage.containsKey(r.getUuid())) {
            throw new NotExistStorageException(r.getUuid());
        }

        storage.remove(r.getUuid());
        storage.put(r.getUuid(), r);
    }

    @Override
    protected void saveResume(Resume r) {
        if (storage.containsKey(r.getUuid())) {
            throw new ExistStorageException(r.getUuid());
        }
        storage.put(r.getUuid(), r);
    }

    @Override
    protected Resume getResume(String uuid) {
        if (!storage.containsKey(uuid)) {
            throw new NotExistStorageException(uuid);
        }
        return storage.get(uuid);
    }

    @Override
    protected void deleteResume(String uuid) {
        if (!storage.containsKey(uuid)) {
            throw new NotExistStorageException(uuid);
        }

        storage.remove(uuid);
    }

    @Override
    public Resume[] getAll() {
        Resume[] resumes = new Resume[size];
        int i = 0;
        for (Map.Entry<String, Resume> entry : storage.entrySet()) {
            resumes[i] = (entry.getValue());
            i++;
        }
        return resumes;
    }

    @Override
    protected int getIndex(String uuid) {
        throw new UnsupportedOperationException();
    }
}
