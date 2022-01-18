package infrastructure.constant;

/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface PageConstance {
    String REDIRECT_COMMAND = "redirect:";
    String MAIN_WEB_FOLDER = "/WEB-INF/";
    String USER_FOLDER = "user/";
    String ANONYMOUS_FOLDER = "anonymous/";
    String ADMIN_FOLDER = "admin/";

    String USERS_JSP = "users.jsp";
    String REGISTRATION_FILE_NAME = "registration.jsp";
    String LOGIN_FILE_NAME = "login.jsp";
    String INDEX_FILE_NAME = "index.jsp";
    String ERROR_404_FILE_NAME = "404.jsp";
    String COUNTER_FILE_NAME = "counter.jsp";
    String USER_PROFILE_FILE_NAME = "user-profile.jsp";
    String USER_DELIVERY_INITIATION_FILE_NAME = "user-delivery-initiation.jsp";
    String USER_DELIVERY_PAY_FILE_NAME = "user-delivery-pay.jsp";
    String USER_DELIVERY_GET_CONFIRM_FILE_NAME = "user-deliveries-to-get.jsp";
    String USER_STATISTIC_FILE_NAME = "user-statistic.jsp";
String INPUT_GIRVNAS_NOT_RATE = "input_grivnasnotrate";

    String LOGIN_REQUEST_COMMAND = "login";
    String ERROR_404_COMMAND = "404";
    String USER_PROFILE_REQUEST_COMMAND = "user/user-profile";

    String REDIRECT_ON_ERROR_404_STRAIGHT = "/delivery/404";


}
