package no.fintlabs.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class IntrospectToken {
    private String aud;
    private String sub;
    private String nbf;
    private String scope;
    private String iss;
    private Boolean active;
    private Long exp;
    @JsonProperty("token_type")
    private String tokenType;
    private Long iat;
    private String jti;
    @JsonProperty("client_id")
    private String clientId;
    private String username;
}
