package infrastructure.soket.web_socket.controller;

import infrastructure.anotation.NeedConfig;
import infrastructure.soket.web_socket.dto.Error404Dto;
import lombok.SneakyThrows;

import javax.websocket.Session;

@NeedConfig
public class Error404DefaultController extends AbstractTcpController<Error404Dto> {


    @SneakyThrows
    @Override
    public Object service(Error404Dto request, Session session) {
        Error404Dto error404Dto = new Error404Dto();
        error404Dto.setErrorDescription("there now controller for this message");
        return error404Dto;
    }
}
