package infrastructure.tcp.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthorizationTcpRequest {
    private String userId;
}
