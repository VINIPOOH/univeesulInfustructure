package infrastructure.tcp.service;

import java.net.Socket;

public interface TcpSecurityService {
    void identifyUser(Socket socket);
}
