package infrastructure.soket.web_socket.controller;

import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.TcpEndpoint;
import infrastructure.soket.web_socket.WebSocketSession;
import lombok.SneakyThrows;

@NeedConfig
@TcpEndpoint(requestMessageCode = "LOGIN")
public class LoginTcpController extends AbstractTcpController<LoginDto>{

    @InjectByType
    private LoginWebSocketService loginWebSocketService;

    @SneakyThrows
    @Override
    public Object service(LoginDto request, WebSocketSession session) {
        if (loginWebSocketService.isUserAllowedToLogin(request.getId(), request.getPassword())){
            session.getSession().close();
        }

        registerUserToSession(request.getId(), session.getSession());
        LoginResultDto loginResultDto = new LoginResultDto();
        loginResultDto.setLoginResult("SUCCESS");
        return loginResultDto;
    }
}
