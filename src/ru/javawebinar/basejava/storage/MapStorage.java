package ru.javawebinar.basejava.storage;

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
    protected void doUpdate(Resume r) {
        storage.remove(r.getUuid());
        storage.put(r.getUuid(), r);
    }

    @Override
    protected void doSave(Resume r) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected Resume doGet(Resume searchKey) {
        return storage.get(searchKey.getUuid());
    }

    @Override
    protected void doDelete(String uuid) {
        storage.remove(uuid);
    }

    @Override
    public Resume[] getAll() {
        int mapSize = size();
        return storage.values().toArray(new Resume[mapSize]);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected int getIndex(String uuid) {
        int searchIndex = 0;

        for (Map.Entry<String, Resume> entry : storage.entrySet()) {
            if (entry.getValue().getUuid().equals(uuid)) {
                break;
            }
            searchIndex++;
        }
        return searchIndex;
    }

    @Override
    protected boolean checkSearchKeyExist(Resume searchKey) {
        return storage.containsKey(searchKey.getUuid());
    }
}
