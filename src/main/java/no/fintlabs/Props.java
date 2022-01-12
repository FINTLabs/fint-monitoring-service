package no.fintlabs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class Props {

    @Value("${fint.idp.username}")
    private String username;

    @Value("${fint.idp.password}")
    private String password;

}
