package infrastructure.soket.web_socket.controller;

import infrastructure.soket.web_socket.WebSocketSession;

public interface TcpController<RequestType> {

    Object service(RequestType request, WebSocketSession session);
}
