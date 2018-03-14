package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class ResumeServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(ResumeServlet.class.getName());

    private Storage storage; // = Config.get().getStorage();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");

        final boolean isCreate = (uuid == null || uuid.length() == 0);
        Resume r;
        if (isCreate) {
            r = new Resume(fullName);
        } else {
            r = storage.get(uuid);
            r.setFullName(fullName);
        }

        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                r.addContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }

        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());

            if (value == null && value.trim().length() == 0) {
                r.getSections().remove(type);
            } else {
                switch (type) {
                    case OBJECTIVE:
                    case PERSONAL:
                        r.addSection(type, new TextSection(value));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        r.addSection(type, new ListSection(Arrays.asList(value.split("\r\n"))));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        String[] values = request.getParameterValues(type.name());
                        String[] url = request.getParameterValues(type.name() + "_url");
                        List<Organization> organizations = new ArrayList<>();

                        for (int i = 0; i < values.length; i++) {
                            List<Organization.Position> positions = new ArrayList<>();

                            String title = values[i];
                            String prefix = type.name() + i;

                            String[] startDate = request.getParameterValues(prefix + "_startDate");
                            String[] endDate = request.getParameterValues(prefix + "_endDate");

                            String[] positionTitle = request.getParameterValues(prefix + "_position_title");
                            String[] description = request.getParameterValues(prefix + "_description");

                            for (int j = 0; j < positionTitle.length; j++) {
                                positions.add(new Organization.Position(LocalDate.parse(startDate[j]), LocalDate.parse(endDate[j]), positionTitle[j], description[j]));
                            }

                            organizations.add(new Organization(new Link(title, url[i]), positions));
                        }

                        r.addSection(type, new OrganizationSection(organizations));
                        break;
                }
            }
        }

        if (isCreate) {
            storage.save(r);
        } else {
            storage.update(r);
        }
        response.sendRedirect("resume");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }

        Resume r;

        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
            case "edit":
                r = storage.get(uuid);
                break;
            case "add":
                r = Resume.EMPTY;
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");

        }

        request.setAttribute("resume", r);
        request.getRequestDispatcher(("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp"))
                .forward(request, response);
    }
}
