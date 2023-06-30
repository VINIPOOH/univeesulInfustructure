package infrastructure.soket.web_socket.controller;

import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.TcpEndpoint;
import lombok.SneakyThrows;

import javax.websocket.Session;

@NeedConfig
@TcpEndpoint
public class LoginTcpController extends AbstractTcpController<LoginDto>{

    @InjectByType
    private LoginWebSocketService loginWebSocketService;

    @SneakyThrows
    @Override
    public Object service(LoginDto request, Session session) {
        if (!loginWebSocketService.isUserAllowedToLogin(request.getId(), request.getPassword())){
            session.close();
        }

        registerUserToSession(request.getId(), session);
        LoginResultDto loginResultDto = new LoginResultDto();
        loginResultDto.setLoginResult("SUCCESS");
        return loginResultDto;
    }
}
