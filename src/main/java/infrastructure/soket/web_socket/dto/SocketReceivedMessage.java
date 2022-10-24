package infrastructure.soket.web_socket.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SocketReceivedMessage {
    private final String messageType;
    private final String jsonMessageData;
}
