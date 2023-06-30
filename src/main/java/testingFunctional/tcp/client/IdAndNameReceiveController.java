package testingFunctional.tcp.client;

import infrastructure.anotation.TcpEndpoint;
import infrastructure.soket.web_socket.controller.AbstractTcpController;

import javax.websocket.Session;

@TcpEndpoint
public class IdAndNameReceiveController extends AbstractTcpController<IdAndNameDto> {

    @Override
    public Object service(IdAndNameDto request, Session session) {
        IdAndNameInlinedDto idAndNameInlinedDto = new IdAndNameInlinedDto();
        idAndNameInlinedDto.setAllInfoInOneString("recived name is " + request.getName() + " recievd ID is " + request.getId());
        forwardStateChangeNotification(session, idAndNameInlinedDto);
        return idAndNameInlinedDto;
    }
}
