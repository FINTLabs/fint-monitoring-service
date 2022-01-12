package no.fintlabs.authentication;

import no.fintlabs.Props;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class TokenService {
    private final ReactiveOAuth2AuthorizedClientManager authorizedClientManager;
    private final Authentication principal;
    private final Props props;

    public TokenService(ReactiveOAuth2AuthorizedClientManager authorizedClientManager, Authentication principal, Props props) {
        this.authorizedClientManager = authorizedClientManager;
        this.principal = principal;
        this.props = props;
    }

    public String getAccessToken() {

        return Optional.ofNullable(authorizedClient().block())
                .orElseThrow(FetchTokenException::new)
                .getAccessToken()
                .getTokenValue();

    }

    public Mono<OAuth2AuthorizedClient> authorizedClient() {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId("fint")
                .principal(principal)
                .attributes(attributes -> {
                    attributes.put(OAuth2ParameterNames.USERNAME, props.getUsername());
                    attributes.put(OAuth2ParameterNames.PASSWORD, props.getPassword());
                }).build();

        return authorizedClientManager.authorize(authorizeRequest);
    }
}