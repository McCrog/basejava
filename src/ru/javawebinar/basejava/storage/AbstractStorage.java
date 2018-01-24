package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {
    public void clear() {
        clearStorage();
    }

    public void update(Resume r) {
        checkResumeNotExist(r);
        doUpdate(r);
    }

    public void save(Resume r) {
        checkResumeExist(r);
        doSave(r);
    }

    public Resume get(String uuid) {
        Resume searchKey = getSearchKey(uuid);
        checkResumeNotExist(searchKey);
        return doGet(searchKey);
    }

    public void delete(String uuid) {
        Resume searchKey = getSearchKey(uuid);
        checkResumeNotExist(searchKey);
        doDelete(uuid);
    }

    private void checkResumeNotExist(Resume r) {
        if (!checkSearchKeyExist(r)) {
            throw new NotExistStorageException(r.getUuid());
        }
    }

    private void checkResumeExist(Resume r) {
        if (checkSearchKeyExist(r)) {
            throw new ExistStorageException(r.getUuid());
        }
    }

    private Resume getSearchKey(String uuid) {
        return new Resume(uuid);
    }

    protected abstract void clearStorage();

    protected abstract void doUpdate(Resume r);

    protected abstract void doSave(Resume r);

    protected abstract void doDelete(String uuid);

    protected abstract int getIndex(String uuid);

    protected abstract boolean checkSearchKeyExist(Resume searchKey);

    protected abstract Resume doGet(Resume searchKey);
}
