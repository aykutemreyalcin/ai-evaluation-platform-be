package com.ata.evaluation.engine;

import static org.junit.jupiter.api.Assertions.*;

import com.ata.evaluation.config.EvaluationRequest;
import com.ata.evaluation.domain.*;
import com.ata.evaluation.langfuse.NoopLangfuseTraceGateway;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class EvaluationEngineTest {
    @Test void passes_when_all_metrics_meet_minimums() {
        var repository = dataset();
        var engine = new EvaluationEngine(repository, List.of(adapter()), List.of(evaluator("correctness", .95)), new NoopLangfuseTraceGateway());
        var run = engine.run(new EvaluationRequest("ata-rag", "v1", "abc", List.of("correctness"), Map.of("correctness", .9), Map.of()));
        assertEquals(RunStatus.PASSED, run.status());
        assertEquals(.95, run.aggregateMetrics().get("correctness"));
    }

    @Test void fails_when_metric_is_below_minimum() {
        var engine = new EvaluationEngine(dataset(), List.of(adapter()), List.of(evaluator("correctness", .5)), new NoopLangfuseTraceGateway());
        var run = engine.run(new EvaluationRequest("ata-rag", "v1", "abc", List.of("correctness"), Map.of("correctness", .9), Map.of()));
        assertEquals(RunStatus.FAILED, run.status());
        assertFalse(run.gateFailures().isEmpty());
    }

    @Test void rejects_unknown_evaluator_before_application_execution() {
        var engine = new EvaluationEngine(dataset(), List.of(adapter()), List.of(), new NoopLangfuseTraceGateway());
        assertThrows(IllegalArgumentException.class, () -> engine.run(new EvaluationRequest("ata-rag", "v1", "abc", List.of("missing"), Map.of(), Map.of())));
    }

    private InMemoryEvaluationDatasetRepository dataset() {
        var repository = new InMemoryEvaluationDatasetRepository();
        repository.put("ata-rag", "v1", List.of(new EvaluationCase("rag-001", "ata-rag", Map.of("question", "Q"), Map.of(), Map.of())));
        return repository;
    }
    private SystemAdapter adapter() { return new SystemAdapter() {
        public String systemId() { return "ata-rag"; }
        public ApplicationExecution execute(EvaluationCase value, Map<String, Object> config) { return new ApplicationExecution(Map.of("answer", "A"), "trace", 1, 2, .01); }
    }; }
    private Evaluator evaluator(String name, double score) { return new Evaluator() {
        public String name() { return name; }
        public String version() { return "v1"; }
        public EvaluationResult evaluate(EvaluationContext context) { return new EvaluationResult(name, "v1", score, score >= .9, "test", "test", Map.of()); }
    }; }
}
