package com.devesta.blogify.user.domain.profile;

import lombok.*;


/***
 * 1- Used to return image blob from Postgres database
 * 2- Ignored due to change storage method to Firebase
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@Entity
//@Table(name = "users")
public class ProfileImage {

//    @Id
//    @Column(name = "user_id")
    private Long userId;

//    @Lob
//    @Column(name = "profile_image")
//    @JsonSerialize(using = BlobSerializer.class)
//    private Blob profileImage;

}
