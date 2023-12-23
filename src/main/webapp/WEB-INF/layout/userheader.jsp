    <body th:fragment="userHeader">
        <div class="row border border-secondary">
        <nav class="navbar navbar-default col-md-12",>
        <div class="container-fluid row">
        <div class="col-md-10 row justify-content-start">


        <a class="col-2" href="${pageContext.request.contextPath}/http/user/delivers-to-get" ><fmt:message key="layout.user.header.not.gotten.delivers"/></a>
        <a class="col-1" href="${pageContext.request.contextPath}/http/user/user-profile" ><fmt:message key="layout.user.header.balance"/></a>
        <a class="col-2" href="${pageContext.request.contextPath}/http/user/user-delivery-initiation" ><fmt:message key="layout.user.header.delivery.initiation"/></a>
        <a class="col-2" href="${pageContext.request.contextPath}/http/user/user-delivery-pay" ><fmt:message key="layout.user.header.delivery.pay"/></a>
            <a class="col-2" href="${pageContext.request.contextPath}/http/user/user-statistic?page=1&size=10" ><fmt:message
            key="layout.user.header.user.statistic"/></a>
        </div>
        </div>
        </nav>
        </div>
        </body>
