package com.devesta.blogify.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.sql.Blob;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class ProfileImage {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "profile_image")
    private Blob profileImage;

}
