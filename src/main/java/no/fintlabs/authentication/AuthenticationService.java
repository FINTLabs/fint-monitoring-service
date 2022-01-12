package no.fintlabs.authentication;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthenticationService {

    private final TokenService tokenService;
    private final MeterRegistry meterRegistry;

    private final static String FINT_IDP_TOKEN_METRIC = "fint.idp.token";



    private final Map<String, AtomicInteger> gauges = new ConcurrentHashMap<>();


    public AuthenticationService(TokenService tokenService, MeterRegistry meterRegistry) {
        this.tokenService = tokenService;
        this.meterRegistry = meterRegistry;
    }

    //@PostConstruct
    public void verifyTokenIssuing() {
        try {
            String accessToken = tokenService.getAccessToken();
            updateMetric(FINT_IDP_TOKEN_METRIC, Collections.emptyList(), 1);
            log.info("Alles in ordnung 🍻");
        } catch (Exception e) {
            updateMetric(FINT_IDP_TOKEN_METRIC, Collections.emptyList(), 0);
            log.error("💩 {}\n", e.getMessage(), e);
        }


    }

    @Scheduled(cron = "${fint.monitoring.authentication.cron:0 * * * * *}")
    public void verify() {
        verifyTokenIssuing();
    }


    private void updateMetric(String metricType, List<Tag> tags, int value) {
        String gaugeId = metricType + "-" + tags.stream().map(Tag::getValue).collect(Collectors.joining("-"));
        gauges.computeIfPresent(gaugeId, (key, v) -> {
            v.set(value);
            return v;
        });

        gauges.putIfAbsent(gaugeId, meterRegistry.gauge(metricType,
                tags,
                new AtomicInteger(value)));
    }
}
