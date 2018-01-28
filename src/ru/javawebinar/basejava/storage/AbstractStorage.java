package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage implements Storage {

    public void update(Resume r) {
        Object searchKey = checkResumeNotExist(r.getUuid());
        doUpdate(r, searchKey);
    }

    public void save(Resume r) {
        Object searchKey = checkResumeExist(r.getUuid());
        doSave(r, searchKey);
    }

    public Resume get(String uuid) {
        Object searchKey = checkResumeNotExist(uuid);
        return doGet(searchKey);
    }

    public void delete(String uuid) {
        Object searchKey = checkResumeNotExist(uuid);
        doDelete(searchKey);
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resumeList = getListStorage();
        resumeList.sort(Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid));
        return resumeList;
    }

    private Object checkResumeNotExist(String uuid) {
        Object searchKey = getSearchKey(uuid);

        if (!checkSearchKeyExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }

        return searchKey;
    }

    private Object checkResumeExist(String uuid) {
        Object searchKey = getSearchKey(uuid);

        if (checkSearchKeyExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }

        return searchKey;
    }

    protected abstract void doUpdate(Resume r, Object searchKey);

    protected abstract void doSave(Resume r, Object searchKey);

    protected abstract void doDelete(Object searchKey);

    protected abstract Object getSearchKey(String uuid);

    protected abstract boolean checkSearchKeyExist(Object searchKey);

    protected abstract Resume doGet(Object searchKey);

    protected abstract List<Resume> getListStorage();
}
