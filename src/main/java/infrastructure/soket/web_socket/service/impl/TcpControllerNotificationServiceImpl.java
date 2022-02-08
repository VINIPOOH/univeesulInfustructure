package infrastructure.soket.web_socket.service.impl;

import infrastructure.ApplicationContext;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import infrastructure.soket.web_socket.WebSocketServer;
import infrastructure.soket.web_socket.WebSocketSession;
import infrastructure.soket.web_socket.dto.TcpControllerRequest;
import infrastructure.soket.web_socket.service.TcpControllerNotificationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NeedConfig
@Singleton
public class TcpControllerNotificationServiceImpl implements TcpControllerNotificationService {

    @InjectByType
    private ApplicationContext applicationContext;

    private final Map<String, List<String>> userIdToSubscribedUserIdsMap = new HashMap<>();
    private final Map<String, WebSocketSession> userIdToConnectionHandlerMap = new HashMap<>();

    @Override
    public void sendShearedState(TcpControllerRequest message, WebSocketSession session) {
        final String user_id = session.getSession().getId();
        userIdToSubscribedUserIdsMap.get(user_id)
                .forEach(s -> applicationContext.getObject(WebSocketServer.class).processMessage(message, userIdToConnectionHandlerMap.get(s)));
    }

    @Override
    public void registerNewSession(String userId, WebSocketSession session) {
        userIdToConnectionHandlerMap.put(userId, session);
        userIdToSubscribedUserIdsMap.putIfAbsent(userId, new ArrayList<>());
    }

    @Override
    public void subscribeOn(String userId, WebSocketSession session) {
        userIdToSubscribedUserIdsMap.compute(session.getSession().getId(), (s, strings) -> {
            strings.add(userId);
            return strings;
        });
    }
}
