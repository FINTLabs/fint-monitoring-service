package no.fintlabs.authentication

import no.fintlabs.Props
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier
import spock.lang.Specification

class TokenServiceSpec extends Specification {

    private TokenService tokenService
    def mockWebServer = new MockWebServer()
    def props = Mock(Props) { getFormData() >> new LinkedMultiValueMap<>() }

    void setup() {
        mockWebServer.start()
        tokenService = new TokenService(
                props,
                WebClient.create(), Mock(MetricService.class)
        )
    }

    void cleanup() {
        mockWebServer.shutdown()
    }

    def "When getting access token a token should be present"() {
        given:
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody('{"access_token": "tokenValue"}'))

        when:
        def tokenMono = tokenService.fetchToken("localhost:" + mockWebServer.getPort())

        then:
        StepVerifier
                .create(tokenMono)
                .expectNextMatches(token -> token.getAccessToken() == "tokenValue")
                .verifyComplete()
    }

//    def "If we don't get a valid response from the IDP an exception should be thrown"() {
//        when:
//        def token = tokenService.getAccessToken()
//
//        then:
//        authorizedClientManager.authorize(_ as OAuth2AuthorizeRequest) >> Mono.error()
//    }
}
