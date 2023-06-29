package infrastructure.soket.web_socket.controller;

import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.soket.web_socket.dto.SocketReceivedMessage;
import infrastructure.soket.web_socket.service.IdentityCommunicationSessionService;

import javax.websocket.Session;
import java.util.List;

@NeedConfig
public abstract class AbstractTcpController<RequestType> implements TcpController<RequestType> {

    @InjectByType
    private IdentityCommunicationSessionService identityCommunicationSessionService;

    protected final void forwardStateChangeNotification(int userId, SocketReceivedMessage message) {
        identityCommunicationSessionService.sendShearedState(userId, message);
    }

    protected final void subscribeToUser(int userId, List<Integer> notifiersUsersIdsList) {
        identityCommunicationSessionService.subscribeOnUser(userId, notifiersUsersIdsList);
    }

    final void registerUserToSession(int userId, Session session){
        identityCommunicationSessionService.registerUserToSession(userId, session);
    }
}
