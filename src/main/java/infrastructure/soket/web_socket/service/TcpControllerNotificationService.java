package infrastructure.soket.web_socket.service;

import infrastructure.soket.ConnectionNotificationSubscriber;
import infrastructure.soket.web_socket.dto.SocketReceivedMessage;

import java.util.List;

public interface TcpControllerNotificationService {
    void sendShearedState(int senderId, SocketReceivedMessage message);

    void registerNewSession(int userId, ConnectionNotificationSubscriber subscriber);

    void subscribeOnUser(int subscriberId, List<Integer> notifiersList);

    void subscribeOnGameFindAction(int subscriberId);

    void notifyUsersAboutNewGame(List<Integer> usersIds, String massageCode);
}
