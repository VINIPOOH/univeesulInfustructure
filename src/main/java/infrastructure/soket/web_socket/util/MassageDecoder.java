package infrastructure.soket.web_socket.util;

import infrastructure.soket.web_socket.dto.TcpControllerRequest;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import static infrastructure.constant.AttributeConstants.TCP_MESSAGE_TYPE_SPLITTER_REGEX;

public class MassageDecoder implements Decoder.Text<TcpControllerRequest>{

    @Override
    public TcpControllerRequest decode(String message) throws DecodeException {
        final String[] split = message.split(TCP_MESSAGE_TYPE_SPLITTER_REGEX, 2);
        final String typeCode = split[0];
        final String messageType = typeCode.toUpperCase();
        return TcpControllerRequest.builder()
                .messageType(messageType)
                .jsonMessageData(split[1])
                .build();
    }

    @Override
    public boolean willDecode(String message) {
        return true;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
