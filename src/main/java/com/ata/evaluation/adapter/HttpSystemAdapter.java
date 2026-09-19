package com.ata.evaluation.adapter;

import com.ata.evaluation.domain.EvaluationCase;
import com.ata.evaluation.engine.SystemAdapter;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import org.springframework.web.client.RestClient;

abstract class HttpSystemAdapter implements SystemAdapter {
    private final RestClient client = RestClient.create();
    abstract String baseUrl();
    abstract String endpoint();
    public ApplicationExecution execute(EvaluationCase evaluationCase, Map<String, Object> configuration) {
        var started = Instant.now();
        try {
            Map<?, ?> response = client.post().uri(baseUrl().replaceAll("/+$", "") + endpoint()).body(evaluationCase.input()).retrieve().body(Map.class);
            @SuppressWarnings("unchecked") var output = (Map<String, Object>) (response == null ? Map.of() : response);
            return new ApplicationExecution(output, null, Duration.between(started, Instant.now()).toMillis(), number(output, "tokenUsage"), decimal(output, "estimatedCost"));
        } catch (Exception exception) {
            return new ApplicationExecution(Map.of("error", "Target system execution failed", "errorType", exception.getClass().getSimpleName()), null, Duration.between(started, Instant.now()).toMillis(), 0, 0);
        }
    }
    private long number(Map<String, Object> output, String key) { return output.get(key) instanceof Number number ? number.longValue() : 0; }
    private double decimal(Map<String, Object> output, String key) { return output.get(key) instanceof Number number ? number.doubleValue() : 0; }
}
