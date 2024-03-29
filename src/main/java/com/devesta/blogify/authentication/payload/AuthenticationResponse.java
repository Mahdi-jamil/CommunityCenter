package com.devesta.blogify.authentication.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    @JsonProperty(value = "token")
    private String accessToken;

    @JsonProperty(value = "refresh_token")
    private String refreshToken;
}
