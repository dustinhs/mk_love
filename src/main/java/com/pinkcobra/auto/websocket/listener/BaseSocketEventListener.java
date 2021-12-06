package com.pinkcobra.auto.websocket.listener;



import com.pinkcobra.auto.websocket.controller.BaseSocketController;
import com.pinkcobra.auto.websocket.entity.ClientInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;
import java.util.Map;

@Component
public class BaseSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(BaseSocketEventListener.class);


    @Autowired
    BaseSocketController baseSocketController;



    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) throws Exception {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        GenericMessage connectHeader = (GenericMessage) headerAccessor.getHeader(SimpMessageHeaderAccessor.CONNECT_MESSAGE_HEADER);
        Map<String, List<String>> nativeHeaders = (Map<String, List<String>>) connectHeader.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);

        //접속 유저정보를 받는다.
        //String username = event.getUser().getName();
        String sessionId = headerAccessor.getSessionId();

        //헤더값
        String username = nativeHeaders.get("username").get(0);

        logger.info(username + "[" + sessionId + "] is Socket Connected");



        baseSocketController.addSocketServersClient(new ClientInfo(username, sessionId));
        baseSocketController.sendOnlineUsers(); //접속정보 갱신
    }

    @EventListener
    public void handleSessionConnectedEvent(SessionConnectedEvent event) {
        //제일먼저 인입
        //logger.info("handleSessionConnectedEvent....");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) throws Exception {
        

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());


        String sessionId = headerAccessor.getSessionId();

        logger.info(sessionId + " is Socket Disconnect");
        baseSocketController.removeSocketServersClient(sessionId); //클라이언트 정보 삭제
        baseSocketController.sendOnlineUsers(); //접속정보 갱신

    }


}
