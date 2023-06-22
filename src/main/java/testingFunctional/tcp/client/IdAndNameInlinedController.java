package testingFunctional.tcp.client;

import infrastructure.anotation.TcpEndpoint;
import infrastructure.soket.web_socket.WebSocketSession;
import infrastructure.soket.web_socket.controller.AbstractTcpController;

import static testingFunctional.tcp.client.RequestCodes.FIRST_REQUEST_ID_NAME;
import static testingFunctional.tcp.client.RequestCodes.ID_AND_NAME_INLINED_DTO;

@TcpEndpoint(requestMessageCode = ID_AND_NAME_INLINED_DTO)
public class IdAndNameInlinedController extends AbstractTcpController<IdAndNameInlinedDto> {
    @Override
    public Object service(IdAndNameInlinedDto request, WebSocketSession session) {
        request.setAllInfoInOneString(request.getAllInfoInOneString());
        return request;
    }
}
