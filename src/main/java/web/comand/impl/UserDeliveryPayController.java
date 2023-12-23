package web.comand.impl;

import dal.entity.User;
import dto.validation.IDValidator;
import infrastructure.anotation.HttpEndpoint;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import logiclayer.exeption.OperationFailException;
import logiclayer.service.BillService;
import logiclayer.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import infrastructure.http.controller.MultipleMethodController;
import web.exception.OnClientSideProblemException;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static web.constant.AttributeConstants.SESSION_LANG;
import static web.constant.AttributeConstants.SESSION_USER;
import static web.constant.PageConstance.*;

/**
 * Process "user/user-delivery-pay" page requests
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton
@NeedConfig
@HttpEndpoint("/http/user/user-delivery-pay")
public class UserDeliveryPayController implements MultipleMethodController {
    private static final String BILL_INFO_TO_PAY = "BillInfoToPay";
    private static final String ID = "Id";
    private static final Logger log = LogManager.getLogger(UserDeliveryPayController.class);
    private static final String NOT_ENOUGH_MONEY = "notEnoughMoney";
    @InjectByType
    private BillService billService;
    @InjectByType
    private UserService userService;
    @InjectByType
    private IDValidator idValidator;

    @Override
    public String doGet(HttpServletRequest request) {
        log.debug("");

        request.setAttribute(BILL_INFO_TO_PAY, billService.getInfoToPayBillsByUserID(((User) request.getSession().getAttribute(SESSION_USER)).getId(), (Locale) request.getSession().getAttribute(SESSION_LANG)));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_PAY_FILE_NAME;
    }

    @Override
    public String doPost(HttpServletRequest request) {
        log.debug("");
        if (!idValidator.isValid(request, ID)) {
            log.error("id is not valid client is broken");
            throw new OnClientSideProblemException();
        }
        User sessionUser = (User) request.getSession().getAttribute(SESSION_USER);
        try {
            billService.payForDelivery(sessionUser.getId(), Long.parseLong(request.getParameter(ID)));
            sessionUser.setUserMoneyInCents(userService.getUserBalance(sessionUser.getId()));
        } catch (OperationFailException e) {
            request.setAttribute(NOT_ENOUGH_MONEY, true);
        }
        request.setAttribute(BILL_INFO_TO_PAY, billService.getInfoToPayBillsByUserID(((User) request.getSession().getAttribute(SESSION_USER)).getId(), (Locale) request.getSession().getAttribute(SESSION_LANG)));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_PAY_FILE_NAME;
    }
}
