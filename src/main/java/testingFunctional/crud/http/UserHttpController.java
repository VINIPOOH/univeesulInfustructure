package testingFunctional.crud.http;

import infrastructure.anotation.HttpEndpoint;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.http.controller.MultipleMethodController;
import testingFunctional.crud.services.UserService;

import javax.servlet.http.HttpServletRequest;

@HttpEndpoint(value = "/http/userHttp")
@NeedConfig
public class UserHttpController implements MultipleMethodController {

    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    @InjectByType
    UserService userService;

    @Override
    public String doGet(HttpServletRequest request) {

        String parameter = request.getParameter(USER_ID);
        if (parameter != null){
            request.setAttribute(USER_ID, userService.getUserById(Integer.parseInt(parameter)));
        } else {
            request.setAttribute(USER_ID, userService.getAllUsers());
        }
        return "/jsptest/userinfo.jsp";
    }

    @Override
    public String doPut(HttpServletRequest request) {
        request.setAttribute(USER_ID, userService.createUser(Integer.parseInt(request.getParameter(USER_ID))));
        return "redirect:/http/userHttp";
    }

    @Override
    public String doPost(HttpServletRequest request) {
        request.setAttribute(USER_ID, userService.updateUserName
                (Integer.parseInt(request.getParameter(USER_ID)), request.getParameter(USER_NAME)));
        return "redirect:/http/userHttp";
    }

    @Override
    public String doDelete(HttpServletRequest request) {
        request.setAttribute(USER_ID, userService.deleteUserById(Integer.parseInt(request.getParameter(USER_ID))));
        return "redirect:/http/userHttp";
    }
}
