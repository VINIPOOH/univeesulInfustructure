package testingFunctional.tcp.client;

import infrastructure.anotation.NetworkDto;

@NetworkDto(massageCode = "ID_AND_NAME_INLINED_DTO")
public class IdAndNameInlinedDto {
    private String allInfoInOneString;

    public String getAllInfoInOneString() {
        return allInfoInOneString;
    }

    public void setAllInfoInOneString(String allInfoInOneString) {
        this.allInfoInOneString = allInfoInOneString;
    }
}
