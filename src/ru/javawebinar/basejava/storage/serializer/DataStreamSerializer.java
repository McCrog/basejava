package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }
            // TODO implements sections
            Map<SectionType, Section> sections = r.getSections();
            dos.writeInt(sections.size());
            for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue().getClass().getName());
                dos.writeUTF(entry.getValue().toString());
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
            // TODO implements sections

            int sectionsSize = dis.readInt();
            for (int i = 0; i < sectionsSize; i++) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                Section section = deserialize(dis.readUTF(), dis.readUTF());
                resume.addSection(sectionType, section);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resume;
    }

    public Section deserialize(String className, String data) throws ClassNotFoundException, IllegalAccessException,
            InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?> clazz = Class.forName(className);
        Constructor<?> ctor;

        switch (clazz.getSimpleName()) {
            case "TextSetion": {
                ctor = clazz.getConstructor(String.class);
                return (Section) ctor.newInstance(data);
            }
            case "ListSection": {
                ctor = clazz.getConstructor(List.class);
                String[] dataArray = data.split(",");
                return (Section) ctor.newInstance(Arrays.asList(dataArray));
            }
            case "OrganizationSection": {
                ctor = clazz.getConstructor(List.class);
                String[] linkObject = data.substring(data.indexOf("Link(") + 5, data.indexOf("),[")).split(",");
                Link link = new Link(linkObject[0], linkObject[1]);

                List<Organization.Position> positions = new ArrayList<>();
                String[] positionObject = data.substring(data.indexOf("Position(") + 9, data.indexOf(")]")).split(",");
                LocalDate startDate = LocalDate.parse(positionObject[0]);
                LocalDate endDate = LocalDate.parse(positionObject[1]);
                String title = positionObject[2];
                String description = positionObject[3];
                positions.add(new Organization.Position(startDate, endDate, title, description));

                Organization organization = new Organization(link, positions);
                List<Organization> organizations = new ArrayList<>();
                organizations.add(organization);

                return (Section) ctor.newInstance(organizations);
            }
            default:
                return null;
        }
    }
}
