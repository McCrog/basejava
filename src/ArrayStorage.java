/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int size = 0;

    void clear() {
        storage = new Resume[10000];
    }

    void save(Resume r) {
        if (size == storage.length) {
            System.out.println("Хранилище заполнено");
            return;
        }

        storage[size] = r;
        size++;
    }

    Resume get(String uuid) {
        Resume resume = null;
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                resume = storage[i];
                break;
            }
        }

        return resume;
    }

    void delete(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                storage[i] = storage[size-1];
                storage[size-1] = null;
                size--;
                break;
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] resumes = new Resume[size];

        System.arraycopy(storage, 0, resumes, 0, size);

        return resumes;
    }

    int size() {
        return size;
    }
}