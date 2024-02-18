package com.devesta.blogify.repository;

import com.devesta.blogify.domain.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

//    @Query("select u.createdDate from users u where user_id = ?1 ")
//    LocalDate getCreatedDate(Long id); // todo test for it
//
//    @Query(value = "select createdDate from users u where u.user_id = :uid ",
//            nativeQuery = true)
//    LocalDate getCreatedDateNative(@Param("uid") Long id); // todo test for it
//
//    @Modifying
//    @Transactional //  used in service layer
//    @Query(
//            value = "update users set email = :email where id = :uid",
//            nativeQuery = true
//    )
//    int updateEmail(
//            @Param("email") String emial,
//            @Param("uid") Long id);

}

