package no.fintlabs.authentication;

import lombok.extern.slf4j.Slf4j;
import no.fintlabs.Props;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class TokenService {
    private final Props props;
    private final WebClient webClient;

    public TokenService(Props props, WebClient webClient) {
        this.props = props;
        this.webClient = webClient;
    }

    public Mono<Token> fetchToken(String uri) {
        return webClient
                .post()
                .uri("https://" + uri + "/nidp/oauth/nam/token")
                .body(BodyInserters.fromFormData(props.getFormData()))
                .retrieve()
                .bodyToMono(Token.class);
    }

    public Mono<IntrospectToken> introspectToken(String uri, String accessToken) {

        return webClient
                .post()
                .uri("https://" + uri + "/nidp/oauth/v1/nam/introspect")
                .header("Authorization", props.getAuthorization())
                .body(BodyInserters.fromFormData("token", accessToken))
                .retrieve()
                .bodyToMono(IntrospectToken.class);
    }
}