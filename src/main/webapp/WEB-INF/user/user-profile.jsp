<%@ include file="../layout/metadata-standart.jsp" %>
<!doctype html>
<html lang="${param.lang}">
<head>
    <%@ include file="../layout/bootstrap.jsp" %>
    <title ><fmt:message key="user.list.page.header"/></title>
</head>
<body>
<%@ include file="../layout/header.jsp" %>
<%@ include file="../layout/userheader.jsp"%>
<%@ include file="../layout/adminHeader.jsp" %>

<div class="container">
    <div class="row">
        <div class="col-md-6">
            <div class="border border-info mt-2 rounded">
                <p><fmt:message key="user.profile.page.paragraph.userMoneyInCents"/></p>
                <p><custom:localise-money moneyInCents="${sessionScope.user.userMoneyInCents}"
                                          lang="${pageContext.response.locale.language}"/></p>
            </div>
        </div>
        <div class="col-md-6 ">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-heading"><fmt:message key="user.profile.page.form.header"/></h3>
                </div>
                <div class="panel-body">
                    <form class="form" method="post" action="${pageContext.request.contextPath}/http/user/user-profile">
                        <c:if test="${inputHasErrors}">
                            <div class="alert alert-danger" role="alert">
                                <p><fmt:message key="user-profile.form.wrong"/></p>
                            </div>
                        </c:if>
                        <div class="form-group">
                            <input type="number" class="form-control" id="money" name="money" >
                        </div>
                        <button class="btn btn-success" type="submit"><fmt:message key="user.profile.page.button.replenish"/></button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
