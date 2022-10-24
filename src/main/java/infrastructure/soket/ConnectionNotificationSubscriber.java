package infrastructure.soket;

import infrastructure.soket.web_socket.dto.SocketReceivedMessage;
import lombok.SneakyThrows;

public interface ConnectionNotificationSubscriber {
    @SneakyThrows
    void processMessage(SocketReceivedMessage request);
}
