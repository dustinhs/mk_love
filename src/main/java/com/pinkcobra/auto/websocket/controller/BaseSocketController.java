package com.pinkcobra.auto.websocket.controller;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pinkcobra.auto.websocket.entity.ClientInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@Controller
public class BaseSocketController {

    private static final Logger logger = LoggerFactory.getLogger(BaseSocketController.class);


    /**
     * CopyOnWriteArraySet 을 사용하여 클라이언트 연결 정보를 저장한다
     */
    private CopyOnWriteArraySet<ClientInfo> socketServersClientList = new CopyOnWriteArraySet<ClientInfo>();

    public SimpMessagingTemplate template;

    public BaseSocketController(SimpMessagingTemplate template) {
        this.template = template;
    }


    public synchronized List<ClientInfo> getOnlineClientInfos(){
        return socketServersClientList.stream().collect(Collectors.toList());
    }


    public synchronized void removeSocketServersClient(String sessionId) {
        socketServersClientList.forEach(client -> {
            if (client.getSessionId().equals(sessionId)) {
                logger.debug("removeSocketServersClient : " + client);
                socketServersClientList.remove(client);
            }
        });
    }

    public void addSocketServersClient(ClientInfo clientInfo) throws Exception {
        this.socketServersClientList.add(clientInfo);
    }



    public void sendTrade(String jsonStr) throws Exception {
        logger.info(jsonStr);
        template.convertAndSend("/subscribe/binance/trade", jsonStr);
    }


    public void sendOnlineUsers() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<ClientInfo> onlineUsers = getOnlineClientInfos();
        logger.info(String.valueOf(onlineUsers));
        template.convertAndSend("/subscribe/onlineUsers", objectMapper.writeValueAsString(onlineUsers));
    }



    @MessageMapping("/getOnlineUsers")
    @SendTo("/subscribe/onlineUsers")
    public String onlineUsers(String message, MessageHeaders headers, MessageHeaderAccessor accessor, Principal principal) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        List<ClientInfo> onlineUsers = getOnlineClientInfos();
        return objectMapper.writeValueAsString(onlineUsers);
    }



    public MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        if (sessionId != null) headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }



}
