package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.Section;
import ru.javawebinar.basejava.model.SectionType;
import ru.javawebinar.basejava.sql.SqlHelper;
import ru.javawebinar.basejava.util.JsonParser;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {
    private static final String UUID = "uuid";
    private static final String FULL_NAME = "full_name";

    private static final String CLEAR_QUERY =  "DELETE FROM resume";

    private static final String UPDATE_QUERY = "UPDATE resume SET full_name =? WHERE uuid =?";

    private static final String SAVE_QUERY =   "INSERT INTO resume (uuid, full_name) VALUES (?,?)";

    private static final String DELETE_QUERY = "DELETE FROM resume WHERE uuid =?";

    private static final String SAVE_CONTACT_QUERY = "INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)";

    private static final String SAVE_SECTION_QUERY = "INSERT INTO section (resume_uuid, type, value) VALUES (?,?,?)";

    private static final String GET_ALL_QUERY =   "SELECT * FROM resume ORDER BY full_name,uuid";

    private static final String GET_ALL_CONTACT = "SELECT * FROM contact";

    private static final String GET_ALL_SECTION =  "SELECT * FROM section";

    private static final String SIZE_QUERY =      "SELECT count(*) FROM resume";

    private static final String DELETE_CONTACT =  "DELETE FROM contact WHERE resume_uuid =?";

    private static final String DELETE_SECTION =  "DELETE FROM section WHERE resume_uuid =?";

    private static final String GET_RESUME_QUERY =  "SELECT * FROM resume WHERE uuid =?";

    private static final String GET_CONTACT_QUERY = "SELECT * FROM contact WHERE resume_uuid =?";

    private static final String GET_SECTION_QUERY = "SELECT * FROM section WHERE resume_uuid =?";

    private SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.execute(CLEAR_QUERY);
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement(UPDATE_QUERY)) {
                        ps.setString(1, r.getFullName());
                        ps.setString(2, r.getUuid());
                        if (ps.executeUpdate() == 0) {
                            throw new NotExistStorageException(r.getUuid());
                        }
                    }

                    deleteAttributes(r, conn, DELETE_CONTACT);
                    saveContact(r, conn);

                    deleteAttributes(r, conn, DELETE_SECTION);
                    saveSection(r, conn);
                    return null;
                }
        );
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement(SAVE_QUERY)) {
                        ps.setString(1, r.getUuid());
                        ps.setString(2, r.getFullName());
                        ps.execute();
                    }
                    saveContact(r, conn);
                    saveSection(r, conn);
                    return null;
                }
        );
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.transactionalExecute(conn -> {
            Resume resume;
            try (PreparedStatement ps = conn.prepareStatement(GET_RESUME_QUERY)) {
                ps.setString(1, uuid);
                ResultSet resultSet = ps.executeQuery();
                if (!resultSet.next()) {
                    throw new NotExistStorageException(uuid);
                }
                resume = new Resume(uuid, resultSet.getString(FULL_NAME));
            }

            try (PreparedStatement ps = conn.prepareStatement(GET_CONTACT_QUERY)) {
                ps.setString(1, uuid);
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()){
                    addContact(resume, resultSet);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(GET_SECTION_QUERY)) {
                ps.setString(1, uuid);
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    addSection(resume, resultSet);
                }
            }

            return resume;
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
        Map<String, Resume> resumes = new LinkedHashMap<>();

        sqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement(GET_ALL_QUERY)) {
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            String uuid = rs.getString(UUID);
                            String fullName = rs.getString(FULL_NAME);
                            resumes.put(uuid, new Resume(uuid, fullName));
                        }
                    }

                    try (PreparedStatement ps = conn.prepareStatement(GET_ALL_CONTACT)) {
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            String uuid = rs.getString("resume_uuid");
                            Resume resume = resumes.get(uuid);
                            addContact(resume, rs);
                        }
                    }

                    try (PreparedStatement ps = conn.prepareStatement(GET_ALL_SECTION)) {
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            String uuid = rs.getString("resume_uuid");
                            Resume resume = resumes.get(uuid);
                            addSection(resume, rs);
                        }
                    }
                    return null;
                }
        );
        return new ArrayList<>(resumes.values());
    }

    @Override
    public int size() {
        return sqlHelper.execute(SIZE_QUERY, preparedStatement -> {
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() ? resultSet.getInt(1) : 0;
        });
    }

    private void saveContact(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SAVE_CONTACT_QUERY)) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void addContact(Resume resume, ResultSet resultSet) throws SQLException {
        String value = resultSet.getString("value");
        if (value != null) {
            resume.addContact(ContactType.valueOf(resultSet.getString("type")), value);
        }
    }

    private void saveSection(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SAVE_SECTION_QUERY)) {
            for (Map.Entry<SectionType, Section> e : r.getSections().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                Section section = e.getValue();
                ps.setString(3, JsonParser.write(section, Section.class));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void addSection(Resume resume, ResultSet resultSet) throws SQLException {
        String value = resultSet.getString("value");

        if (value != null) {
            SectionType type = SectionType.valueOf(resultSet.getString("type"));
            resume.addSection(type, JsonParser.read(value, Section.class));
        }
    }

    private void deleteAttributes(Resume r, Connection conn, String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getUuid());
            ps.execute();
        }
    }
}
