package web.comand.impl;

import dal.entity.User;
import infrastructure.anotation.HttpEndpoint;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import logiclayer.service.BillService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import infrastructure.http.controller.MultipleMethodController;
import web.util.Pagination;

import javax.servlet.http.HttpServletRequest;

import static web.constant.AttributeConstants.SESSION_USER;
import static web.constant.PageConstance.*;

/**
 * Process user/user-statistic page requests
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton
@NeedConfig
@HttpEndpoint("/http/user/user-statistic")
public class UserStatisticController implements MultipleMethodController {

    private static final String USER_USER_STATISTIC = "user/user-statistic";
    private static final int PAGE_ATTRIBUTE = 1;
    private static final int PAGE_SIZE = 10;
    private static final String BILLS_LIST = "billsList";
    private static final Logger log = LogManager.getLogger(UserStatisticController.class);
    @InjectByType
    private Pagination pagination;
    @InjectByType
    private BillService billService;

    @Override
    public String doGet(HttpServletRequest request) {
        log.debug("");

        int pageAtribute = PAGE_ATTRIBUTE;
        int pageSize = PAGE_SIZE;
        if (pagination.validate(request)) {
            pageAtribute = Integer.parseInt(request.getParameter("page"));
            pageSize = Integer.parseInt(request.getParameter("size"));
        }
        int offset = (pageAtribute - 1) * pageSize;

        pagination.paginate(pageAtribute, pageSize, billService.countAllBillsByUserId(((User) request.getSession().getAttribute(SESSION_USER)).getId()), request, USER_USER_STATISTIC);
        request.setAttribute(BILLS_LIST, billService.getBillHistoryByUserId(((User) request.getSession().getAttribute(SESSION_USER)).getId(), offset, pageSize));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_STATISTIC_FILE_NAME;
    }

    @Override
    public String doPost(HttpServletRequest request) {
        return null;
    }
}
