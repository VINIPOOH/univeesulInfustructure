package infrastructure.tcp.service.impl;

import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import infrastructure.tcp.connection.ConnectionNotificationSubscriber;
import infrastructure.tcp.dto.TcpControllerMessage;
import infrastructure.tcp.service.TcpControllerNotificationService;
import infrastructure.tcp.service.TcpSessionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static infrastructure.constant.AttributeConstants.SESSION_USER_ID;

@NeedConfig
@Singleton
public class TcpControllerNotificationServiceImpl implements TcpControllerNotificationService {

    @InjectByType
    private TcpSessionService tcpSessionService;

    private final Map<String, List<String>> userIdToSubscribedUserIdsMap = new HashMap<>();
    private final Map<String, ConnectionNotificationSubscriber> userIdToConnectionHandlerMap = new HashMap<>();

    @Override
    public void sendShearedState(TcpControllerMessage message) {
        final String user_id = (String) tcpSessionService.getAttribute(SESSION_USER_ID);
        userIdToSubscribedUserIdsMap.get(user_id)
                .forEach(s -> userIdToConnectionHandlerMap.get(s).processMessage(message));
    }

    @Override
    public void registerNewConnectionHandler(String userId, ConnectionNotificationSubscriber handler) {
        userIdToConnectionHandlerMap.put(userId, handler);
        userIdToSubscribedUserIdsMap.putIfAbsent(userId, new ArrayList<>());
    }

    @Override
    public void subscribeOn(String userId) {
        userIdToSubscribedUserIdsMap.compute((String) tcpSessionService.getAttribute(SESSION_USER_ID), (s, strings) -> {
            strings.add(userId);
            return strings;
        });
    }
}
