package com.devesta.blogify.mapper;

import com.devesta.blogify.domain.dao.UserDao;
import com.devesta.blogify.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component// todo
public interface UserMapper {
    @Mapping(target = "createdDate", source = "createdDate", defaultValue = "java.time.LocalDate.now()")
    @Mapping(target = "communitiesSubscribedTo", source = "communitiesSubscribedTo", defaultValue = "java.util.Collections.emptyList()")
    User userDaoToUser(UserDao userDao);

    @Mapping(target = "password", ignore = true)
    UserDao userToUserDao(User user);
}

