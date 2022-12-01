package no.fintlabs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

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

    @Value("${fint.grant.type}")
    private String grantType;

    @Value("${fint.idp.ips}")
    private ArrayList<String> ips;

    @Value("${fint.scope}")
    private String scope;

    @Value("${fint.idp.authorization}")
    private String authorization;

    @Value("${fint.idp.host}")
    private String host;
}
