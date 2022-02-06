package infrastructure.tcp.connection;

import infrastructure.tcp.dto.TcpControllerMessage;
import lombok.SneakyThrows;

public interface ConnectionNotificationSubscriber {
    @SneakyThrows
    void processMessage(TcpControllerMessage message);
}
