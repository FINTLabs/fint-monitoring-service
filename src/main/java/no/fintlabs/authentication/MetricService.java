package no.fintlabs.authentication;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MetricService {

    private final static String FINT_IDP_TOKEN_METRIC = "fint.idp.token";

    private final MeterRegistry meterRegistry;

    private final Map<String, AtomicInteger> gauges = new ConcurrentHashMap<>();

    public MetricService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void updateMetric(String uri, int value) {
        String gaugeId = FINT_IDP_TOKEN_METRIC + "-" + uri;
        gauges.computeIfPresent(gaugeId, (key, v) -> {
            v.set(value);
            return v;
        });

        gauges.putIfAbsent(gaugeId, meterRegistry.gauge(FINT_IDP_TOKEN_METRIC,
                Collections.singletonList(Tag.of("uri", uri)),
                new AtomicInteger(value)));
    }
}
