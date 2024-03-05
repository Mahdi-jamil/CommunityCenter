package com.devesta.blogify.utils;

import com.devesta.blogify.user.domain.Role;
import com.devesta.blogify.user.domain.User;

import java.time.LocalDate;

public class TestUtils {

    private TestUtils(){}

    public static User user1(){
        return User.builder()
                .username("mahdi")
                .password("root123")
                .email("jamilmahdi77@gmail.com")
                .role(Role.USER)
                .createdDate(LocalDate.now())
                .build();
    }

}
