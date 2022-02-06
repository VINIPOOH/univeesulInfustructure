package infrastructure.tcp.service.impl;

import com.google.gson.Gson;
import infrastructure.anotation.InjectByType;
import infrastructure.tcp.dto.AuthorizationTcpRequest;
import infrastructure.tcp.service.TcpSecurityService;
import infrastructure.tcp.service.TcpSessionService;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import static infrastructure.constant.AttributeConstants.SESSION_USER_ID;

public class TcpSecurityServiceImpl implements TcpSecurityService {

    @InjectByType
    private TcpSessionService tcpSessionService;

    @SneakyThrows
    @Override
    public void identifyUser(Socket socket) {
        try (
                final BufferedReader inReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final BufferedWriter outWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            while (true) {
                final AuthorizationTcpRequest authorizationTcpRequest = new Gson().fromJson(inReader.readLine(), AuthorizationTcpRequest.class);
                tcpSessionService.addAttribute(SESSION_USER_ID, authorizationTcpRequest.getUserId());

                //todo ad invalidation of past session if exists;
            }
        }
    }
}
