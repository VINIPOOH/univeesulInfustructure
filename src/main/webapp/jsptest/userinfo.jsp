<%--
  Created by IntelliJ IDEA.
  User: Ivan_Vendelovskyi
  Date: 6/15/2023
  Time: 4:45 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User info</title>
    ${requestScope.user_id}
</head>
<body>
<form id="getUserById" class="form" method="post"
      action="${pageContext.request.contextPath}/userHttp">
    <input type="hidden" name="_method" value="get"/>
        <td>
            <input name="user_id" placeholder="user_id"/>
        </td>
    </tr>
    <tr>
        <td>
            <button class="btn btn-success">Get user by Id</button>
        </td>
    </tr>
</form>
<form id="getUserById" class="form" method="get"
      action="${pageContext.request.contextPath}/userHttp">
    <input type="hidden" name="_method" value="get"/>
    <td>
        <input name="user_id" placeholder="user_id"/>
    </td>
    </tr>
    <tr>
        <td>
            <button class="btn btn-success">Get user by id get-get form</button>
        </td>
    </tr>
</form>
<form id="updateUser" class="form" method="post"
      action="${pageContext.request.contextPath}/userHttp">
    <input type="hidden" name="_method" value="post"/>
    <td>
        <input name="user_id" placeholder="user_id"/>
        <input name="user_name" placeholder="user_name"/>
    </td>
    </tr>
    <tr>
        <td>
            <button class="btn btn-success">Update User Name</button>
        </td>
    </tr>
</form>
<form id="createUSER" class="form" method="post"
      action="${pageContext.request.contextPath}/userHttp">
    <input type="hidden" name="_method" value="put"/>
    <td>
        <input name="user_id" placeholder="user_id"/>
    </td>
    </tr>
    <tr>
        <td>
            <button class="btn btn-success">CreateUser</button>
        </td>
    </tr>
</form>
<form id="delete uesr" class="form" method="post"
      action="${pageContext.request.contextPath}/userHttp">
    <input type="hidden" name="_method" value="delete"/>
    <td>
        <input name="user_id" placeholder="user_id"/>
    </td>
    </tr>
    <tr>
        <td>
            <button class="btn btn-success">delete user</button>
        </td>
    </tr>
</form>

</body>
</html>
