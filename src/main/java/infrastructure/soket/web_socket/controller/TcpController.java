package infrastructure.soket.web_socket.controller;


import javax.websocket.Session;

public interface TcpController<RequestType> {

    Object service(RequestType request, Session session);
}
