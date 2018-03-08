package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.*;
import java.util.*;

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

                    deleteValue(r, DELETE_CONTACT);
                    saveContact(r, conn);

                    deleteValue(r, DELETE_SECTION);
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
        Map<String, Resume> map = new LinkedHashMap<>();

        sqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement(GET_ALL_QUERY)) {
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            String uuid = rs.getString(UUID);
                            String fullName = rs.getString(FULL_NAME);
                            map.put(uuid, new Resume(uuid, fullName));
                        }
                    }

                    try (PreparedStatement ps = conn.prepareStatement(GET_ALL_CONTACT)) {
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            String uuid = rs.getString("resume_uuid");
                            Resume resume = map.get(uuid);
                            addContact(resume, rs);
                        }
                    }

                    try (PreparedStatement ps = conn.prepareStatement(GET_ALL_SECTION)) {
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            String uuid = rs.getString("resume_uuid");
                            Resume resume = map.get(uuid);
                            addSection(resume, rs);
                        }
                    }
                    return null;
                }
        );
        return new ArrayList<>(map.values());
    }

    @Override
    public int size() {
        return sqlHelper.execute(SIZE_QUERY, preparedStatement -> {
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() ? resultSet.getInt(1) : 0;
        });
    }

    private void addContact(Resume resume, ResultSet resultSet) throws SQLException {
        String value = resultSet.getString("value");
        if (value != null) {
            resume.addContact(ContactType.valueOf(resultSet.getString("type")), value);
        }
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

    private void deleteValue(Resume r, String sql) {
        sqlHelper.execute(sql, ps -> {
            ps.setString(1, r.getUuid());
            ps.execute();
            return null;
        });
    }

    private void saveSection(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SAVE_SECTION_QUERY)) {
            for (Map.Entry<SectionType, Section> e : r.getSections().entrySet()) {
                ps.setString(1, r.getUuid());

                SectionType sectionType = e.getKey();
                ps.setString(2, sectionType.name());

                String value;
                if (sectionType.ordinal() > 1) {
                    value = setSectionValue(e.getValue());
                } else {

                    value = String.valueOf(e.getValue());
                }

                ps.setString(3, value);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void addSection(Resume resume, ResultSet resultSet) throws SQLException {
        SectionType sectionType = SectionType.valueOf(resultSet.getString("type"));

        Section section;
        String value = resultSet.getString("value");
        if (sectionType.ordinal() < 2) {
            section = new TextSection(value);
        } else {
            List<String> list = new ArrayList<>(Arrays.asList(getSectionValues(value)));
            section = new ListSection(list);
        }
        resume.addSection(sectionType, section);
    }

    private String setSectionValue(Section section) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> list = ((ListSection)section).getItems();
        for (String string : list) {
            stringBuilder.append(string);
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    private String[] getSectionValues(String value) {
        return value.split("\n");
    }
}
