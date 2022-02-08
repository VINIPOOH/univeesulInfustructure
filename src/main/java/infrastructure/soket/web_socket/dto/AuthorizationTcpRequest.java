package infrastructure.soket.web_socket.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthorizationTcpRequest {
    private String userId;
}
