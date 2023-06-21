package infrastructure.soket.web_socket.controller;

import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.soket.web_socket.dto.SocketReceivedMessage;
import infrastructure.soket.web_socket.service.TcpControllerNotificationService;

import java.util.List;

@NeedConfig
public abstract class AbstractTcpController<RequestType> implements TcpController<RequestType> {

    @InjectByType
    private TcpControllerNotificationService tcpControllerNotificationService;

    protected final void forwardStateChangeNotification(int userId, SocketReceivedMessage message) {
        tcpControllerNotificationService.sendShearedState(userId, message);
    }

    protected final void subscribeToUser(int userId, List<Integer> notifiersUsersIdsList) {
        tcpControllerNotificationService.subscribeOnUser(userId, notifiersUsersIdsList);
    }
}
