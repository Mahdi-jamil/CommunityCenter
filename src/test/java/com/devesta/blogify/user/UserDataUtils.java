package com.devesta.blogify.user;

import com.devesta.blogify.user.domain.Role;
import com.devesta.blogify.user.domain.UserDto;

import java.time.LocalDate;

public class UserDataUtils {

    public UserDataUtils(){}
    LocalDate today = LocalDate.now();

    UserDto userDto1 = UserDto.builder()
            .userId(1L)
            .username("mahdi")
            .email("jamilmahdi77@gmail.com")
            .createdDate(today)
            .role(Role.USER)
            .build();

    UserDto userDto1Updated = UserDto.builder()
            .userId(1L)
            .username("mahdi")
            .email("jamilmahdi78@gmail.com")
            .createdDate(today)
            .role(Role.USER)
            .build();

    UserDto userDto2 = UserDto.builder()
            .userId(2L)
            .username("ali")
            .email("alijamil88@gmail.com")
            .createdDate(today)
            .role(Role.USER)
            .build();

    UserDto adminDto = UserDto.builder()
            .userId(3L)
            .username("hasan")
            .email("hasanjamil99@gmail.com")
            .createdDate(today)
            .role(Role.ADMIN)
            .build();

    int year = today.getYear();
    int month = today.getMonthValue();
    int day = today.getDayOfMonth();
}
