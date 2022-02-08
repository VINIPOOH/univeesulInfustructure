package infrastructure.soket.web_socket.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TcpControllerResponse {
    private final String messageType;
    private final Object response;
}
