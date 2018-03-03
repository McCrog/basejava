package ru.javawebinar.basejava.sql;

import org.postgresql.util.PSQLException;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.basejava.storage.SqlStorage.*;

public class SqlHelper {
    private ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void execute(String method, String sql, Object... params) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int cnt = 0;
            for (Object param : params) {
                preparedStatement.setObject(++cnt, param);
            }

            checkStorageException(method, preparedStatement);

        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public <T extends Object> T executeGet(String method, String sql, Object... params) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int cnt = 0;
            for (Object param : params) {
                preparedStatement.setObject(++cnt, param);
            }
            ResultSet resultSet = preparedStatement.executeQuery();

            switch (method) {
                case GET:
                    return (T) getItem(resultSet, () -> new Resume((String) params[0], resultSet.getString(FULL_NAME)));
                case GET_ALL_SORTED:
                    return (T) getList(resultSet, () -> new Resume(resultSet.getString(UUID), resultSet.getString(FULL_NAME)));
                case SIZE:
                    return (T) getItem(resultSet, () -> resultSet.getInt("RECORDCOUNT"));
                default:
                    throw new IllegalStateException();
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    private <T> List<T> getList(ResultSet resultSet, SqlSupplier<T> sqlSupplier) throws SQLException {
        List<T> resumes = new ArrayList<>();
        while (resultSet.next()) {
            resumes.add(sqlSupplier.get());
        }
        return resumes;
    }

    private <T> T getItem(ResultSet resultSet, SqlSupplier<T> sqlSupplier) throws SQLException {
        T item = null;
        if (resultSet.next()) {
            item = sqlSupplier.get();
        } else if (!resultSet.next()) {
            throw new NotExistStorageException(null);
        }
        return item;
    }

    private interface SqlSupplier<T> {
        T get() throws SQLException;
    }

    private void checkStorageException(String method, PreparedStatement preparedStatement) {
        try {
            switch (method) {
                case CLEAR:
                case SAVE:
                    try {
                        preparedStatement.execute();
                    } catch (PSQLException e) {
                        throw new ExistStorageException(null);
                    }
                    break;
                case DELETE:
                    try {
                        preparedStatement.executeQuery();
                    } catch (PSQLException e) {
                        throw new NotExistStorageException(null);
                    }
                    break;
                case UPDATE:
                    if (preparedStatement.executeUpdate() == 0) {
                        throw new NotExistStorageException(null);
                    }
                    break;
                default:
                    throw new IllegalStateException();
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}
