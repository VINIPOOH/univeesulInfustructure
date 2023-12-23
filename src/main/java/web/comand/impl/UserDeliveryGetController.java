package web.comand.impl;

import dal.entity.User;
import dto.validation.IDValidator;
import infrastructure.anotation.HttpEndpoint;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import logiclayer.service.DeliveryService;
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
 * Process "user/delivers-to-get" page requests
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton
@NeedConfig
@HttpEndpoint("/http/user/delivers-to-get")
public class UserDeliveryGetController implements MultipleMethodController {
    private static final String DELIVERIES_WHICH_ADDRESSED_FOR_USER = "deliveriesWhichAddressedForUser";
    private static final String DELIVERY_ID = "deliveryId";
    private static final Logger log = LogManager.getLogger(UserDeliveryGetController.class);

    @InjectByType
    private IDValidator idValidator;
    @InjectByType
    private DeliveryService deliveryService;

    @Override
    public String doGet(HttpServletRequest request) {
        log.debug("");

        request.setAttribute(DELIVERIES_WHICH_ADDRESSED_FOR_USER, deliveryService.getInfoToGetDeliveriesByUserID(((User) request.getSession().getAttribute(SESSION_USER)).getId(), (Locale) request.getSession().getAttribute(SESSION_LANG)));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_GET_CONFIRM_FILE_NAME;
    }

    @Override
    public String doPost(HttpServletRequest request) {
        boolean isValid = idValidator.isValid(request, DELIVERY_ID);
        log.debug("isValid" + isValid);
        if (!isValid) {
            log.error("id is not valid client is broken");

            throw new OnClientSideProblemException();
        }
        deliveryService.confirmGettingDelivery(((User) request.getSession().getAttribute(SESSION_USER)).getId(), Long.parseLong(request.getParameter(DELIVERY_ID)));
        request.setAttribute(DELIVERIES_WHICH_ADDRESSED_FOR_USER, deliveryService.getInfoToGetDeliveriesByUserID(((User) request.getSession().getAttribute(SESSION_USER)).getId(), (Locale) request.getSession().getAttribute(SESSION_LANG)));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_GET_CONFIRM_FILE_NAME;
    }
}
