package testingFunctional.tcp.client;

import infrastructure.soket.web_socket.controller.AbstractTcpController;

import javax.websocket.Session;

public class IdAndNameInlinedController extends AbstractTcpController<IdAndNameInlinedDto> {
    @Override
    public Object service(IdAndNameInlinedDto request, Session session) {
        return "anoter User sended" + request.getAllInfoInOneString();
    }
}
