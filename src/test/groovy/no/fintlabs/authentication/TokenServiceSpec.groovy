package no.fintlabs.authentication

import no.fintlabs.Props
import org.junit.jupiter.api.Disabled
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.OAuth2AccessToken
import reactor.core.publisher.Mono
import spock.lang.Specification

import java.time.Instant

class TokenServiceSpec extends Specification {

    def tokenService
    def authorizedClientManager

    void setup() {
        authorizedClientManager = Mock(ReactiveOAuth2AuthorizedClientManager)
        tokenService = new TokenService(
                authorizedClientManager,
                new UsernamePasswordAuthenticationToken("drosje", "loyve", Collections.emptyList()),
                new Props()
        )
    }

    def "When getting access token a token should be present"() {
        when:
        def token = tokenService.getAccessToken()

        then:
        token == "tokenValue"
        1 * authorizedClientManager.authorize(_ as OAuth2AuthorizeRequest) >> Mono.just(
                new OAuth2AuthorizedClient(
                        ClientRegistration.withRegistrationId("reg")
                                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                                .clientId("id")
                                .tokenUri("uri")
                                .build(),
                        "principal",
                        new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                                "tokenValue",
                                Instant.EPOCH, Instant.MAX)
                )
        )


    }

//    def "If we don't get a valid response from the IDP an exception should be thrown"() {
//        when:
//        def token = tokenService.getAccessToken()
//
//        then:
//        authorizedClientManager.authorize(_ as OAuth2AuthorizeRequest) >> Mono.error()
//    }
}
