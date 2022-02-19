package infrastructure.soket.web_socket.service.impl;

import infrastructure.ApplicationContext;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import infrastructure.soket.ConnectionNotificationSubscriber;
import infrastructure.soket.web_socket.dto.TcpControllerRequest;
import infrastructure.soket.web_socket.service.TcpControllerNotificationService;

import java.util.*;

@NeedConfig
@Singleton
public class TcpControllerNotificationServiceImpl implements TcpControllerNotificationService {

    @InjectByType
    private ApplicationContext applicationContext;

    private final Map<Integer, List<Integer>> userIdToSubscribedUserIdsMap = new HashMap<>();
    private final Map<Integer, ConnectionNotificationSubscriber> userIdToConnectionHandlerMap = new HashMap<>();
    private final Set<Integer> usersSubscribedOnFindGameAction = new HashSet<>();

    @Override
    public void sendShearedState(int senderId, TcpControllerRequest message) {
        userIdToSubscribedUserIdsMap.get(senderId)
                .forEach(s ->userIdToConnectionHandlerMap.get(senderId).processMessage(message));
    }

    @Override
    public void registerNewSession(int userId, ConnectionNotificationSubscriber subscriber) {
        userIdToConnectionHandlerMap.put(userId, subscriber);
        userIdToSubscribedUserIdsMap.putIfAbsent(userId, new ArrayList<>());
    }

    @Override
    public void subscribeOnUser(int subscriberId, List<Integer> notifiersList) {
        userIdToSubscribedUserIdsMap.compute(subscriberId, (s, strings) -> {
            strings.addAll(notifiersList);
            return strings;
        });
    }

    @Override
    public void subscribeOnGameFindAction(int subscriberId){
        usersSubscribedOnFindGameAction.add(subscriberId);
    }

    @Override
    public void notifyUsersAboutNewGame(List<Integer> usersIds, String massageCode){
        TcpControllerRequest request = TcpControllerRequest.builder()
                .messageType(massageCode).build();

        usersIds.forEach(
                (s ->userIdToConnectionHandlerMap.get(s).processMessage(request)));

    }
}
