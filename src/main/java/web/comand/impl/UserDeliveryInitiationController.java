package web.comand.impl;

import dal.entity.User;
import dto.DeliveryOrderCreateDto;
import dto.mapper.RequestDtoMapper;
import dto.validation.Validator;
import infrastructure.anotation.HttpEndpoint;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import logiclayer.exeption.FailCreateDeliveryException;
import logiclayer.exeption.UnsupportableWeightFactorException;
import logiclayer.service.BillService;
import logiclayer.service.LocalityService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import infrastructure.http.controller.MultipleMethodController;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static web.constant.AttributeConstants.SESSION_LANG;
import static web.constant.AttributeConstants.SESSION_USER;
import static web.constant.PageConstance.*;

/**
 * Process "user/user-delivery-initiation" page requests
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton
@NeedConfig
@HttpEndpoint("/http/user/user-delivery-initiation")
public class UserDeliveryInitiationController implements MultipleMethodController {
    private static final String LOCALITY_LIST = "localityList";
    private static final String DELIVERY_WEIGHT = "deliveryWeight";
    private static final String LOCALITY_GET_ID = "localityGetID";
    private static final String LOCALITY_SAND_ID = "localitySandID";
    private static final String ADDRESSEE_EMAIL = "addresseeEmail";
    private static final String INPUT_HAS_ERRORS = "inputHasErrors";


    private static final Logger log = LogManager.getLogger(UserDeliveryInitiationController.class);

    @InjectByType
    private LocalityService localityService;
    @InjectByType
    private BillService billService;
    @InjectByType
    private Validator deliveryOrderCreateDtoValidator;

    @Override
    public String doGet(HttpServletRequest request) {
        log.debug(request.getMethod() + " UserDeliveryInitiationController");

        Locale o = (Locale) request.getSession().getAttribute(SESSION_LANG);
        request.setAttribute(LOCALITY_LIST, localityService.getLocaliseLocalities(o));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_INITIATION_FILE_NAME;
    }

    @Override
    public String doPost(HttpServletRequest request) {
        boolean isValid = deliveryOrderCreateDtoValidator.isValid(request);
        log.debug("isValidRequest = " + isValid);

        request.setAttribute(LOCALITY_LIST, localityService.getLocaliseLocalities((Locale) request.getSession().getAttribute(SESSION_LANG)));
        if (!isValid) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_INITIATION_FILE_NAME;
        }
        try {
            billService.initializeBill(getDeliveryOrderCreateDtoRequestDtoMapper(request).mapToDto(request), ((User) request.getSession().getAttribute(SESSION_USER)).getId());
        } catch (UnsupportableWeightFactorException | FailCreateDeliveryException e) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
        }
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_INITIATION_FILE_NAME;
    }

    private RequestDtoMapper<DeliveryOrderCreateDto> getDeliveryOrderCreateDtoRequestDtoMapper(HttpServletRequest request) {
        return request1 -> DeliveryOrderCreateDto.builder()
                .deliveryWeight(Integer.parseInt(request.getParameter(DELIVERY_WEIGHT)))
                .localityGetID(Long.parseLong(request.getParameter(LOCALITY_GET_ID)))
                .localitySandID(Long.parseLong(request.getParameter(LOCALITY_SAND_ID)))
                .addresseeEmail(request.getParameter(ADDRESSEE_EMAIL))
                .build();
    }


}
