<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.ListSection" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="ru.javawebinar.basejava.model.OrganizationSection" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <!-- Full name -->
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size=50 value="${resume.fullName}"></dd>
        </dl>
        <!-- Contact section -->
        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type}" size=30 value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>
        <hr>
        <!-- Sections -->
        <c:forEach var="type" items="<%=SectionType.values()%>">
            <c:set var="section" value="${resume.getSection(type)}"/>
            <jsp:useBean id="section" type="ru.javawebinar.basejava.model.Section"/>
            <h2>${type.title}</h2>
            <c:choose>
                <c:when test="${type == 'OBJECTIVE'}">
                    <!-- Objective section -->
                    <input type="text" name="${type}" size=77 value='<%=section%>'>
                </c:when>

                <c:when test="${type == 'PERSONAL'}">
                    <!-- Personal section -->
                    <textarea name='${type}' cols=75 rows=5><%=section%></textarea>
                </c:when>

                <c:when test="${type == 'ACHIEVEMENT' || type == 'QUALIFICATIONS'}">
                    <!-- List section -->
                    <textarea name='${type}' cols=75 rows=5><%=String.join("\n", ((ListSection) section).getItems())%></textarea>
                </c:when>

                <c:when test="${type == 'EXPERIENCE' || type == 'EDUCATION'}">
                    <!-- Organizations section -->
                    <c:forEach var="organization" items="<%=((OrganizationSection) section).getOrganizations()%>"
                               varStatus="counter">

                        <!-- Organization title -->
                        <dl>
                            <dt>Организация</dt>
                            <dd><input type="text" name="${type}" size=47 value="${organization.homePage.name}"></dd>
                        </dl>

                        <!-- Organization url -->
                        <dl>
                            <dt>Сайт</dt>
                            <dd><input type="text" name="${type}_url" size=47 value="${organization.homePage.url}"></dd>
                        </dl>

                        <!-- Position section -->
                        <c:forEach var="position" items="${organization.positions}">
                            <jsp:useBean id="position" type="ru.javawebinar.basejava.model.Organization.Position"/>
                            <!-- Position start date -->
                            <dl>
                                <dt>Начало работы</dt>
                                <dd><input type="text" name="${type}${counter.index}_startDate" size=47 value="${position.startDate}"></dd>
                            </dl>

                            <!-- Position end date -->
                            <dl>
                                <dt>Окончание</dt>
                                <dd><input type="text" name="${type}${counter.index}_endDate" size=47 value="${position.endDate}"></dd>
                            </dl>

                            <!-- Position title -->
                            <dl>
                                <dt>Должность</dt>
                                <dd><input type="text" name="${type}${counter.index}_position_title" size=47 value="${position.title}"></dd>
                            </dl>

                            <!-- Position description -->
                            <dl>
                                <dt>Описание</dt>
                                <dd><textarea name='${type}${counter.index}_description' cols=45 rows=5>${position.description}</textarea></dd>
                            </dl>
                        </c:forEach>
                    </c:forEach>
                </c:when>
            </c:choose>
        </c:forEach>

        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>