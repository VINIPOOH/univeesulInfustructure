package infrastructure.tcp.controller;

import infrastructure.tcp.dto.Dto;
import infrastructure.tcp.dto.TcpControllerResponse;

public interface TcpController {

    TcpControllerResponse service(Dto dto);
}
