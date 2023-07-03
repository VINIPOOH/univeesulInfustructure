package infrastructure.soket.web_socket.controller;

import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.soket.web_socket.service.IdentityCommunicationSessionService;

import javax.websocket.Session;
import java.util.List;

@NeedConfig
public abstract class AbstractTcpController<RequestType> implements TcpController<RequestType> {

    @InjectByType
    private IdentityCommunicationSessionService identityCommunicationSessionService;

    protected final void forwardStateChangeNotification(Session senderSession, Object message) {
        identityCommunicationSessionService.sendShearedState(senderSession, message);
    }

    protected final void subscribeToUsers(int userId, List<Integer> notifiersUsersIdsList) {
        identityCommunicationSessionService.subscribeOnUsers(userId, notifiersUsersIdsList);
    }

    protected final void unsubscribeOnUsers(int userId, List<Integer> notifiersUsersIdsList) {
        identityCommunicationSessionService.unsubscribeOnUsers(userId, notifiersUsersIdsList);
    }

    final void registerUserToSession(int userId, Session session){
        identityCommunicationSessionService.registerUserToSession(userId, session);
    }
}
