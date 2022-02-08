package infrastructure.soket;

import infrastructure.soket.web_socket.WebSocketSession;
import infrastructure.soket.web_socket.dto.TcpControllerRequest;

public interface ConnectionNotificationSubscriber {


    void processMessage(TcpControllerRequest request, WebSocketSession session);
}
