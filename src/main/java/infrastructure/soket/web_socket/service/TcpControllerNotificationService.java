package infrastructure.soket.web_socket.service;

import infrastructure.soket.ConnectionNotificationSubscriber;
import infrastructure.soket.web_socket.dto.SocketReceivedMessage;

import javax.websocket.Session;
import java.util.List;

public interface TcpControllerNotificationService {
    void sendShearedState(int senderId, SocketReceivedMessage message);

    void registerNewEmptySession(String sessionId, ConnectionNotificationSubscriber subscriber);

    void subscribeOnUser(int subscriberId, List<Integer> notifiersList);

    void registerUserToSession(int userId, String session);
}
