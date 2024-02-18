package com.devesta.blogify.domain.entity.enumerated;

public enum Role {
    USER("user"),
    ADMIN("admin");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }
}
