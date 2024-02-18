package com.devesta.blogify.service;

import com.devesta.blogify.domain.dao.UserDao;
import com.devesta.blogify.domain.entity.User;
import com.devesta.blogify.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    public final UserRepository userRepository;
//    public final UserMapper userMapper;

    /**
     * @param username Get User by his/her username
     * @return UserDao
     */
    public UserDao getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        // todo

        User user1 = user.orElseThrow(() -> new UsernameNotFoundException("user not found"));

//        return userMapper.userToUserDao(user1);

        return new UserDao(user1.getUserId()
                , user1.getUsername()
                , user1.getEmail()
                , user1.getCreatedDate()
                , user1.getCreatedCommunities()// todo
                , user1.getRole());
    }


}
