package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is no directory");
        }

        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    protected void doUpdate(Resume r, File file) {
        doWrite(r, file);
    }

    @Override
    protected void doSave(Resume r, File file) {
        try {
            file.createNewFile();
            doWrite(r, file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected void doDelete(File file) {
        file.delete();
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected boolean checkSearchKeyExist(File file) {
        return file.exists();
    }

    @Override
    protected Resume doGet(File file) {
        return doRead(file);
    }

    @Override
    protected List<Resume> getListStorage() {
        List<Resume> resumes = new ArrayList<>();
        for (File childFile : Objects.requireNonNull(directory.listFiles())) {
            resumes.add(doGet(childFile));
        }
        return resumes;
    }

    @Override
    public void clear() {
        try {
            deleteDirectory(directory);
        } catch (IOException e) {
            throw new StorageException("IO error", directory.getName(), e);
        }
    }

    @Override
    public int size() {
        return Objects.requireNonNull(directory.list()).length;
    }

    protected abstract void doWrite(Resume r, File file);

    protected abstract Resume doRead(File file);

    private static void deleteDirectory(File file) throws IOException {
        for (File childFile : Objects.requireNonNull(file.listFiles())) {
            if (childFile.isDirectory()) {
                deleteDirectory(childFile);
            } else {
                if (!childFile.delete()) {
                    throw new IOException(file.getName());
                }
            }
        }

        if (!file.delete()) {
            throw new IOException(file.getName());
        }
    }
}
