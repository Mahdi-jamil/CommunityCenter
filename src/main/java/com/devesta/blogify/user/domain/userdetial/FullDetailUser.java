package com.devesta.blogify.user.domain.userdetial;

import com.devesta.blogify.user.domain.Role;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record FullDetailUser(
        Long userId,
        String username,
        String email,
        String bio,
        LocalDate createdDate,
        Role role,
        String image_url
) {

}
