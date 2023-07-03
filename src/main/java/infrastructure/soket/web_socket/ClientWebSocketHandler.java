package infrastructure.soket.web_socket;

import com.google.gson.Gson;
import infrastructure.ApplicationContext;
import infrastructure.ApplicationContextImpl;
import infrastructure.soket.web_socket.controller.TcpController;
import infrastructure.soket.web_socket.dto.SocketReceivedMessage;
import infrastructure.soket.web_socket.service.IdentityCommunicationSessionService;
import infrastructure.soket.web_socket.util.MassageDecoder;
import infrastructure.soket.web_socket.util.MassageEncoder;
import lombok.SneakyThrows;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint(decoders = MassageDecoder.class, encoders = MassageEncoder.class)
public class ClientWebSocketHandler {//с етип пока не понятно что делать, вероятно он не должен отличаться от сервер сокета

    private IdentityCommunicationSessionService identityCommunicationSessionService;
    private ApplicationContext applicationContext;
    private Session session;

    public ClientWebSocketHandler(URI endpointURI) {
        applicationContext = ApplicationContextImpl.getContext();
        applicationContext.addObject(ServerWebSocketHandler.class, this);
        identityCommunicationSessionService = applicationContext.getObject(IdentityCommunicationSessionService.class);
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(
            Session session) throws IOException {
        this.session = session;
    }

    @OnMessage
    public Object onMessage(Session session, SocketReceivedMessage request) {

        final TcpController tcpController = applicationContext.getTcpCommandController(request.getMessageCode());
        final Object message = convertJsonMassageToDto(request);
        return tcpController.service(message, session);
    }

    @OnClose
    public void onClose(Session session) throws IOException {

    }

    @OnError
    public void onError(Session session, Throwable throwable) {
    }


    @SneakyThrows
    private Object convertJsonMassageToDto(SocketReceivedMessage receivedMessage) {
        final Class messageTypeByCode = applicationContext.getMessageTypeByCode(receivedMessage.getMessageCode());
        return new Gson().fromJson(receivedMessage.getJsonMessageData(), messageTypeByCode);
    }
}
