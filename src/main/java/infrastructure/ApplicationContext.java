package infrastructure;

import infrastructure.currency.CurrencyInfo;
import infrastructure.factory.ObjectFactory;
import infrastructure.http.controller.MultipleMethodController;
import infrastructure.soket.web_socket.controller.TcpController;
import infrastructure.сonfig.Config;

/**
 * Represents info about context in which application run.
 * Contains info about currency rates.
 * Controls beans and commands.
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface ApplicationContext {
    void init();

    CurrencyInfo getCurrencyInfo(String langKey);

    <T> void addObject(Class<T> typeKey, Object object);

    <T> T getObject(Class<T> typeKey);

    MultipleMethodController getHttpCommand(String linkKey);

    TcpController getTcpCommandController(String messageType);

    Class getMessageTypeByCode(String messageCode);

    String getMessageCodeByType(Object messageType);

    void setFactory(ObjectFactory factory);

    Config getConfig();
}
