package infrastructure.tcp.utils;

import infrastructure.tcp.dto.TcpControllerMessage;

import static infrastructure.constant.AttributeConstants.TCP_MESSAGE_TYPE_SPLITTER;

public class MassageExtractor {

    public TcpControllerMessage extractMessage(String message) {
        final String[] split = message.split(TCP_MESSAGE_TYPE_SPLITTER, 2);
        final String typeCode = split[0];
        final String messageType = typeCode.toUpperCase();
        return TcpControllerMessage.builder()
                .messageType(messageType)
                .jsonMessageData(split[1])
                .build();
    }
}
