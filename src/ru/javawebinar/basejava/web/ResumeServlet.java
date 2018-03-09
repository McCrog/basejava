package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.SectionType;
import ru.javawebinar.basejava.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

public class ResumeServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(ResumeServlet.class.getName());
    private Storage storage = Config.get().getStorage();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String name = request.getParameter("name");
        PrintWriter writer = response.getWriter();

        List<Resume> resumes = storage.getAllSorted();

        writer.print("" +
                "<html>" +
                    "<head>" +
                        "<title>Мои резюме</title>" +
                        "<meta charset=\"UTF-8\">" +
                        "<link rel=\"stylesheet\" href=\"css/style.css\">" +
                    "</head>" +
                    "<body>" +
                        "<header>Разработка Web приложения \"База данных резюме\". <a href=\"http://javaops.ru/reg/basejava/\" target=\"_blank\">Basejava</a></header>" +
                        "<p>" + (name == null ? "Hello Resumes!" : "Hello " + name + "!") + "</p>" +
                        "<center>" +
                            "<table>" +
                                "<tr>" +
                                    "<th>UUID</th>" +
                                    "<th>Name</th>" +
                                    "<th>Contact</th>" +
                                    "<th>Objective</th>" +
                                "</tr>");

        for (Resume r : resumes) {
            writer.print("" +
                                "<tr>" +
                                    "<td>" + r.getUuid() + "</td>" +
                                    "<td><a href=?name=" + r.getFullName() + ">" + r.getFullName() + "</a></td>" +
                                    "<td>" + r.getContact(ContactType.EMAIL) + "</td>" +
                                    "<td>" + r.getSection(SectionType.OBJECTIVE) + "</td>" +
                                "</tr>");
        }

        writer.print("" +
                            "</table>" +
                        "</center>" +
                        "<footer>Разработка Web приложения \"База данных резюме\". <a href=\"http://javaops.ru/reg/basejava/\" target=\"_blank\">Basejava</a></footer>" +
                    "</body>" +
                "</html>");
    }
}
