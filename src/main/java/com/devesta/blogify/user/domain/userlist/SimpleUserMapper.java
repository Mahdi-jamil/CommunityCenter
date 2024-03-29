package com.devesta.blogify.user.domain.userlist;

import com.devesta.blogify.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public interface SimpleUserMapper {
    SimpleUserMapper INSTANCE = Mappers.getMapper(SimpleUserMapper.class);

    @Mapping(source = "createdDate", target = "createdDate", dateFormat = "yyyy/mm/dd")
    @Mapping(source = "profileUrl" , target = "image_url")
    UserDto userToUserDao(User user);

}

