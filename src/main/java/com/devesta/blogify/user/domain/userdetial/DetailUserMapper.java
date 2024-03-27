package com.devesta.blogify.user.domain.userdetial;

import com.devesta.blogify.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public interface DetailUserMapper {

    @Mapping(source = "createdDate", target = "createdDate", dateFormat = "yyyy/mm/dd")
    @Mapping(source = "profileUrl", target = "image_url")
    FullDetailUser userToDetailUserDao(User user);

}
