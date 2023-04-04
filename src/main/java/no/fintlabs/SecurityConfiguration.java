package no.fintlabs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .authorizeExchange()
                .anyExchange().permitAll()
                .and()
                .httpBasic().disable()
                .build();
    }
//
//    @Bean
//    public Authentication dummyAuthentication() {
//        return new UsernamePasswordAuthenticationToken("drosje", "loyve", Collections.emptyList());
//    }
//
//    @Bean
//    public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(ReactiveClientRegistrationRepository clientRegistrationRepository,
//                                                                         ReactiveOAuth2AuthorizedClientService authorizedClientService) {
//
//        ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider = ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
//                .password().refreshToken().build();
//
//        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager =
//                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);
//
//        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
//
//        authorizedClientManager.setContextAttributesMapper(contextAttributesMapper());
//
//        return authorizedClientManager;
//    }
//
//    private Function<OAuth2AuthorizeRequest, Mono<Map<String, Object>>> contextAttributesMapper() {
//        return authorizeRequest -> {
//            Map<String, Object> contextAttributes = new HashMap<>();
//            contextAttributes.put(OAuth2AuthorizationContext.USERNAME_ATTRIBUTE_NAME, authorizeRequest.getAttribute(OAuth2ParameterNames.USERNAME));
//            contextAttributes.put(OAuth2AuthorizationContext.PASSWORD_ATTRIBUTE_NAME, authorizeRequest.getAttribute(OAuth2ParameterNames.PASSWORD));
//            return Mono.just(contextAttributes);
//        };
//    }

//    @Bean
//    public ClientHttpConnector clientHttpConnector() {
//        HttpClient httpClient = HttpClient.create(ConnectionProvider.builder("laidback").maxLifeTime(Duration.ofHours(1)).maxIdleTime(Duration.ofMinutes(1)).build())
//                .tcpConfiguration(tcpClient -> tcpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 300000)
//                        .doOnConnected(connection -> connection
//                                .addHandlerLast(new ReadTimeoutHandler(120))
//                                .addHandlerLast(new WriteTimeoutHandler(120))));
//
//        return new ReactorClientHttpConnector(httpClient);
//    }

    @Bean
    public WebClient webClient(WebClient.Builder builder) {

        return builder
                .defaultHeader("Host", "idp.felleskomponent.no")
                .build();
    }
}