package infrastructure.soket.web_socket.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
/**
 * dto for send message to paired socket
 */
public class SocketSendMessage {
    private final String messageType;

    /**
     * Object will convert into string in
     */
    private final Object response;
}
