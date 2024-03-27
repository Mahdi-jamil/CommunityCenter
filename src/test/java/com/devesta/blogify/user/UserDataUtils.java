package com.devesta.blogify.user;

import com.devesta.blogify.user.domain.Role;
import com.devesta.blogify.user.domain.userdetial.FullDetailUser;
import com.devesta.blogify.user.domain.userlist.UserDto;

import java.time.LocalDate;

public class UserDataUtils {

    public UserDataUtils(){}
    LocalDate today = LocalDate.now();

    UserDto userDto1 = UserDto.builder()
            .userId(1L)
            .username("mahdi")
            .createdDate(today)
            .image_url("image_url1")
            .build();

    FullDetailUser fullDetailUser = FullDetailUser.builder()
            .userId(1L)
            .username("mahdi")
            .bio("I am Java Backend Software Engineer")
            .email("jamilmahdi77@gmail.com")
            .createdDate(today)
            .role(Role.USER)
            .build();

    UserDto userDto1Updated = UserDto.builder()
            .userId(1L)
            .username("mahdi")
            .createdDate(today)
            .image_url("image_url2")
            .build();

    UserDto userDto2 = UserDto.builder()
            .userId(2L)
            .username("ali")
            .createdDate(today)
            .build();

    UserDto adminDto = UserDto.builder()
            .userId(3L)
            .username("hasan")
            .createdDate(today)
            .image_url("image_url3")
            .build();

    int year = today.getYear();
    int month = today.getMonthValue();
    int day = today.getDayOfMonth();
}
