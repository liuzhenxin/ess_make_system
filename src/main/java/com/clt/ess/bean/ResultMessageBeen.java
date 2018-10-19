package com.clt.ess.bean;

public class ResultMessageBeen {
    private String message;
    private Object body;
    public ResultMessageBeen() { }
    public ResultMessageBeen(String message, Object body) {
        message = message;
        body = body;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
