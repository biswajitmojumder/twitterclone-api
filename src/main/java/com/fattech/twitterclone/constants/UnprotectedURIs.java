package com.fattech.twitterclone.constants;

public enum UnprotectedURIs {
    LOGIN("/auth/login"),
    SIGNUP("/auth/signup");

    private final String path;

    UnprotectedURIs(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
