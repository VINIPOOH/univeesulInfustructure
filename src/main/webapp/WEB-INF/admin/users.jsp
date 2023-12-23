<%@ include file="../layout/metadata-standart.jsp" %>
<!doctype html>
<html lang="${param.lang}">
<head>
    <%@ include file="../layout/bootstrap.jsp" %>
    <title ><fmt:message key="homepage.title"/></title>
</head>
<body>
<%@ include file="../layout/header.jsp" %>
<%@ include file="../layout/userheader.jsp" %>
<%@ include file="../layout/adminHeader.jsp" %>
<table class="table" style="
        width: 40%;
        margin: 0 auto;
        background:  #0c5460;
        border-radius: 10px;
        color: white;
        ">
    <thead>
    <tr>
        <th><fmt:message key="user.list.page.password.colum.name"/></th>
        <th><fmt:message key="user.list.page.email.colum.name"/></th>
        <th><fmt:message key="user.list.page.role.colum.name"/></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="user" items="${requestScope.usersList}">
        <tr>
            <td><span>${user.password}</span></td>
            <td><span>${user.email}</span></td>
            <td><span>${user.roleType}</span></td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
