package infrastructure.tcp.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TcpControllerMessage {
    private final String messageType;
    private final String jsonMessageData;
}
