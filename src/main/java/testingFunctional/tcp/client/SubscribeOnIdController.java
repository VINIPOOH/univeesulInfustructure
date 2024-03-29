package testingFunctional.tcp.client;

import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.TcpEndpoint;
import infrastructure.soket.web_socket.controller.AbstractTcpController;
import infrastructure.soket.web_socket.service.IdentityCommunicationSessionService;

import javax.websocket.Session;

@NeedConfig
@TcpEndpoint
public class SubscribeOnIdController extends AbstractTcpController<SubscribeToIdDto> {

    @InjectByType
    private IdentityCommunicationSessionService identityCommunicationSessionService;

    @Override
    public Object service(SubscribeToIdDto request, Session session) {
        subscribeToUsers(identityCommunicationSessionService.getUserId(session), request.getIdsToSubscribe());
        return "Successes";
    }
}
