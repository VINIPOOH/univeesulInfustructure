package testingFunctional.tcp.servises;

import infrastructure.anotation.Singleton;
import infrastructure.soket.web_socket.controller.LoginWebSocketService;

@Singleton
public class LoginWebSocketServiceImpl implements LoginWebSocketService {
    @Override
    public boolean isUserAllowedToLogin(int id, String password) {
        return true;
    }
}
