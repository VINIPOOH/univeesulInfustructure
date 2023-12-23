        <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


        <c:if test="${not empty param.lang}">
            <fmt:setLocale value="${param.lang}" scope="session"/>
        </c:if>
        <fmt:setBundle basename="page"/>
        <html>
        <head>
        <title>Pagination</title>
        </head>
        <body>
        <nav aria-label="Navigation for countries">
        <ul class="pagination">
        <c:if test="${page ne 1}">
            <li class="page-item"><a class="page-link"
            href="${pageContext.request.contextPath}/http/${command}?page=${page - 1}&size=${size}">
            <fmt:message key="pagination.prev"/>
            <<<
            </a>
            </li>
        </c:if>

        <c:if test="${numberOfPages ne 1}">
            <c:forEach begin="1" end="${numberOfPages}" var="i">
                <c:choose>
                    <c:when test="${page eq i}">
                        <li class="page-item active"><a class="page-link">
                        ${i} <span class="sr-only">(current)</span></a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item"><a class="page-link"
                        href="${pageContext.request.contextPath}/http/${command}?page=${i}&size=${size}">${i}</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </c:if>

        <c:if test="${page lt numberOfPages}">
            <li class="page-item"><a class="page-link"
            href="${pageContext.request.contextPath}/http/${command}?page=${page + 1}&size=${size}">
            <fmt:message key="pagination.next"/>
            >>>
            </a>
            </li>
        </c:if>
        </ul>
        </nav>
        </body>
        </html>
