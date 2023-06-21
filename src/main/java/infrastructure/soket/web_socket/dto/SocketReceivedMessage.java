package infrastructure.soket.web_socket.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SocketReceivedMessage {
    private final String messageCode;
    private final String jsonMessageData;
}
