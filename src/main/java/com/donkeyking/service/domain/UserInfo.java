package com.donkeyking.service.domain;

import org.springframework.web.socket.WebSocketSession;

public class UserInfo {

    private String nickName;
    private String userId;
    private String secretId;
    private int chances = 3;
    private WebSocketSession webSocketSession;

    public UserInfo(String nickName, String userId, String secretId, WebSocketSession webSocketSession) {
        this.nickName = nickName;
        this.userId = userId;
        this.secretId = secretId;
        this.webSocketSession = webSocketSession;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    public void setWebSocketSession(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

    public boolean isChanceOver() {
        return this.chances <= 0;
    }

    public void decrementChances() {
        this.chances = this.chances - 1;
    }

    public int getChances() {
        return chances;
    }
}
