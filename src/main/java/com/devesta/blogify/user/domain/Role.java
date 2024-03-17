package com.devesta.blogify.user.domain;

public enum Role {
    USER("user"),
    ADMIN("admin"); // todo add MODERATOR

    private final String name;

    Role(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }
}
