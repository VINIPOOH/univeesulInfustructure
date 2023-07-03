package infrastructure.soket.web_socket.dto;

import infrastructure.anotation.NetworkDto;

@NetworkDto(massageCode = "404")
public class Error404Dto {
    private String errorDescription;

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
}
