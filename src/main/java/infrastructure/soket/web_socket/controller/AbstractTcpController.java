package infrastructure.soket.web_socket.controller;

import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.soket.web_socket.WebSocketSession;
import infrastructure.soket.web_socket.dto.TcpControllerRequest;
import infrastructure.soket.web_socket.service.TcpControllerNotificationService;

import java.util.List;

@NeedConfig
public abstract class AbstractTcpController implements TcpController {

    @InjectByType
    private TcpControllerNotificationService tcpControllerNotificationService;

    protected final void forwardStateChangeNotification(int userId, TcpControllerRequest message) {
        tcpControllerNotificationService.sendShearedState(userId, message);
    }

    protected final void subscribeToUser(int userId, List<Integer> notifiersList) {
        tcpControllerNotificationService.subscribeOnUser(userId, notifiersList);
    }
}
