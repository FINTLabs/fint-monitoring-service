package no.fintlabs.authentication;

import lombok.extern.slf4j.Slf4j;
import no.fintlabs.Props;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
public class TokenService {
    private final Props props;
    private final WebClient webClient;

    public TokenService(Props props, WebClient webClient) {
        this.props = props;
        this.webClient = webClient;
    }

    private MultiValueMap<String, String> getFormData() {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.put("grant_type", List.of(props.getGrantType()));
        formData.put("client_id", List.of(props.getClientId()));
        formData.put("client_secret", List.of(props.getClientSecret()));
        formData.put("username", List.of(props.getUsername()));
        formData.put("password", List.of(props.getPassword()));
        formData.put("scope", List.of(props.getScope()));

        return formData;
    }

    public Mono<TokenModel> fetchToken(String uri) {
        return webClient
                .post()
                .uri("https://" + uri + "/nidp/oauth/nam/token")
                .body(BodyInserters.fromFormData(this.getFormData()))
                .retrieve()
                .bodyToMono(TokenModel.class);
    }

    public Mono<IntrospectModel> introspectToken(String uri, String token) {

        return webClient
                .post()
                .uri("https://" + uri + "/nidp/oauth/v1/nam/introspect")
                .header("Authorization", props.getAuthorization())
                .body(BodyInserters.fromFormData("token", token))
                .retrieve()
                .bodyToMono(IntrospectModel.class);
    }
}