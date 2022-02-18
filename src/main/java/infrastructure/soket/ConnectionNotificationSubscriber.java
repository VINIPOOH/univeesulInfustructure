package infrastructure.soket;

import infrastructure.soket.web_socket.WebSocketSession;
import infrastructure.soket.web_socket.dto.TcpControllerRequest;
import lombok.SneakyThrows;

public interface ConnectionNotificationSubscriber {
    @SneakyThrows
    void processMessage(TcpControllerRequest request);
}
