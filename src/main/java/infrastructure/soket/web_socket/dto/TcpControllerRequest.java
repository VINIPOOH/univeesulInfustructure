package infrastructure.soket.web_socket.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TcpControllerRequest {
    private final String messageType;
    private final String jsonMessageData;
}
