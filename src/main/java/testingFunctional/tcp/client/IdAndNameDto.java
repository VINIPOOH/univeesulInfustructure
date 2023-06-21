package testingFunctional.tcp.client;

import infrastructure.anotation.NetworkDto;

import static testingFunctional.tcp.client.RequestCodes.FIRST_REQUEST_ID_NAME;

@NetworkDto(massageCode = FIRST_REQUEST_ID_NAME)
public class IdAndNameDto {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
