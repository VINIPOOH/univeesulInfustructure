package testingFunctional.tcp.client;

import infrastructure.anotation.TcpEndpoint;
import infrastructure.soket.web_socket.controller.AbstractTcpController;

import javax.websocket.Session;

import static testingFunctional.tcp.client.RequestCodes.ID_AND_NAME_INLINED_DTO;

@TcpEndpoint(requestMessageCode = ID_AND_NAME_INLINED_DTO)
public class IdAndNameInlinedController extends AbstractTcpController<IdAndNameInlinedDto> {
    @Override
    public Object service(IdAndNameInlinedDto request, Session session) {
        request.setAllInfoInOneString(request.getAllInfoInOneString());
        return request;
    }
}
