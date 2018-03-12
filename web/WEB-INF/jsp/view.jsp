<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ru.javawebinar.basejava.model.ListSection" %>
<%@ page import="ru.javawebinar.basejava.util.HtmlUtil" %>
<%@ page import="ru.javawebinar.basejava.model.OrganizationSection" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png" alt="Edit"></a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<ru.javawebinar.basejava.model.ContactType, java.lang.String>"/>
            <%=HtmlUtil.contactToHtml(contactEntry.getKey(), contactEntry.getValue())%><br/>
        </c:forEach>
    <p>
    <hr>
    <p>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<ru.javawebinar.basejava.model.SectionType, ru.javawebinar.basejava.model.Section>"/>
            <h2><%=sectionEntry.getKey().getTitle()%></h2>

            <c:choose>
                <c:when test="${sectionEntry.getKey() == 'OBJECTIVE'}">
                    <h3>${sectionEntry.getValue()}</h3>
                </c:when>

                <c:when test="${sectionEntry.getKey() == 'PERSONAL'}">
                    <p>${sectionEntry.getValue()}</p>
                </c:when>

                <c:when test="${sectionEntry.getKey() == 'ACHIEVEMENT' || sectionEntry.getKey() == 'QUALIFICATIONS'}">
                    <ul>
                    <c:forEach var="list" items="<%=((ListSection) sectionEntry.getValue()).getItems()%>">
                        <li>${list}</li>
                    </c:forEach>
                    </ul>
                </c:when>

                <c:when test="${sectionEntry.getKey() == 'EXPERIENCE' || sectionEntry.getKey() == 'EDUCATION'}">
                    <c:forEach var="organization" items="<%=((OrganizationSection) sectionEntry.getValue()).getOrganizations()%>">

                        <c:choose>
                            <c:when test="${empty organization.homePage.url}">
                                <h3>${organization.homePage.name}</h3>
                            </c:when>
                            <c:otherwise>
                                <h3><a href="${organization.homePage.url}">${organization.homePage.name}</a></h3>
                            </c:otherwise>
                        </c:choose>

                        <table>
                        <c:forEach var="position" items="${organization.positions}">
                            <jsp:useBean id="position" type="ru.javawebinar.basejava.model.Organization.Position"/>
                            <tr style="vertical-align: top">
                                <td><%=DateUtil.toHtml(position.getStartDate(), position.getEndDate())%></td>
                                <td><b>${position.title}</b><br/>${position.description}</td>
                            </tr>
                        </c:forEach>
                        </table>
                    </c:forEach>
                </c:when>
            </c:choose>
        </c:forEach>
    <p>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
