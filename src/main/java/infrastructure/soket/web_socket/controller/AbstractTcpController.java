package infrastructure.soket.web_socket.controller;

import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.soket.web_socket.WebSocketSession;
import infrastructure.soket.web_socket.dto.TcpControllerRequest;
import infrastructure.soket.web_socket.service.TcpControllerNotificationService;

@NeedConfig
public abstract class AbstractTcpController implements TcpController {

    @InjectByType
    private TcpControllerNotificationService tcpControllerNotificationService;

    protected final void forwardStateChangeNotification(TcpControllerRequest message, WebSocketSession session) {
        tcpControllerNotificationService.sendShearedState(message, session);
    }

    protected final void subscribeToUser(String userId, WebSocketSession session) {
        tcpControllerNotificationService.subscribeOn(userId, session);
    }
}
