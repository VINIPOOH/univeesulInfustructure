package infrastructure.tcp.controller;

import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.tcp.dto.TcpControllerMessage;
import infrastructure.tcp.service.TcpControllerNotificationService;

@NeedConfig
public abstract class AbstractTcpController implements TcpController {

    @InjectByType
    private TcpControllerNotificationService tcpControllerNotificationService;

    public final void forwardStateChangeNotification(TcpControllerMessage message) {
        tcpControllerNotificationService.sendShearedState(message);
    }

    public final void subscribeToUser(String userId){
        tcpControllerNotificationService.subscribeOn(userId);
    }
}
