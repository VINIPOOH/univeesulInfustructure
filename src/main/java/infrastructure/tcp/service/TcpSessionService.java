package infrastructure.tcp.service;

public interface TcpSessionService {
    Object getAttribute(String key);

    void addAttribute(String key, Object value);
}
