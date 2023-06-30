package testingFunctional.tcp.client;

import infrastructure.anotation.NetworkDto;

import java.util.List;

import static testingFunctional.tcp.client.RequestCodes.SUBSCRIBE_TO_ID;

@NetworkDto(massageCode = SUBSCRIBE_TO_ID)
public class SubscribeToIdDto {
    private List<Integer> idsToSubscribe;

    public List<Integer> getIdsToSubscribe() {
        return idsToSubscribe;
    }

    public void setIdsToSubscribe(List<Integer> idsToSubscribe) {
        this.idsToSubscribe = idsToSubscribe;
    }
}
