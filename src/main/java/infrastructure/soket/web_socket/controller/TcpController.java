package infrastructure.soket.web_socket.controller;

import infrastructure.soket.web_socket.WebSocketSession;

public interface TcpController {

    Object service(Object request, WebSocketSession session);
}
