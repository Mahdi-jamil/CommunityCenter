package com.devesta.blogify.user.domain;

import lombok.Builder;

@Builder
public record UpdatePayLoad(String email,String password) {
}
