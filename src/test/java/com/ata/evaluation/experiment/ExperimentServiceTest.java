package com.ata.evaluation.experiment;

import static org.junit.jupiter.api.Assertions.*;
import com.ata.evaluation.config.EvaluationRequest;
import com.ata.evaluation.domain.*;
import com.ata.evaluation.engine.*;
import com.ata.evaluation.langfuse.NoopLangfuseTraceGateway;
import java.util.*;
import org.junit.jupiter.api.Test;

class ExperimentServiceTest {
    @Test void identifies_metric_and_case_regressions() {
        var repo = new InMemoryEvaluationDatasetRepository(); repo.put("ata-rag", "v1", List.of(new EvaluationCase("c1", "ata-rag", Map.of(), Map.of(), Map.of())));
        var values = new ArrayDeque<>(List.of(.95, .70));
        var evaluator = new Evaluator() { public String name() { return "correctness"; } public String version() { return "v1"; } public EvaluationResult evaluate(EvaluationContext context) { var score = values.remove(); return new EvaluationResult(name(), version(), score, score >= .9, "", "", Map.of()); } };
        var adapter = new SystemAdapter() { public String systemId() { return "ata-rag"; } public ApplicationExecution execute(EvaluationCase c, Map<String,Object> x) { return new ApplicationExecution(Map.of(), null, 0, 0, 0); } };
        var service = new ExperimentService(new EvaluationEngine(repo, List.of(adapter), List.of(evaluator), new NoopLangfuseTraceGateway()));
        var request = new EvaluationRequest("ata-rag", "v1", "sha", List.of("correctness"), Map.of(), Map.of());
        var result = service.compare(service.run(request).id(), service.run(request).id(), .02);
        assertEquals(List.of("correctness"), result.regressions()); assertEquals(List.of("c1"), result.newFailures());
    }
}
