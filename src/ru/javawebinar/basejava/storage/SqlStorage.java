package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    private static final String UUID = "uuid";
    private static final String FULL_NAME = "full_name";

    private static final String CLEAR_QUERY = "DELETE FROM resume";
    private static final String UPDATE_QUERY = "UPDATE resume SET full_name =? WHERE uuid =?";
    private static final String SAVE_QUERY = "INSERT INTO resume (uuid, full_name) VALUEs (?,?)";
    private static final String DELETE_QUERY = "DELETE FROM resume WHERE uuid =?";
    private static final String GET_QUERY = "SELECT * FROM resume r WHERE r.uuid =?";
    private static final String GET_ALL_SORTED_QUERY = "SELECT * FROM resume ORDER BY uuid";
    private static final String SIZE_QUERY = "SELECT count(*)as RECORDCOUNT FROM resume";

    private SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.execute(CLEAR_QUERY, preparedStatement -> {
            preparedStatement.execute();
            return null;
        });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.execute(UPDATE_QUERY, preparedStatement -> {
            preparedStatement.setString(1, r.getFullName());
            preparedStatement.setString(2, r.getUuid());
            if (preparedStatement.executeUpdate() == 0) {
                throw new NotExistStorageException(r.getUuid());
            }
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.execute(SAVE_QUERY, preparedStatement -> {
            preparedStatement.setString(1, r.getUuid());
            preparedStatement.setString(2, r.getFullName());
            preparedStatement.executeUpdate();
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute(GET_QUERY, preparedStatement -> {
            preparedStatement.setString(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, resultSet.getString(FULL_NAME));
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute(DELETE_QUERY, preparedStatement -> {
            preparedStatement.setString(1, uuid);
            if (preparedStatement.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.execute(GET_ALL_SORTED_QUERY, preparedStatement -> {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Resume> resumes = new ArrayList<>();
            while (resultSet.next()) {
                resumes.add(new Resume(resultSet.getString(UUID), resultSet.getString(FULL_NAME)));
            }
            return resumes;
        });
    }

    @Override
    public int size() {
        return sqlHelper.execute(SIZE_QUERY, preparedStatement -> {
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() ? resultSet.getInt("RECORDCOUNT") : 0;
        });
    }
}
