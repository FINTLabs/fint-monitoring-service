package no.fintlabs.authentication;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Ref;

@Getter
@Setter
@ToString
public class TokenModel {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    private String acr;
    private String scope;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expires_in")
    private int expiresIn;

    public TokenModel(String accessToken, String refreshToken, String acr, String scope, String tokenType, int expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.acr = acr;
        this.scope = scope;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }
}
