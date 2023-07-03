package infrastructure;

import infrastructure.currency.CurrencyInfo;
import infrastructure.factory.ObjectFactory;
import infrastructure.http.controller.MultipleMethodController;
import infrastructure.soket.web_socket.ClientWebSocketHandler;
import infrastructure.soket.web_socket.controller.TcpController;
import infrastructure.config.Config;

/**
 * Represents info about context in which application run.
 * Contains info about currency rates.
 * Controls beans and commands.
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface ApplicationContext {
    public String getPropertyValue(String key);

    void init();

    ClientWebSocketHandler createClientWebSocketConnection(String serverPath);

    CurrencyInfo getCurrencyInfo(String langKey);

    <T> void addObject(Class<T> typeKey, Object object);

    <T> T getObject(Class<T> typeKey);

    MultipleMethodController getHttpCommand(String linkKey);

    RestUrlCommandProcessorInfo getRestCommand(String linkKey, String requestMethod);

    TcpController getTcpCommandController(String messageType);

    Class<?> getMessageTypeByCode(String messageCode);

    /**
     * retrieves message code by dto class
     *
     * @param messageType key for getting code
     * @return kod of message
     */
    String getMessageCodeByType(Class<?> messageType);

    void setFactory(ObjectFactory factory);

    Config getConfig();
}
