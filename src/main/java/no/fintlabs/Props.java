package no.fintlabs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Data
@Configuration
public class Props {

    @Value("${fint.idp.username}")
    private String username;

    @Value("${fint.idp.password}")
    private String password;

    @Value("${fint.client.secret}")
    private String clientSecret;

    @Value("${fint.client.id}")
    private String clientId;

    @Value("${fint.grant.type:password}")
    private String grantType;

    @Value("${fint.idps}")
    private ArrayList<String> idps;

    @Value("${fint.scope:profile}")
    private String scope;

    //@Value("${fint.idp.authorization}")
    //private String authorization;

    //@Value("${fint.idp.host}")
    //private String host;

    public MultiValueMap<String, String> getFormData() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.put("grant_type", List.of(this.getGrantType()));
        formData.put("client_id", List.of(this.getClientId()));
        formData.put("client_secret", List.of(this.getClientSecret()));
        formData.put("username", List.of(this.getUsername()));
        formData.put("password", List.of(this.getPassword()));
        formData.put("scope", List.of(this.getScope()));

        return formData;
    }

    public String getIntrospectTokenAuthorizationHeader() {
        return "Basic: " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
    }
}
