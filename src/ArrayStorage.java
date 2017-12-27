import org.apache.commons.lang.ArrayUtils;
/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];

    void clear() {
        storage = new Resume[10000];
    }

    void save(Resume r) {
        int id = 0;
        int i = 0;

        boolean isTrue = true;
        while (isTrue && i < storage.length) {
            if (storage[i] == null) {
                id = i;
                isTrue = false;
            }

            i++;
        }

        if (storage[id] == null) {
            storage[id] = r;
        } else {
            System.out.println("Resume storage is full. You can delete one resume or to clear storage.");
        }
    }

    Resume get(String uuid) {
        int count = 0;
        Resume resume = new Resume();
        resume.uuid = "Empty";

        if (storage.length != 0) {
            while (storage[count] != null) {
                if (storage[count].uuid.equals(uuid)) {
                    resume = storage[count];
                    break;
                }
                count++;

                if (count == storage.length)
                    break;
            }
        }
        return resume;
    }

    void delete(String uuid) {
        int count = 0;

        if (storage.length != 0) {
            while (storage[count] != null) {
                if (storage[count].uuid.equals(uuid)) {
                    storage = (Resume[]) ArrayUtils.remove(storage, count);
                    break;
                }

                count++;

                if (count == storage.length)
                    break;
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return getResumeInStorage();
    }

    int size() {
        return getResumeInStorage().length;
    }

    private Resume[] getResumeInStorage() {
        int count = 0;

        if (storage.length != 0) {
            while (storage[count] != null) {
                count++;

                if (count == storage.length)
                    break;
            }
        }

        Resume[] resumeTemp = new Resume[count];
        for (int i = 0; i < resumeTemp.length; i++) {
            resumeTemp[i] = storage[i];
        }

        return resumeTemp;
    }
}