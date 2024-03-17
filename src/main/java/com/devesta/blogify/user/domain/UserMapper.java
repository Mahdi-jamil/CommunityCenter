package com.devesta.blogify.user.domain;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "joinedCommunities", ignore = true)
    User userDaoToUser(UserDto userDto);

    @Mapping(source = "createdDate", target = "createdDate", dateFormat = "yyyy/mm/dd")
    UserDto userToUserDao(User user);

}

