package com.ata.evaluation.engine;

import com.ata.evaluation.config.EvaluationRequest;
import com.ata.evaluation.domain.*;
import com.ata.evaluation.langfuse.LangfuseTraceGateway;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class EvaluationEngine {
    private final EvaluationDatasetRepository datasets;
    private final Map<String, SystemAdapter> adapters;
    private final Map<String, Evaluator> evaluators;
    private final LangfuseTraceGateway traceGateway;

    public EvaluationEngine(EvaluationDatasetRepository datasets, List<SystemAdapter> adapters, List<Evaluator> evaluators,
                            LangfuseTraceGateway traceGateway) {
        this.datasets = datasets;
        this.adapters = adapters.stream().collect(Collectors.toUnmodifiableMap(SystemAdapter::systemId, value -> value));
        this.evaluators = evaluators.stream().collect(Collectors.toUnmodifiableMap(Evaluator::name, value -> value));
        this.traceGateway = traceGateway;
    }

    public EvaluationRun run(EvaluationRequest request) {
        var adapter = Optional.ofNullable(adapters.get(request.system())).orElseThrow(() -> new IllegalArgumentException("Unknown system: " + request.system()));
        var selected = request.evaluators().stream().map(name -> Optional.ofNullable(evaluators.get(name))
                .orElseThrow(() -> new IllegalArgumentException("Unknown evaluator: " + name))).toList();
        var started = Instant.now();
        var caseResults = new ArrayList<EvaluationRun.CaseEvaluation>();
        for (var evaluationCase : datasets.findCases(request.system(), request.datasetVersion())) {
            var execution = adapter.execute(evaluationCase, nullToEmpty(request.configuration()));
            var context = new EvaluationContext(evaluationCase, execution.output(), nullToEmpty(request.configuration()), execution.traceId(), execution.latencyMs(), execution.tokenUsage(), execution.estimatedCost());
            var results = selected.stream().map(evaluator -> evaluator.evaluate(context)).toList();
            traceGateway.traceEvaluation(context, results);
            caseResults.add(new EvaluationRun.CaseEvaluation(evaluationCase.id(), results));
        }
        var metrics = aggregate(caseResults);
        var failures = gateFailures(metrics, nullToEmpty(request.minimumScores()));
        return new EvaluationRun(UUID.randomUUID().toString(), request.system(), request.datasetVersion(), request.applicationVersion(),
                failures.isEmpty() ? RunStatus.PASSED : RunStatus.FAILED, started, Instant.now(), List.copyOf(caseResults), metrics, failures);
    }

    private Map<String, Double> aggregate(List<EvaluationRun.CaseEvaluation> cases) {
        return cases.stream().flatMap(c -> c.results().stream()).collect(Collectors.groupingBy(EvaluationResult::evaluator,
                Collectors.averagingDouble(EvaluationResult::score)));
    }
    private List<String> gateFailures(Map<String, Double> metrics, Map<String, Double> minimums) {
        return minimums.entrySet().stream().filter(entry -> metrics.getOrDefault(entry.getKey(), 0d) < entry.getValue())
                .map(entry -> entry.getKey() + " below minimum " + entry.getValue()).toList();
    }
    private <T> Map<String, T> nullToEmpty(Map<String, T> value) { return value == null ? Map.of() : value; }
}
