package com.devesta.blogify.domain.dao;

import com.devesta.blogify.domain.entity.Community;
import com.devesta.blogify.domain.entity.enumerated.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDao {
    private Long id;
    private String username;
    private String email;
    private LocalDate createdDate;
    private Role role;
}