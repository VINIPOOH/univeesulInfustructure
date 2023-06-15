package testingFunctional.crud.http;

import infrastructure.anotation.HttpEndpoint;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.http.controller.MultipleMethodController;
import testingFunctional.crud.services.UserService;

import javax.servlet.http.HttpServletRequest;

@HttpEndpoint(value = "/userHttp")
@NeedConfig
public class UserController implements MultipleMethodController {

    public static final String USER_ID = "userid";
    @InjectByType
    UserService userService;

    @Override
    public String doGet(HttpServletRequest request) {

        request.setAttribute("userId", userService.getUserById(Integer.parseInt(request.getParameter(USER_ID))));
        return "/jsptest/userinfo.jsp";
    }

    @Override
    public String doPost(HttpServletRequest request) {
        return null;
    }

    @Override
    public String doPut(HttpServletRequest request) {
        return null;
    }

    @Override
    public String doDelete(HttpServletRequest request) {
        return null;
    }
}
