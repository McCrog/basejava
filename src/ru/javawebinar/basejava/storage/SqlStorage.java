package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ConnectionFactory;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.DriverManager;
import java.util.List;

public class SqlStorage implements Storage {
    public static final String UUID = "uuid";
    public static final String FULL_NAME = "full_name";

    private static final String CLEAR_QUERY = "DELETE FROM resume";
    private static final String UPDATE_QUERY = "UPDATE resume SET full_name =? WHERE uuid =?";
    private static final String SAVE_QUERY = "INSERT INTO resume (uuid, full_name) VALUEs (?,?)";
    private static final String DELETE_QUERY = "DELETE FROM resume WHERE uuid =?";
    private static final String GET_QUERY = "SELECT * FROM resume r WHERE r.uuid =?";
    private static final String GET_ALL_SORTED_QUERY = "SELECT * FROM resume ORDER BY uuid";
    private static final String SIZE_QUERY = "SELECT count(*)as RECORDCOUNT FROM resume";

    public static final String CLEAR = "clear";
    public static final String UPDATE = "update";
    public static final String SAVE = "save";
    public static final String DELETE = "delete";
    public static final String GET = "get";
    public static final String GET_ALL_SORTED = "getAllSorted";
    public static final String SIZE = "size";

    private SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        ConnectionFactory connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        sqlHelper = new SqlHelper(connectionFactory);
    }

    @Override
    public void clear() {
        sqlHelper.execute(CLEAR, CLEAR_QUERY);
    }

    @Override
    public void update(Resume r) {
        sqlHelper.execute(UPDATE, UPDATE_QUERY, r.getFullName(), r.getUuid());
    }

    @Override
    public void save(Resume r) {
        sqlHelper.execute(SAVE, SAVE_QUERY, r.getUuid(), r.getFullName());
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.executeGet(GET, GET_QUERY, uuid);
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute(DELETE, DELETE_QUERY, uuid);
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.executeGet(GET_ALL_SORTED, GET_ALL_SORTED_QUERY);
    }

    @Override
    public int size() {
        return sqlHelper.executeGet(SIZE, SIZE_QUERY);
    }
}
