package infrastructure.soket.web_socket.controller;

public interface LoginWebSocketService {
    boolean isUserAllowedToLogin(int id, String password);
}
