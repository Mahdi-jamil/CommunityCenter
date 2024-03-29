package com.devesta.blogify.security.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("""
            select t from Token t inner join User u on t.user.userId = u.userId
             where t.user.userId = ?1 and (t.expired = false or t.revoked = false)
            """)
    List<Token> findAllValidTokenByUserId(Long userId);

    Optional<Token> findByToken(String token);

    @Modifying
    @Query("Update Token t set t.revoked = true")
    void revokeAllToken();


}