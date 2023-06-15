package infrastructure.soket.web_socket;

import infrastructure.ApplicationContext;
import infrastructure.soket.web_socket.util.MassageEncoder;
import lombok.SneakyThrows;

import javax.websocket.Session;


public class WebSocketMessageSender {
    private final ApplicationContext applicationContext;
    private final Session session;
    private final MassageEncoder massageEncoder;

    public WebSocketMessageSender(ApplicationContext applicationContext, Session session, MassageEncoder massageEncoder) {
        this.applicationContext = applicationContext;
        this.session = session;
        this.massageEncoder = massageEncoder;
    }

    @SneakyThrows
    public void sandMessage(Object message)
    {
        session.getBasicRemote().sendObject(massageEncoder.encode(message));
    }


}
