package no.fintlabs.authentication;

import lombok.extern.slf4j.Slf4j;
import no.fintlabs.Props;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class TokenService {
    private final Props props;
    private final WebClient webClient;

    private final MetricService metricService;

    public TokenService(Props props, WebClient webClient, MetricService metricService) {
        this.props = props;
        this.webClient = webClient;
        this.metricService = metricService;
    }

    public Mono<Token> fetchToken(String uri) {
        return webClient
                .post()
                .uri(uri + "/nidp/oauth/nam/token")
                .body(BodyInserters.fromFormData(props.getFormData()))
                .retrieve()
                .onStatus((HttpStatus::isError), it -> Mono.empty())
                .bodyToMono(Token.class)
                .onErrorResume(throwable -> {
                    metricService.updateMetric(uri, 0);
                    log.error("{}", throwable.getMessage());
                    return Mono.empty();
                });
    }

    public Mono<IntrospectToken> introspectToken(String uri, String accessToken) {

        return webClient
                .post()
                .uri(uri + "/nidp/oauth/v1/nam/introspect")
                .header("Authorization", props.getIntrospectTokenAuthorizationHeader())
                .body(BodyInserters.fromFormData("token", accessToken))
                .retrieve()
                .onStatus((HttpStatus::isError), it -> Mono.empty())
                .bodyToMono(IntrospectToken.class)
                .onErrorResume(throwable -> {
                    metricService.updateMetric(uri, 0);
                    log.error("{}", throwable.getMessage());
                    return Mono.empty();
                });
    }


}