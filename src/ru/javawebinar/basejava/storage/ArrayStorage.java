package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int size = 0;

    public void clear() {
        storage = new Resume[10000];
        size = 0;
    }

    public void update(Resume r) {
        int index = checkingPresenceOfResume(r.getUuid());
        if (index == -1) {
            System.out.println("Резюме " + r.getUuid() + " не найдено.");
        } else {
            storage[index] = r;
        }
    }

    public void save(Resume r) {
        int index = checkingPresenceOfResume(r.getUuid());
        if (index != -1) {
            System.out.println("Резюме " + r.getUuid() + " присутствует в хранилище.");
        } else if (size == storage.length) {
            System.out.println("Хранилище заполнено. Вы можете удалить резюме или полностью очистить хранилище.");
        } else {
            storage[size] = r;
            size++;
        }
    }

    public Resume get(String uuid) {
        int index = checkingPresenceOfResume(uuid);
        if (index != -1) {
            System.out.println("Резюме " + uuid + " найдено.");
            return storage[index];
        } else {
            System.out.println("Резюме " + uuid + " не найдено.");
            return null;
        }
    }

    public void delete(String uuid) {
        int index = checkingPresenceOfResume(uuid);
        if (index == -1) {
            System.out.println("Резюме " + uuid + " не найдено.");
        } else {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
            System.out.println("Резюме " + uuid + " было удалено.");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        Resume[] resumes = new Resume[size];

        System.arraycopy(storage, 0, resumes, 0, size);

        return resumes;
    }

    public int size() {
        return size;
    }

    private int checkingPresenceOfResume(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}