package com.ata.evaluation.experiment;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ata.evaluation.config.EvaluationRequest;
import com.ata.evaluation.datasets.InternshipGoldenDatasetV1;
import com.ata.evaluation.datasets.RagGoldenDatasetV1;
import com.ata.evaluation.domain.EvaluationCase;
import com.ata.evaluation.domain.EvaluationContext;
import com.ata.evaluation.domain.EvaluationResult;
import com.ata.evaluation.engine.EvaluationEngine;
import com.ata.evaluation.engine.Evaluator;
import com.ata.evaluation.engine.InMemoryEvaluationDatasetRepository;
import com.ata.evaluation.engine.SystemAdapter;
import com.ata.evaluation.langfuse.NoopLangfuseTraceGateway;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/** Executes immutable golden datasets in CI without network or provider dependencies. */
class CiRegressionGateTest {
    private static final double ALLOWED_DECLINE = 0.02;

    @Test
    void candidate_preserves_all_golden_dataset_metrics_and_cases() {
        var datasets = new InMemoryEvaluationDatasetRepository();
        datasets.put("ata-rag", RagGoldenDatasetV1.VERSION, RagGoldenDatasetV1.cases());
        datasets.put("internship-coordinator", InternshipGoldenDatasetV1.VERSION, InternshipGoldenDatasetV1.cases());
        var service = new ExperimentService(new EvaluationEngine(datasets,
                List.of(contractAdapter("ata-rag"), contractAdapter("internship-coordinator")),
                List.of(contractEvaluator()), new NoopLangfuseTraceGateway()));

        for (var request : List.of(request("ata-rag", RagGoldenDatasetV1.VERSION), request("internship-coordinator", InternshipGoldenDatasetV1.VERSION))) {
            var baseline = service.run(request);
            var candidate = service.run(request);
            var comparison = service.compare(baseline.id(), candidate.id(), ALLOWED_DECLINE);
            assertTrue(comparison.regressions().isEmpty(), () -> "Metric regressions: " + comparison.regressions());
            assertTrue(comparison.newFailures().isEmpty(), () -> "Newly failed cases: " + comparison.newFailures());
        }
    }

    private static EvaluationRequest request(String system, String dataset) {
        return new EvaluationRequest(system, dataset, "ci-candidate", List.of("golden-contract"), Map.of("golden-contract", 1.0), Map.of());
    }

    private static SystemAdapter contractAdapter(String system) {
        return new SystemAdapter() {
            @Override public String systemId() { return system; }
            @Override public ApplicationExecution execute(EvaluationCase evaluationCase, Map<String, Object> configuration) {
                return new ApplicationExecution(evaluationCase.expectedOutput(), "ci-" + evaluationCase.id(), 1, 0, 0);
            }
        };
    }

    private static Evaluator contractEvaluator() {
        return new Evaluator() {
            @Override public String name() { return "golden-contract"; }
            @Override public String version() { return "ci-v1"; }
            @Override public EvaluationResult evaluate(EvaluationContext context) {
                boolean passed = context.evaluationCase().expectedOutput().equals(context.actualOutput());
                return new EvaluationResult(name(), version(), passed ? 1.0 : 0.0, passed,
                        passed ? "MATCH" : "MISMATCH", "CI golden contract comparison", Map.of());
            }
        };
    }
}
