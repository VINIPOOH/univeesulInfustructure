package infrastructure.tcp.connection;

import com.google.gson.Gson;
import infrastructure.ApplicationContext;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.tcp.controller.TcpController;
import infrastructure.tcp.dto.*;
import infrastructure.tcp.utils.MassageCreator;
import infrastructure.tcp.utils.MassageExtractor;
import lombok.SneakyThrows;

import java.io.*;
import java.lang.reflect.Method;
import java.net.Socket;

@NeedConfig
public class TcpConnectionHandler implements ConnectionNotificationSubscriber, Runnable {

    @InjectByType
    private MassageExtractor massageExtractor;
    @InjectByType
    private MassageCreator massageCreator;
    @InjectByType
    private ApplicationContext applicationContext;

    private Socket socket;

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @SneakyThrows
    @Override
    public void run() {
        try (
                final BufferedReader inReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final BufferedWriter outWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            while (true){
                final TcpControllerMessage tcpControllerMessage = getMassage(inReader);
                final TcpController tcpController = applicationContext.getTcpCommandController(tcpControllerMessage.getMessageType());
                final Dto message = convertJsonMassageToDto(tcpController, tcpControllerMessage);
                final TcpControllerResponse response = tcpController.service(message);
                outWriter.write(massageCreator.createMessage(response));
            }
        }
    }

    @SneakyThrows
    private TcpControllerMessage getMassage(BufferedReader inReader){
        String inputMassage = inReader.readLine();
        return massageExtractor.extractMessage(inputMassage);

    }

    @SneakyThrows
    private Dto convertJsonMassageToDto(TcpController tcpController, TcpControllerMessage tcpControllerMessage){       //todo ivan add proper naming to message from out side and dto
        final Class<? extends TcpController> commandControllerClass = tcpController.getClass();
        final Method service = commandControllerClass.getMethod("service", Dto.class);//todo cash tis operation two
        final Class<?> parameterType = service.getParameterTypes()[0];
        return (Dto) new Gson().fromJson(tcpControllerMessage.getJsonMessageData(), parameterType);
    }

    @SneakyThrows
    @Override
    public void processMessage(TcpControllerMessage message) {
        try (
                final BufferedWriter outWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ){
            final TcpController tcpController = applicationContext.getTcpCommandController(message.getMessageType());
            final Dto mesage = convertJsonMassageToDto(tcpController, message);
            final TcpControllerResponse response = tcpController.service(mesage);
            outWriter.write(massageCreator.createMessage(response));
        }

    }
}
