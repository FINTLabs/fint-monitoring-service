package no.fintlabs.authentication

import no.fintlabs.Props
import org.springframework.http.HttpStatus
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import spock.lang.Specification

class TokenServiceSpec extends Specification {

    private TokenService tokenService

    void setup() {
        def props = Mock(Props) {
            getFormData() >> new LinkedMultiValueMap<>()
        }

        def webClient = WebClient.builder()
                .exchangeFunction(clientRequest ->
                        Mono.just(ClientResponse.create(HttpStatus.OK)
                                .header("content-type", "application/json")
                                .body("{ \"access_token\" : \"tokenvalue\"}")
                                .build())
                ).build()

        tokenService = new TokenService(props, webClient)
    }

    def "When getting access token a token should be present"() {
        when:
        def token = tokenService.fetchToken("idp.felleskomponent.no")

        then:
        token.block().getAccessToken() == "tokenvalue"
    }

//    def "If we don't get a valid response from the IDP an exception should be thrown"() {
//        when:
//        def token = tokenService.getAccessToken()
//
//        then:
//        authorizedClientManager.authorize(_ as OAuth2AuthorizeRequest) >> Mono.error()
//    }
}
