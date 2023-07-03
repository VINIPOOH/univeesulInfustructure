package infrastructure.soket.web_socket.service.impl;

import infrastructure.soket.ConnectionNotificationSubscriber;

public class SendNotificationMessage implements Runnable{

    final ConnectionNotificationSubscriber connectionNotificationSubscriber;
    final Object message;

    public SendNotificationMessage(ConnectionNotificationSubscriber connectionNotificationSubscriber, Object message) {
        this.connectionNotificationSubscriber = connectionNotificationSubscriber;
        this.message = message;
    }

    @Override
    public void run() {
        connectionNotificationSubscriber.processMessage(message);
    }
}
