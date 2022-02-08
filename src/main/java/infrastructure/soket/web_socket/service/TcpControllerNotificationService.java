package infrastructure.soket.web_socket.service;

import infrastructure.soket.web_socket.WebSocketSession;
import infrastructure.soket.web_socket.dto.TcpControllerRequest;

public interface TcpControllerNotificationService {
    void sendShearedState(TcpControllerRequest message, WebSocketSession session);

    void registerNewSession(String userId, WebSocketSession session);

    void subscribeOn(String userId, WebSocketSession session);
}
