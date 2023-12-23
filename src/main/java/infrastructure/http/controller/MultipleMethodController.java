package infrastructure.http.controller;

import javax.servlet.http.HttpServletRequest;

/**
 * Declare interface http get and post controllers.
 * If you need add own controller by manage {@link infrastructure.ApplicationContext}
 * and oblige {@link web.DispatcherServlet} use it in a team for process http requests.
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface MultipleMethodController {

    default String doGet(HttpServletRequest request){
        return null;
    }

    default String doPost(HttpServletRequest request){
        return null;
    }

    default String doPut(HttpServletRequest request){
        return null;
    }

    default String doDelete(HttpServletRequest request){
        return null;
    }

}
