package infrastructure.soket.web_socket.util;

import com.google.gson.Gson;
import infrastructure.ApplicationContext;
import infrastructure.ApplicationContextImpl;
import infrastructure.soket.web_socket.dto.TcpControllerRequest;
import infrastructure.soket.web_socket.dto.TcpControllerResponse;
import lombok.SneakyThrows;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import static infrastructure.constant.AttributeConstants.TCP_MESSAGE_TYPE_SPLITTER_REGEX;

public class MassageEncoder implements Encoder.Text<Object> {
    @SneakyThrows
    private String convertObjectTpJson(Object object) {
        return new Gson().toJson(object);
    }

    @Override
    public String encode(Object tcpControllerResponse) throws EncodeException {
        return ApplicationContextImpl.getContext().getMessageCodeByType(tcpControllerResponse.getClass()) + TCP_MESSAGE_TYPE_SPLITTER_REGEX + convertObjectTpJson(tcpControllerResponse);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
