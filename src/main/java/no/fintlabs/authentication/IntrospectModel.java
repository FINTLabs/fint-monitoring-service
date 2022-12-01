package no.fintlabs.authentication;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class IntrospectModel {
    private String aud;
    private String sub;
    private String nbf;
    private String scope;
    private String iss;
    private Boolean active;
    private Long exp;
    private String tokenType;
    private Long iat;
    private String jti;
    private String clientId;
    private String username;

    public IntrospectModel(String aud, String sub, String nbf, String scope, String iss, Boolean active, Long exp, String tokenType, Long iat, String jti, String clientId, String username) {
        this.aud = aud;
        this.sub = sub;
        this.nbf = nbf;
        this.scope = scope;
        this.iss = iss;
        this.active = active;
        this.exp = exp;
        this.tokenType = tokenType;
        this.iat = iat;
        this.jti = jti;
        this.clientId = clientId;
        this.username = username;
    }
}
