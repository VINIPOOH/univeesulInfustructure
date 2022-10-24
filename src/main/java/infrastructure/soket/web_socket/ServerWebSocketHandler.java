package infrastructure.soket.web_socket;

import com.google.gson.Gson;
import infrastructure.ApplicationContext;
import infrastructure.ApplicationContextImpl;
import infrastructure.soket.ConnectionNotificationSubscriber;
import infrastructure.soket.web_socket.controller.TcpController;
import infrastructure.soket.web_socket.dto.SocketReceivedMessage;
import infrastructure.soket.web_socket.service.TcpControllerNotificationService;
import infrastructure.soket.web_socket.service.WebSocketSecurityService;
import infrastructure.soket.web_socket.util.MassageDecoder;
import infrastructure.soket.web_socket.util.MassageEncoder;
import lombok.SneakyThrows;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;

@ServerEndpoint(value = "/game", decoders = MassageDecoder.class, encoders = MassageEncoder.class)
public class ServerWebSocketHandler implements ConnectionNotificationSubscriber {
    public static final int HARDCODED_USER_ID = 12;
    //todo ivan find out how made thread wor here

    private WebSocketSecurityService tcpSecurityService;
    private ApplicationContext applicationContext;
    private Map<String, Session> sessionIdToSession;
    private TcpControllerNotificationService tcpControllerNotificationService;
    private Session session;

    public ServerWebSocketHandler() {
        applicationContext = ApplicationContextImpl.getContext();
        applicationContext.addObject(ServerWebSocketHandler.class, this);
        tcpSecurityService = applicationContext.getObject(WebSocketSecurityService.class);
        tcpControllerNotificationService = applicationContext.getObject(TcpControllerNotificationService.class);
    }


    @OnOpen
    public void onOpen(
            Session session) throws IOException {
        this.session = session;
        tcpSecurityService.isUserAuthorizedToRequest(session.getId());
        tcpControllerNotificationService.registerNewSession(HARDCODED_USER_ID, this); // todo change session id on user id
    }

    @OnMessage
    public Object onMessage(Session session, SocketReceivedMessage request)
            throws IOException {

        final TcpController tcpController = applicationContext.getTcpCommandController(request.getMessageType());
        final Object message = convertJsonMassageToDto(request);
        return tcpController.service(message, new WebSocketSession(session));
    }

    @OnClose
    public void onClose(Session session) throws IOException {

    }

    @OnError
    public void onError(Session session, Throwable throwable) {
    }

    @SneakyThrows
    @Override
    public void processMessage(SocketReceivedMessage request) {
        final TcpController tcpController = applicationContext.getTcpCommandController(request.getMessageType());
        final Object massage = convertJsonMassageToDto(request);
        final Object response = tcpController.service(massage, new WebSocketSession(session));
        session.getBasicRemote().sendObject(response);
    }

    @SneakyThrows
    private Object convertJsonMassageToDto(SocketReceivedMessage socketReceivedMessage) {       //todo ivan add proper naming to message from out side and dto
        final Class messageTypeByCode = applicationContext.getMessageTypeByCode(socketReceivedMessage.getMessageType());
        return new Gson().fromJson(socketReceivedMessage.getJsonMessageData(), messageTypeByCode);
    }
}
