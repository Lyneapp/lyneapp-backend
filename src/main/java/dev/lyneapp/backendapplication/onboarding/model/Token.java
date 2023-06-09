package dev.lyneapp.backendapplication.onboarding.model;


import dev.lyneapp.backendapplication.common.model.User;
import dev.lyneapp.backendapplication.onboarding.model.enums.TokenType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "jwt_token")
public class Token {
    @Id
    private String id;
    @NotBlank
    private String token;
    @NotBlank
    private TokenType tokenType = TokenType.BEARER;
    private boolean expired;
    private boolean revoked;

    @Reference
    private User user;
}
