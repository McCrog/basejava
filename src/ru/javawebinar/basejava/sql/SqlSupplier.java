package ru.javawebinar.basejava.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlSupplier<T> {
    T get(PreparedStatement preparedStatement) throws SQLException;
}
