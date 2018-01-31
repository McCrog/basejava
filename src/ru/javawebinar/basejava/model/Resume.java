package ru.javawebinar.basejava.model;

import java.util.Objects;
import java.util.UUID;

import static ru.javawebinar.basejava.model.SectionType.*;

/**
 * com.urise.webapp.model.ru.javawebinar.basejava.model.Resume class
 */
public class Resume implements Comparable<Resume> {

    // Unique identifier
    private final String uuid;
    private final String fullName;

    private ListSection contacts;
    private TextSection personal;
    private TextSection objective;
    private ListSection achievement;
    private ListSection qualifications;
    private BlockSection experience;
    private BlockSection education;

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "uuid must not be null");
        Objects.requireNonNull(fullName, "fullName must not be null");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setNewSection(SectionType sectionType) {
        switch (sectionType) {
            case CONTACT:
                if (contacts == null)
                    contacts = new ListSection(CONTACT);
                break;
            case PERSONAL:
                if (personal == null)
                    personal = new TextSection(PERSONAL);
                break;
            case OBJECTIVE:
                if (objective == null)
                    objective = new TextSection(OBJECTIVE);
                break;
            case ACHIEVEMENT:
                if (achievement == null)
                    achievement = new ListSection(ACHIEVEMENT);
                break;
            case QUALIFICATIONS:
                if (qualifications == null)
                    qualifications = new ListSection(QUALIFICATIONS);
                break;
            case EXPERIENCE:
                if (experience == null)
                    experience = new BlockSection(EXPERIENCE);
                break;
            case EDUCATION:
                if (education == null)
                    education = new BlockSection(EDUCATION);
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public void removeSection(SectionType sectionType) {
        switch (sectionType) {
            case CONTACT:
                if (contacts != null)
                    contacts = null;
                break;
            case PERSONAL:
                if (personal != null)
                    personal = null;
                break;
            case OBJECTIVE:
                if (objective != null)
                    objective = null;
                break;
            case ACHIEVEMENT:
                if (achievement != null)
                    achievement = null;
                break;
            case QUALIFICATIONS:
                if (qualifications != null)
                    qualifications = null;
                break;
            case EXPERIENCE:
                if (experience != null)
                    experience = null;
                break;
            case EDUCATION:
                if (education != null)
                    education = null;
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public ListSection getContacts() {
        return contacts;
    }

    public TextSection getPersonal() {
        return personal;
    }

    public TextSection getObjective() {
        return objective;
    }

    public ListSection getAchievement() {
        return achievement;
    }

    public ListSection getQualifications() {
        return qualifications;
    }

    public BlockSection getExperience() {
        return experience;
    }

    public BlockSection getEducation() {
        return education;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;

        return uuid.equals(resume.uuid) && fullName.equals(resume.fullName);
    }

    @Override
    public int hashCode() {
        int result = uuid.hashCode();
        result = 31 * result + fullName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return uuid + '(' + fullName + ')' + "\n"
                + "contacts         " + contacts + "\n"
                + "personal         " + personal + "\n"
                + "objective        " + objective + "\n"
                + "achievemen       " + achievement + "\n"
                + "qualifications   " + qualifications + "\n"
                + "experience       " + experience + "\n"
                + "education        " + education;
    }

    @Override
    public int compareTo(Resume o) {
        int cmp = fullName.compareTo(o.fullName);
        return cmp != 0 ? cmp : uuid.compareTo(o.uuid);
    }
}
