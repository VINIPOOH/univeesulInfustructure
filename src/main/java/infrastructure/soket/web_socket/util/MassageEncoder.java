package infrastructure.soket.web_socket.util;

import com.google.gson.Gson;
import infrastructure.ApplicationContextImpl;
import lombok.SneakyThrows;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import static infrastructure.constant.AttributeConstants.TCP_MESSAGE_TYPE_SPLITTER;

public class MassageEncoder implements Encoder.Text<Object> {
    @SneakyThrows
    private String convertObjectToJson(Object object) {
        return new Gson().toJson(object);
    }

    @Override
    public String encode(Object message) {
        return ApplicationContextImpl.getContext().getMessageCodeByType(message.getClass()) + TCP_MESSAGE_TYPE_SPLITTER
                + convertObjectToJson(message);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
