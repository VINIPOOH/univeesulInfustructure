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
    <c:forEach var="deliveryForUser" items="${requestScope.deliveriesWhichAddressedForUser}">
    <div class="row border border-info mt-2 rounded">
        <div class="card-body col-md-3">
            <h5 class="card-title" ><fmt:message key="user.deliveries.to.get.card.title"/>${deliveryForUser.addresserEmail}
                <fmt:message key="user.deliveries.to.get.card.title.delivery.id"/>${deliveryForUser.deliveryId}</h5>
        </div>
        <div class="col-md-4 mt-3">
            <p1 class="list-group-item"><fmt:message key="user.deliveries.to.get.card.list.from.city"/>${deliveryForUser.localitySandName}</p1>
        </div>
        <div class="col-md-3 mt-3">
        <p1 class="list-group-item"><fmt:message key="user.deliveries.to.get.card.list.to.city"/>${deliveryForUser.localityGetName}</p1>
        </div>
        <div class="card-body col-md-2">
            <form class="form" method="post" action="${pageContext.request.contextPath}/http/user/delivers-to-get">
                <input id="deliveryId" name="deliveryId" value="${deliveryForUser.deliveryId}" type="hidden">
                <button class="btn btn-success" type="submit"><fmt:message key="user.statistic.page.button.confirm.receipt.delivery"/></button>
            </form>
        </div>

    </div>
    </c:forEach>
</div>
</body>
</html>
