package com.ata.evaluation.langfuse;

import com.ata.evaluation.domain.EvaluationContext;
import com.ata.evaluation.domain.EvaluationResult;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/** Sends v4 OpenTelemetry JSON to Langfuse; the legacy ingestion API is intentionally not used. */
@Component
@ConditionalOnProperty(prefix = "app.langfuse", name = "enabled", havingValue = "true")
public class OtlpLangfuseTraceGateway implements LangfuseTraceGateway {
    private final LangfuseProperties properties;
    private final RestClient client = RestClient.create();
    private final SecureRandom random = new SecureRandom();

    public OtlpLangfuseTraceGateway(LangfuseProperties properties) { this.properties = properties; }

    @Override public String traceEvaluation(EvaluationContext context, List<EvaluationResult> results) {
        if (!properties.configured()) return context.traceId();
        var traceId = isTraceId(context.traceId()) ? context.traceId() : hex(16);
        var span = Map.of("traceId", traceId, "spanId", hex(8), "name", "evaluate-case", "startTimeUnixNano", String.valueOf(System.currentTimeMillis() * 1_000_000L),
                "endTimeUnixNano", String.valueOf(System.currentTimeMillis() * 1_000_000L), "attributes", List.of(
                attribute("langfuse.observation.type", "evaluator"), attribute("langfuse.observation.input", safeJson(Map.of("caseId", context.evaluationCase().id(), "system", context.evaluationCase().system(), "input", context.evaluationCase().input()))),
                attribute("langfuse.observation.output", safeJson(Map.of("results", results))), attribute("langfuse.environment", properties.environment()),
                attribute("langfuse.observation.metadata.datasetVersion", String.valueOf(context.evaluationCase().metadata().getOrDefault("datasetVersion", "unknown"))),
                attribute("langfuse.observation.metadata.latencyMs", String.valueOf(context.latencyMs()))));
        var body = Map.of("resourceSpans", List.of(Map.of("scopeSpans", List.of(Map.of("spans", List.of(span))))));
        client.post().uri(properties.baseUrl().replaceAll("/+$", "") + "/api/public/otel/v1/traces")
                .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Basic " + credentials()).header("x-langfuse-ingestion-version", "4")
                .body(body).retrieve().toBodilessEntity();
        return traceId;
    }
    private Map<String, Object> attribute(String key, String value) { return Map.of("key", key, "value", Map.of("stringValue", value == null ? "" : value)); }
    private String credentials() { return Base64.getEncoder().encodeToString((properties.publicKey() + ":" + properties.secretKey()).getBytes(StandardCharsets.UTF_8)); }
    private String hex(int bytes) { byte[] value = new byte[bytes]; random.nextBytes(value); return java.util.HexFormat.of().formatHex(value); }
    private boolean isTraceId(String value) { return value != null && value.matches("[0-9a-f]{32}"); }
    private String safeJson(Object value) { return value.toString().replaceAll("(?i)(password|secret|token|api[_-]?key)=[^,}]+", "$1=[REDACTED]"); }
}
