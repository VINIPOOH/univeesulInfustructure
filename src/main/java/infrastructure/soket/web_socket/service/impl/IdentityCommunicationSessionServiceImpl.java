package infrastructure.soket.web_socket.service.impl;

import infrastructure.ApplicationContext;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import infrastructure.soket.ConnectionNotificationSubscriber;
import infrastructure.soket.web_socket.dto.SocketReceivedMessage;
import infrastructure.soket.web_socket.service.IdentityCommunicationSessionService;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NeedConfig
@Singleton
public class IdentityCommunicationSessionServiceImpl implements IdentityCommunicationSessionService {

    public static final String USER_ID = "USER_ID";
    @InjectByType
    private ApplicationContext applicationContext;

    private final Map<Integer, List<Integer>> userIdToSubscribedUsersIdsMap = new ConcurrentHashMap<>();
    private final Map<String, ConnectionNotificationSubscriber> sessionIdToConnectionHandlerMap = new ConcurrentHashMap<>();
    private final Map<Integer, String> userIdToSessionIdMap = new ConcurrentHashMap<>();

    @Override
    public void registerNewEmptySession(String sessionId, ConnectionNotificationSubscriber subscriber) {
        sessionIdToConnectionHandlerMap.put(sessionId, subscriber);
    }


    @Override
    public void registerUserToSession(int userId, Session session) {
        session.getUserProperties().put(USER_ID, userId);
        userIdToSessionIdMap.put(userId, session.getId());
        userIdToSubscribedUsersIdsMap.put(userId, new ArrayList<>());
    }

    @Override
    public boolean isUserLongedIn(Session session) {
        return session.getUserProperties().get(USER_ID) != null;
    }

    @Override
    public int getUserId(Session session){
        return (Integer) session.getUserProperties().get(USER_ID);
    }

    @Override
    public void subscribeOnUser(int subscriberId, List<Integer> idsOfNotisiers) {
        idsOfNotisiers.stream().forEach(notifierId -> {
            userIdToSubscribedUsersIdsMap.compute(notifierId, (notifierId1, notificationReviversIds) -> {
                notificationReviversIds.add(subscriberId);
                return notificationReviversIds;
            });
        });
    }

    @Override
    public void sendShearedState(Session senderSession, Object message) {
        userIdToSubscribedUsersIdsMap.get(getUserId(senderSession))
                .forEach(integer -> {
                    String sessionId = userIdToSessionIdMap.get(integer);
                    ConnectionNotificationSubscriber connectionNotificationSubscriber = sessionIdToConnectionHandlerMap.get(sessionId);
                    connectionNotificationSubscriber.processMessage(message);
                });
    }

}
