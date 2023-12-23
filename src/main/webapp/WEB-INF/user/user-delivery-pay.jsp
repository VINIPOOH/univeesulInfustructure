<%@ include file="../layout/metadata-standart.jsp" %>
<!doctype html>
<html lang="${param.lang}">
<head>
    <%@ include file="../layout/bootstrap.jsp" %>
    <title><fmt:message key="user.statistic.page.title"/></title>
</head>
<body>
<%@ include file="../layout/header.jsp" %>
<%@ include file="../layout/userheader.jsp" %>
<%@ include file="../layout/adminHeader.jsp" %>

<div name="deliveries-which-wait-getting" class="container">
    <c:forEach var="billToPay" items="${requestScope.BillInfoToPay}">
        <div class="row border border-info mt-2 rounded">
            <div class="card-body col-md-3">
                <h5 class="card-title"><fmt:message
                        key="user-delivery-request-confirm.card.title"/>${billToPay.addreeseeEmail}</h5>
            </div>
            <div class="col-md-4 mt-3">
                <p1 class="list-group-item"><fmt:message
                        key="user-delivery-request-confirm.card.list.from.city"/>${billToPay.localitySandName}</p1>
            </div>
            <div class="col-md-3 mt-3">
                <p1 class="list-group-item"><fmt:message
                        key="user-delivery-request-confirm.card.list.to.city"/>${billToPay.localityGetName}</p1>
            </div>
            <div class="col-md-3 mt-3">
                <p1 class="list-group-item"><fmt:message
                        key="user-delivery-request-confirm.card.time.on.delivery"/>${billToPay.weight}</p1>
            </div>
            <div class="col-md-3 mt-3">
                <p1 class="list-group-item"><fmt:message
                        key="user-delivery-request-confirm.card.price.on.delivery"/>
                    <fmt:message key="user-statistic.price"/>
                    <custom:localise-money moneyInCents="${billToPay.price}"
                                           lang="${pageContext.response.locale.language}"/></p1>
            </div>

            <div class="card-body col-md-2">
                <form class="form" method="post"
                      action="${pageContext.request.contextPath}/http/user/user-delivery-pay">
                    <c:if test="${notEnoughMoney}">
                        <div class="alert alert-danger" role="alert">
                            <p><fmt:message key="user-delivery-request-confirm.form.wrong.not.enough.money"/></p>
                        </div>
                    </c:if>
                    <div class="form-group">
                        <input id="Id" name="Id" value="${billToPay.billId}" type="hidden">
                        <button class="btn btn-success" type="submit"><fmt:message
                                key="user-delivery-request-confirm.button.text"/></button>
                    </div>
                </form>
            </div>
        </div>
    </c:forEach>
</div>
</body>
</html>
