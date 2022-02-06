package infrastructure.tcp.utils;

import com.google.gson.Gson;
import infrastructure.tcp.dto.TcpControllerResponse;
import lombok.SneakyThrows;

import static infrastructure.constant.AttributeConstants.TCP_MESSAGE_TYPE_SPLITTER;

public class MassageCreator {



    public String createMessage(TcpControllerResponse tcpControllerResponse) {
        return tcpControllerResponse.getMessageType() + TCP_MESSAGE_TYPE_SPLITTER + convertObjectTpJson(tcpControllerResponse.getResponse());
    }

    @SneakyThrows
    private String convertObjectTpJson(Object object) {
        return new Gson().toJson(object);
    }
}
