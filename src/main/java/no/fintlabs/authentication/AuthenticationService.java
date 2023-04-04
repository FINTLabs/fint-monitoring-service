package no.fintlabs.authentication;

import io.micrometer.core.instrument.Tag;
import lombok.extern.slf4j.Slf4j;
import no.fintlabs.Props;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthenticationService {

    private final TokenService tokenService;

    private final Props props;

    private final MetricService metricService;




    public AuthenticationService(TokenService tokenService, Props props, MetricService metricService) {
        this.props = props;
        this.tokenService = tokenService;
        this.metricService = metricService;
    }

    @PostConstruct
    public void verifyTokenIssuing() {
        try {

            props.getIdps().forEach(uri -> {
                tokenService
                        .fetchToken(uri)
                        .subscribe(token -> {
                            log.debug("Token for {}: {}", uri, token.toString());
                            log.info("Access token is present for {}: {}", uri, !Objects.isNull(token.getAccessToken()));

                            if (StringUtils.hasText(token.getAccessToken())) {
                                tokenService.introspectToken(uri, token.getAccessToken())
                                        .subscribe(introspectToken -> {
                                            log.debug("Introspect token for {}: {}", uri, introspectToken.toString());
                                            log.info("Introspect is active for {}: {}", uri, introspectToken.getActive());
                                            if (introspectToken.isNotActive()) {
                                                metricService.updateMetric(uri, 0);
                                            } else {
                                                log.info("Alles in ordnung for {} 🍻", uri);
                                                metricService.updateMetric( uri, 1);
                                            }
                                        });
                            } else {
                                metricService.updateMetric( uri, 0);
                            }
                        });
            });

        } catch (Exception e) {
            log.error("💩 {}\n", e.getMessage(), e);
        }
    }

    @Scheduled(cron = "${fint.monitoring.authentication.cron:*/30 * * * * *}")
    public void verify() {
        verifyTokenIssuing();
    }


}
