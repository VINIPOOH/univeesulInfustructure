<%@ include file="../layout/metadata-standart.jsp" %>
<!doctype html>
<html lang="${param.lang}">
<head>
    <%@ include file="../layout/bootstrap.jsp" %>
    <div th:include="~{layout/header.html::head_base_data}"></div>
    <title ><fmt:message key="title.login.page"/></title>
</head>
<body>
<%@ include file="../layout/header.jsp" %>
<div class="container">
    <div class="row">
        <div class="col-md-6 col-md-offset-3">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-heading"><fmt:message key="login.page.form.header"/></h3>
                </div>
                <div class="panel-body">
                    <form class="form" method="post" action="${pageContext.request.contextPath}/http/anonymous/login">
                        <c:if test="${inputHasErrors}">
                        <div class="alert alert-danger" role="alert">
                            <p><fmt:message key="login.page.form.wrong"/></p>
                        </div>
                        </c:if>
                        <div class="form-group">
                            <input type="text" class="form-control" id="username" name="username"
                                   placeholder=<fmt:message key="login.page.form.label.email"/>>
                        </div>
                        <div class="form-group">
                            <input type="password" class="form-control" id="password" name="password"
                                   placeholder=<fmt:message key="login.page.form.label.password"/>>
                        </div>
                        <button class="btn btn-success" type="submit"><fmt:message key="login.page.button.login"/></button>
                        <a class="btn"  align="left" href="${pageContext.request.contextPath}/http/anonymous/registration"><fmt:message key="login.page.button.gotoRegistration"/></a>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
