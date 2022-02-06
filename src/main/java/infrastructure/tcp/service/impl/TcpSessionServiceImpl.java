package infrastructure.tcp.service.impl;

import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import infrastructure.tcp.service.TcpSessionService;

import java.util.Map;

@NeedConfig
@Singleton
public class TcpSessionServiceImpl implements TcpSessionService {

    private ThreadLocal<Map<String, Object>> sessionAttributes;

    @Override
    public Object getAttribute(String key){
        return sessionAttributes.get().get(key);
    }
    @Override
    public void addAttribute(String key, Object value){
        sessionAttributes.get().put(key, value);
    }
}
