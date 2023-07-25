package infrastructure.soket.web_socket;

import com.google.gson.Gson;
import infrastructure.ApplicationContext;
import infrastructure.ApplicationContextImpl;
import infrastructure.soket.ConnectionNotificationSubscriber;
import infrastructure.soket.web_socket.controller.ErrorDto;
import infrastructure.soket.web_socket.controller.TcpController;
import infrastructure.soket.web_socket.dto.SocketReceivedMessage;
import infrastructure.soket.web_socket.service.IdentityCommunicationSessionService;
import infrastructure.soket.web_socket.util.MassageDecoder;
import infrastructure.soket.web_socket.util.MassageEncoder;
import lombok.SneakyThrows;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/socket", decoders = MassageDecoder.class, encoders = MassageEncoder.class)
public class WebSocketFrontController implements ConnectionNotificationSubscriber {
    private ApplicationContext applicationContext;
    private IdentityCommunicationSessionService identityCommunicationSessionService;
    private Session session;

    public WebSocketFrontController() {
        applicationContext = ApplicationContextImpl.getContext();
        applicationContext.addObject(WebSocketFrontController.class, this);
        identityCommunicationSessionService = applicationContext.getObject(IdentityCommunicationSessionService.class);
    }


    @OnOpen
    public void onOpen(
            Session session) throws IOException {
        this.session = session;
        identityCommunicationSessionService.registerNewEmptySession(session.getId(), this);
    }

    @OnMessage
    public Object onMessage(Session session, SocketReceivedMessage request) {
        String requestMessageCode = request.getMessageCode();
        if (!requestMessageCode.equals("LOGIN") && !identityCommunicationSessionService.isUserLongedIn(session)) {
            return new ErrorDto("You are not logged in so your request was declined");
        }

        Class messageTypeByCode = applicationContext.getMessageTypeByCode(requestMessageCode);
        final TcpController tcpController = applicationContext.getTcpCommandController(requestMessageCode);


        return tcpController.service(messageTypeByCode.cast(convertJsonMassageToDto(request)), session);
    }

    @OnClose
    public void onClose(Session session) {
        identityCommunicationSessionService.closeSession(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        identityCommunicationSessionService.closeSession(session);
    }

    @SneakyThrows
    @Override
    public void processMessage(Object request) {
        String messageCode = applicationContext.getMessageCodeByType(request.getClass());
        final TcpController tcpController = applicationContext.getTcpCommandController(messageCode);


        final Object response = tcpController.service(request, session);
        session.getBasicRemote().sendObject(response);
    }

    @SneakyThrows
    private Object convertJsonMassageToDto(SocketReceivedMessage socketReceivedMessage) {
        final Class messageTypeByCode = applicationContext.getMessageTypeByCode(socketReceivedMessage.getMessageCode());
        return new Gson().fromJson(socketReceivedMessage.getJsonMessageData(), messageTypeByCode);
    }
}
