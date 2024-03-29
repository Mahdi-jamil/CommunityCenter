package com.devesta.blogify.security.jwt;

import com.devesta.blogify.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Long id;

    private String token;

    private Boolean expired;

    private Boolean revoked;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
