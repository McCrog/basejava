package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListStorage extends AbstractStorage {

    private List<Resume> storage = new ArrayList<>();

    @Override
    protected void doUpdate(Resume r) {
        storage.remove(getIndex(r.getUuid()));
        storage.add(r);
    }

    @Override
    protected void clearStorage() {
        storage.clear();
    }

    @Override
    protected void doSave(Resume r) {
        storage.add(r);
    }

    @Override
    protected Resume doGet(Resume searchKey) {
        return storage.get(getIndex(searchKey.getUuid()));
    }

    @Override
    protected void doDelete(String uuid) {
        storage.remove(getIndex(uuid));
    }

    @Override
    public Resume[] getAll() {
        int listSize = size();
        return storage.toArray(new Resume[listSize]);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected int getIndex(String uuid) {
        int searchIndex = 0;

        for (Resume r : storage) {
            if (Objects.equals(r.getUuid(), uuid)) {
                break;
            }
            searchIndex++;
        }
        return searchIndex;
    }

    @Override
    protected boolean checkSearchKeyExist(Resume searchKey) {
        return storage.contains(searchKey);
    }
}
