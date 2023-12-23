    <div class="row border border-green">
        <nav class="navbar navbar-default col-md-8">
        <div class="container-fluid">
        <div class="navbar-header col-md-2">
        <a href="${pageContext.request.contextPath}/http/counter" class="navbar-brand"><fmt:message key="layout.header.logo"/></a>
        </div>
        <div class="col-md-10 row justify-content-start">
        <a class="col-2" href="${pageContext.request.contextPath}/http/counter" >
        <fmt:message key="layout.header.homepage"/>
        </a>
        <a class="col-2" href="${pageContext.request.contextPath}/http/user/user-profile"><fmt:message
            key="layout.user.header.cabinet"/></a>
        </div>
        </div>
        </nav>
        <div class="col-md-4">
        <div><a class="btn" href="?lang=en" ><fmt:message key="button.en.text"/></a>
<%--        <a class="btn" href="?lang=ru"><fmt:message key="button.ru.text"/></a>--%>
        </div>
        <c:if test="${sessionScope.user != null}">
            <div class="row">
            <div class="col-md-6">
            <label>${sessionScope.user.email}</label>
            </div>
            <div class="col-md-6">
            <a class="btn border" href="${pageContext.request.contextPath}/http/user/logout" ><fmt:message
                key="layout.header.link.logout"/></a>
            </div>
            </div>
        </c:if>
        <c:if test="${sessionScope.user==null}">
            <div class="row">
            <a class="btn btn-success" href="${pageContext.request.contextPath}/http/anonymous/login" type="submit" ><fmt:message
                key="login.page.button.login"/></a>
            <a class="btn" align="left" href="${pageContext.request.contextPath}/http/anonymous/registration" ><fmt:message
                key="login.page.button.gotoRegistration"/></a>
            </div>
        </c:if>
        </div>
        </div>
