package infrastructure.soket.web_socket;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.websocket.Session;

@Data
@AllArgsConstructor
public class WebSocketSession {
    private Session session;
}
