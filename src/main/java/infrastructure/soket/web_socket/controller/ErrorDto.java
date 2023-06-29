package infrastructure.soket.web_socket.controller;

import infrastructure.anotation.NetworkDto;

@NetworkDto(massageCode = "ERROR")
public class ErrorDto {
    private String errorMessage;

    public ErrorDto(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
