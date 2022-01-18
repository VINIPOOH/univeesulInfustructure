package infrastructure.controller;

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

    String doGet(HttpServletRequest request);

    String doPost(HttpServletRequest request);

}
