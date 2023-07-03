package infrastructure.soket.web_socket.service;

import infrastructure.soket.ConnectionNotificationSubscriber;

import javax.websocket.Session;
import java.util.List;

public interface IdentityCommunicationSessionService {
    void unsubscribeOnUsers(int subscriberId, List<Integer> idsOfNotisiers);

    void sendShearedState(Session senderSession, Object message);

    void registerNewEmptySession(String sessionId, ConnectionNotificationSubscriber subscriber);

    int getUserId(Session session);

    void subscribeOnUsers(int subscriberId, List<Integer> notifiersList);

    void registerUserToSession(int userId, Session session);

    boolean isUserLongedIn(Session session);

    void closeSession(Session session);
}
