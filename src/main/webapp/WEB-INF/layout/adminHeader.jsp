    <body th:fragment="userHeader">
        <c:if test="${sessionScope.user.roleType.name()== 'ROLE_ADMIN'}">
            <div class="row border border-secondary">
            <nav class="navbar navbar-default col-md-12",>
            <div class="container-fluid row">
            <div class="col-md-10 row justify-content-start">
            <a class="col-2" href="${pageContext.request.contextPath}/http/admin/users"><fmt:message
                key="layout.adminHeader.users"/></a>
            </div>
            </div>
            </nav>
            </div>
        </c:if>
        </body>
