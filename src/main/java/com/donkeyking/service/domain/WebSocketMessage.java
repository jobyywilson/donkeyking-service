package com.donkeyking.service.domain;

public class WebSocketMessage {
    private final String from;
    private final String type;
    private final Object data;

    public WebSocketMessage() {
        this.from = null;
        this.type = null;
        this.data = null;
    }

    public WebSocketMessage(String from, String type, Object data) {
        this.from = from;
        this.type = type;
        this.data = data;
    }

    public String getFrom() {
        return from;
    }

    public String getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

}
