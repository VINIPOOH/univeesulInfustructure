package infrastructure.tcp.connection;

import infrastructure.ApplicationContext;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import infrastructure.tcp.service.TcpControllerNotificationService;
import infrastructure.tcp.service.TcpSecurityService;
import infrastructure.tcp.service.TcpSessionService;
import lombok.SneakyThrows;

import java.net.ServerSocket;
import java.net.Socket;

@NeedConfig
@Singleton
public class TcpServerHandler implements Runnable {

    @InjectByType
    private ApplicationContext applicationContext;
    @InjectByType
    private TcpSecurityService tcpSecurityService;
    @InjectByType
    private TcpControllerNotificationService tcpControllerNotificationService;
    @InjectByType
    private TcpSessionService tcpSessionService;

    @SneakyThrows
    @Override
    public void run() {
        final ServerSocket server = new ServerSocket(8988);
        while (true) {
            try (
                    final Socket socket = server.accept();
            ) {
                tcpSecurityService.identifyUser(socket);
                final TcpConnectionHandler tcpConnectionHandler = applicationContext.getObject(TcpConnectionHandler.class);
                tcpConnectionHandler.setSocket(socket);
                tcpControllerNotificationService.registerNewConnectionHandler((String) tcpSessionService.getAttribute("USER_ID"), tcpConnectionHandler);
                new Thread(tcpConnectionHandler).start();
            }
        }
    }
}
