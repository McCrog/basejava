package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Collections;
import java.util.List;

public abstract class AbstractStorage<SK> implements Storage {

    protected abstract void doUpdate(Resume r, SK searchKey);

    protected abstract void doSave(Resume r, SK searchKey);

    protected abstract void doDelete(SK searchKey);

    protected abstract SK getSearchKey(String uuid);

    protected abstract boolean checkSearchKeyExist(SK searchKey);

    protected abstract Resume doGet(SK searchKey);

    protected abstract List<Resume> getListStorage();

    public void update(Resume r) {
        SK searchKey = checkResumeNotExist(r.getUuid());
        doUpdate(r, searchKey);
    }

    public void save(Resume r) {
        SK searchKey = checkResumeExist(r.getUuid());
        doSave(r, searchKey);
    }

    public Resume get(String uuid) {
        SK searchKey = checkResumeNotExist(uuid);
        return doGet(searchKey);
    }

    public void delete(String uuid) {
        SK searchKey = checkResumeNotExist(uuid);
        doDelete(searchKey);
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resumeList = getListStorage();
        Collections.sort(resumeList);
        return resumeList;
    }

    private SK checkResumeNotExist(String uuid) {
        SK searchKey = getSearchKey(uuid);

        if (!checkSearchKeyExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }

        return searchKey;
    }

    private SK checkResumeExist(String uuid) {
        SK searchKey = getSearchKey(uuid);

        if (checkSearchKeyExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }

        return searchKey;
    }
}
