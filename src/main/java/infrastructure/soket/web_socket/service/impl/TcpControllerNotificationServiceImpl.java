package infrastructure.soket.web_socket.service.impl;

import infrastructure.ApplicationContext;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import infrastructure.soket.ConnectionNotificationSubscriber;
import infrastructure.soket.web_socket.dto.SocketReceivedMessage;
import infrastructure.soket.web_socket.service.TcpControllerNotificationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NeedConfig
@Singleton
public class TcpControllerNotificationServiceImpl implements TcpControllerNotificationService {

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
    public void registerUserToSession(int userId, String session) {
        userIdToSessionIdMap.put(userId, session);
        userIdToSubscribedUsersIdsMap.put(userId, new ArrayList<>());
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
    public void sendShearedState(int senderId, SocketReceivedMessage message) {
        userIdToSubscribedUsersIdsMap.get(senderId)
                .forEach(integer -> {
                    String sessionId = userIdToSessionIdMap.get(integer);
                    ConnectionNotificationSubscriber connectionNotificationSubscriber = sessionIdToConnectionHandlerMap.get(sessionId);
                    connectionNotificationSubscriber.processMessage(message);
                });
    }

}
