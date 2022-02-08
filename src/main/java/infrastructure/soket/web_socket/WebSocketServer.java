package infrastructure.soket.web_socket;

import com.google.gson.Gson;
import infrastructure.ApplicationContext;
import infrastructure.ApplicationContextImpl;
import infrastructure.soket.ConnectionNotificationSubscriber;
import infrastructure.soket.web_socket.dto.TcpControllerRequest;
import infrastructure.soket.web_socket.dto.TcpControllerResponse;
import infrastructure.soket.web_socket.service.TcpControllerNotificationService;
import infrastructure.soket.web_socket.controller.TcpController;
import infrastructure.soket.web_socket.service.WebSocketSecurityService;
import infrastructure.soket.web_socket.util.MassageDecoder;
import infrastructure.soket.web_socket.util.MassageEncoder;
import lombok.SneakyThrows;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;

@ServerEndpoint(value = "/game", decoders = MassageDecoder.class, encoders = MassageEncoder.class)
public class WebSocketServer implements ConnectionNotificationSubscriber {
    //todo ivan find out how made thread wor here

    private WebSocketSecurityService tcpSecurityService;
    private ApplicationContext applicationContext;
    private Map<String, Session> sessionIdToSession;
    private TcpControllerNotificationService tcpControllerNotificationService;

    public WebSocketServer() {
        applicationContext = ApplicationContextImpl.getContext();
        applicationContext.addObject(WebSocketServer.class, this);
        tcpSecurityService = applicationContext.getObject(WebSocketSecurityService.class);
        tcpControllerNotificationService = applicationContext.getObject(TcpControllerNotificationService.class);
    }


    @OnOpen
    public void onOpen(
            Session session) throws IOException {
        tcpSecurityService.isUserAuthorizedToRequest(session.getId());
        tcpControllerNotificationService.registerNewSession(session.getId(), new WebSocketSession(session)); // todo change session id on user id
    }

    @OnMessage
    public Object onMessage(Session session, TcpControllerRequest request)
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
    public void processMessage(TcpControllerRequest request, WebSocketSession session) {
        final TcpController tcpController = applicationContext.getTcpCommandController(request.getMessageType());
        final Object massage = convertJsonMassageToDto(request);
        final Object response = tcpController.service(massage, session);
        session.getSession().getBasicRemote().sendObject(response);
    }

    @SneakyThrows
    private Object convertJsonMassageToDto(TcpControllerRequest tcpControllerRequest) {       //todo ivan add proper naming to message from out side and dto
        final Class messageTypeByCode = applicationContext.getMessageTypeByCode(tcpControllerRequest.getMessageType());
        return new Gson().fromJson(tcpControllerRequest.getJsonMessageData(), messageTypeByCode);
    }
}
