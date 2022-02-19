package infrastructure.soket.web_socket;

import com.google.gson.Gson;
import infrastructure.ApplicationContext;
import infrastructure.ApplicationContextImpl;
import infrastructure.soket.web_socket.controller.TcpController;
import infrastructure.soket.web_socket.dto.TcpControllerRequest;
import infrastructure.soket.web_socket.service.WebSocketSecurityService;
import infrastructure.soket.web_socket.util.MassageDecoder;
import infrastructure.soket.web_socket.util.MassageEncoder;
import lombok.SneakyThrows;

import javax.websocket.*;
import java.io.IOException;

@ClientEndpoint(decoders = MassageDecoder.class, encoders = MassageEncoder.class)
public class ClientWebSocketHandler {

    public static final int HARDCODED_USER_ID = 12;
    //todo ivan find out how made thread wor here

    private WebSocketSecurityService tcpSecurityService;
    private ApplicationContext applicationContext;
    private Session session;

    public ClientWebSocketHandler() {
        applicationContext = ApplicationContextImpl.getContext();
        applicationContext.addObject(ServerWebSocketHandler.class, this);
        tcpSecurityService = applicationContext.getObject(WebSocketSecurityService.class);
    }


    @OnOpen
    public void onOpen(
            Session session) throws IOException {
        this.session = session;
        tcpSecurityService.isUserAuthorizedToRequest(session.getId());
    }

    @OnMessage
    public Object onMessage(Session session, TcpControllerRequest request) {

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
    private Object convertJsonMassageToDto(TcpControllerRequest tcpControllerRequest) {       //todo ivan add proper naming to message from out side and dto
        final Class messageTypeByCode = applicationContext.getMessageTypeByCode(tcpControllerRequest.getMessageType());
        return new Gson().fromJson(tcpControllerRequest.getJsonMessageData(), messageTypeByCode);
    }
}
