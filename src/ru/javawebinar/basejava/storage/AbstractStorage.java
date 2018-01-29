package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<SK> implements Storage {
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    protected abstract void doUpdate(Resume r, SK searchKey);

    protected abstract void doSave(Resume r, SK searchKey);

    protected abstract void doDelete(SK searchKey);

    protected abstract SK getSearchKey(String uuid);

    protected abstract boolean checkSearchKeyExist(SK searchKey);

    protected abstract Resume doGet(SK searchKey);

    protected abstract List<Resume> getListStorage();

    public void update(Resume r) {
        LOG.info("Update " + r);
        SK searchKey = checkResumeNotExist(r.getUuid());
        doUpdate(r, searchKey);
    }

    public void save(Resume r) {
        LOG.info("Save " + r);
        SK searchKey = checkResumeExist(r.getUuid());
        doSave(r, searchKey);
    }

    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        SK searchKey = checkResumeNotExist(uuid);
        return doGet(searchKey);
    }

    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        SK searchKey = checkResumeNotExist(uuid);
        doDelete(searchKey);
    }

    @Override
    public List<Resume> getAllSorted() {
        LOG.info("getAllSorted");
        List<Resume> resumeList = getListStorage();
        Collections.sort(resumeList);
        return resumeList;
    }

    private SK checkResumeNotExist(String uuid) {
        SK searchKey = getSearchKey(uuid);

        if (!checkSearchKeyExist(searchKey)) {
            LOG.warning("Resume " + uuid + " not exist");
            throw new NotExistStorageException(uuid);
        }

        return searchKey;
    }

    private SK checkResumeExist(String uuid) {
        SK searchKey = getSearchKey(uuid);

        if (checkSearchKeyExist(searchKey)) {
            LOG.warning("Resume " + uuid + " already exist");
            throw new ExistStorageException(uuid);
        }

        return searchKey;
    }
}
