package infrastructure.tcp.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TcpControllerResponse {
    private final String messageType;
    private final Object response;
}
