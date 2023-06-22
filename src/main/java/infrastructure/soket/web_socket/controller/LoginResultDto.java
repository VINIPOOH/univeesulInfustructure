package infrastructure.soket.web_socket.controller;

import infrastructure.anotation.NetworkDto;

@NetworkDto(massageCode = "LOGIN_RESULT_DTO")
public class LoginResultDto {
    private String loginResult;

    public String getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(String loginResult) {
        this.loginResult = loginResult;
    }
}
