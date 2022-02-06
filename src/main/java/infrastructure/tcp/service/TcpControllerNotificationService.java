package infrastructure.tcp.service;

import infrastructure.tcp.connection.ConnectionNotificationSubscriber;
import infrastructure.tcp.dto.TcpControllerMessage;

public interface TcpControllerNotificationService {
    void sendShearedState(TcpControllerMessage message);

    void registerNewConnectionHandler(String userId, ConnectionNotificationSubscriber handler);

    void subscribeOn(String userId);
}
