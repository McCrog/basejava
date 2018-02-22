package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<ContactType, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            if (contacts.size() > 0) {
                for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                    dos.writeUTF(entry.getKey().name());
                    dos.writeUTF(entry.getValue());
                }
            }

            // Completed implements sections

            Map<SectionType, Section> sections = r.getSections();
            int sectionsSize = sections.size();
            dos.writeInt(sectionsSize);

            if (sectionsSize > 0) {
                for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
                    SectionType sectionType = entry.getKey();
                    Section section = entry.getValue();

                    dos.writeUTF(sectionType.name());
                    switch (sectionType) {
                        case OBJECTIVE:
                        case PERSONAL:
                            dos.writeUTF(((TextSection) section).getContent());
                            break;
                        case ACHIEVEMENT:
                        case QUALIFICATIONS:
                            writeListSection(dos, (ListSection) section);
                            break;
                        case EXPERIENCE:
                        case EDUCATION:
                            writeOrganizationSection(dos, (OrganizationSection) section);
                            break;
                    }
                }
            }
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        Resume resume = null;
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            resume = new Resume(uuid, fullName);
            int contactsSize = dis.readInt();
            for (int i = 0; i < contactsSize; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }
            // Completed implements sections

            int sectionsSize = dis.readInt();

            if (sectionsSize > 0) {
                for (int i = 0; i < sectionsSize; i++) {
                    SectionType sectionType = SectionType.valueOf(dis.readUTF());

                    switch (sectionType) {
                        case OBJECTIVE:
                        case PERSONAL:
                            resume.addSection(sectionType, readTextSection(dis));
                            break;
                        case ACHIEVEMENT:
                        case QUALIFICATIONS:
                            resume.addSection(sectionType, readListSection(dis));
                            break;
                        case EXPERIENCE:
                        case EDUCATION:
                            resume.addSection(sectionType, readOrganizationSection(dis));
                            break;
                    }
                }
            }
        } catch (Exception e) {
            throw new StorageException("Data stream read error", e);
        }
        return resume;
    }

    private Section readTextSection(DataInputStream dis) throws IOException {
        return new TextSection(dis.readUTF());
    }

    private Section readListSection(DataInputStream dis) throws IOException {
        List<String> list = new ArrayList<>();

        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            list.add(dis.readUTF());
        }

        return new ListSection(list);
    }

    private Section readOrganizationSection(DataInputStream dis) throws IOException {
        List<Organization> organizations = new ArrayList<>();
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            String name = dis.readUTF();
            String url = dis.readUTF();

            Link link = new Link(name, url);

            List<Organization.Position> positions = new ArrayList<>();
            int positionSize = dis.readInt();
            for (int j = 0; j < positionSize; j++) {
                LocalDate startDate = LocalDate.parse(dis.readUTF());
                LocalDate endDate = LocalDate.parse(dis.readUTF());
                String title = dis.readUTF();
                String description = dis.readUTF();
                positions.add(new Organization.Position(startDate, endDate, title, description));
            }

            organizations.add(new Organization(link, positions));
        }
        return new OrganizationSection(organizations);
    }

    private void writeListSection(DataOutputStream dos, ListSection listSection) throws IOException {
        List<String> list = listSection.getItems();
        int size = list.size();
        dos.writeInt(size);
        for (String item : list) {
            dos.writeUTF(item);
        }
    }

    private void writeOrganizationSection(DataOutputStream dos, OrganizationSection organizationSection) throws IOException {
        List<Organization> organizations = organizationSection.getOrganizations();
        int size = organizations.size();
        dos.writeInt(size);
        for (Organization organization : organizations) {
            Link link = organization.getHomePage();
            dos.writeUTF(link.getName());
            dos.writeUTF(link.getUrl());

            List<Organization.Position> positions = organization.getPositions();
            dos.writeInt(positions.size());
            for (Organization.Position position : positions) {
                dos.writeUTF(position.getStartDate().toString());
                dos.writeUTF(position.getEndDate().toString());
                dos.writeUTF(position.getTitle());
                dos.writeUTF(position.getDescription());
            }
        }
    }
}
