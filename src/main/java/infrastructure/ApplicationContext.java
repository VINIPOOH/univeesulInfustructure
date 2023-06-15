package infrastructure;

import infrastructure.currency.CurrencyInfo;
import infrastructure.factory.ObjectFactory;
import infrastructure.http.controller.MultipleMethodController;
import infrastructure.soket.web_socket.controller.TcpController;
import infrastructure.config.Config;

import java.util.ResourceBundle;

/**
 * Represents info about context in which application run.
 * Contains info about currency rates.
 * Controls beans and commands.
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface ApplicationContext {
    ResourceBundle getApplicationConfigurationBundle();

    void init();

//    @SneakyThrows
//    WebSocketMessageSender createClientWebSocketConnection(String serverPath);

    CurrencyInfo getCurrencyInfo(String langKey);

    <T> void addObject(Class<T> typeKey, Object object);

    <T> T getObject(Class<T> typeKey);

    MultipleMethodController getHttpCommand(String linkKey);

    RestUrlCommandProcessorInfo getRestCommand(String linkKey, String requestMethod);

    TcpController getTcpCommandController(String messageType);

    Class getMessageTypeByCode(String messageCode);

    /**
     * retrieves message code by dto class
     *
     * @param messageType key for getting code
     * @return kod of message
     */
    String getMessageCodeByType(Object messageType);

    void setFactory(ObjectFactory factory);

    Config getConfig();
}
