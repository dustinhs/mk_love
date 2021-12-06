package com.pinkcobra.auto.websocket.entity;


import lombok.Data;

import java.io.Serializable;

@Data
public class ClientInfo implements Serializable {

    //private static final long serialVersionUID = 8957107006902627635L;

    private String username;
    private String sessionId;

    public ClientInfo(String username,String sessionId) {
        this.username = username;
        this.sessionId = sessionId;
    }
}
