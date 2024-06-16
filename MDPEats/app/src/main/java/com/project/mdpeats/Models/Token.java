package com.project.mdpeats.Models;

public class Token {// originally used for firebase messaging
    private String token;
    private boolean isFSOToken;

    public Token(String token, boolean isFSOToken) {
        this.token = token;
        this.isFSOToken = isFSOToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isFSOToken() {
        return isFSOToken;
    }

    public void setFSOToken(boolean FSOToken) {
        isFSOToken = FSOToken;
    }
}
