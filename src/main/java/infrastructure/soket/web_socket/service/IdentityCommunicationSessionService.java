package infrastructure.soket.web_socket.service;

import infrastructure.soket.ConnectionNotificationSubscriber;
import infrastructure.soket.web_socket.dto.SocketReceivedMessage;

import javax.websocket.Session;
import java.util.List;

public interface IdentityCommunicationSessionService {
    void sendShearedState(Session senderSession, Object message);

    void registerNewEmptySession(String sessionId, ConnectionNotificationSubscriber subscriber);

    int getUserId(Session session);

    void subscribeOnUser(int subscriberId, List<Integer> notifiersList);

    void registerUserToSession(int userId, Session session);

    boolean isUserLongedIn(Session session);
}
