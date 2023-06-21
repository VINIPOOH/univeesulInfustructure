package infrastructure.soket.web_socket.util;

import infrastructure.soket.web_socket.dto.SocketReceivedMessage;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import static infrastructure.constant.AttributeConstants.TCP_MESSAGE_TYPE_SPLITTER_REGEX;

public class MassageDecoder implements Decoder.Text<SocketReceivedMessage>{

    @Override
    public SocketReceivedMessage decode(String message) throws DecodeException {
        final String[] split = message.split(TCP_MESSAGE_TYPE_SPLITTER_REGEX, 2);
        final String typeCode = split[0];
        final String messageType = typeCode.toUpperCase();
        return SocketReceivedMessage.builder()
                .messageCode(messageType)
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
