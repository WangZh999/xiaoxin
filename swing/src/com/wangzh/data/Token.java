package com.wangzh.data;

public class Token {
    private String token;

    private static class HOLDER {
        private final static Token INSTANCE = new Token();
    }

    public static Token getInstance() {
        return Token.HOLDER.INSTANCE;
    }

    private Token() {
    }

    public String getToken() {
        if (token == null) {
            return "";
        }
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
