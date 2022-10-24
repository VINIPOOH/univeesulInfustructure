package infrastructure.soket.web_socket.util;

import infrastructure.ApplicationContextImpl;
import lombok.SneakyThrows;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import com.google.gson.Gson;

import static infrastructure.constant.AttributeConstants.TCP_MESSAGE_TYPE_SPLITTER_REGEX;

public class MassageEncoder implements Encoder.Text<Object> {
    @SneakyThrows
    private String convertObjectToJson(Object object) {
        return new Gson().toJson(object);
    }

    @Override
    public String encode(Object tcpControllerResponse) {
        return ApplicationContextImpl.getContext().getMessageCodeByType(tcpControllerResponse.getClass()) + TCP_MESSAGE_TYPE_SPLITTER_REGEX
                + convertObjectToJson(tcpControllerResponse);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
