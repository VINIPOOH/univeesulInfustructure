package testingFunctional.tcp.client;

import infrastructure.anotation.TcpEndpoint;
import infrastructure.soket.web_socket.controller.AbstractTcpController;

import javax.websocket.Session;

@TcpEndpoint
public class IdAndNameInlinedController extends AbstractTcpController<IdAndNameInlinedDto> {
    @Override
    public Object service(IdAndNameInlinedDto request, Session session) {
        IdAndNameInlinedDto idAndNameInlinedDto = new IdAndNameInlinedDto();
        idAndNameInlinedDto.setAllInfoInOneString("anoter User sended" + request.getAllInfoInOneString());
        return idAndNameInlinedDto;
    }
}
