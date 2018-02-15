package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.util.List;

public class Organization {
    private final Link homePage;
    private List<OrganizationPosition> positions;

    public Organization(String name, String url, List<OrganizationPosition> positions) {
        this.homePage = new Link(name, url);
        this.positions = positions;
    }

    public List<OrganizationPosition> getPositions() {
        return positions;
    }

    public void addPosition(LocalDate startDate, LocalDate endDate, String title, String description) {
        positions.add(new OrganizationPosition(startDate, endDate, title, description));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        return homePage.equals(that.homePage) && positions.equals(that.positions);
    }

    @Override
    public int hashCode() {
        int result = homePage.hashCode();
        result = 31 * result + positions.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "homePage=" + homePage +
                ", positions=" + positions +
                '}';
    }
}
