package com.ata.evaluation.experiment;

import com.ata.evaluation.config.EvaluationRequest;
import com.ata.evaluation.domain.EvaluationRun;
import com.ata.evaluation.engine.EvaluationEngine;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class ExperimentService {
    private final EvaluationEngine engine;
    private final Map<String, EvaluationRun> runs = new LinkedHashMap<>();
    public ExperimentService(EvaluationEngine engine) { this.engine = engine; }
    public synchronized EvaluationRun run(EvaluationRequest request) { var run = engine.run(request); runs.put(run.id(), run); return run; }
    public synchronized List<EvaluationRun> list() { return List.copyOf(runs.values()); }
    public synchronized EvaluationRun get(String id) { return Optional.ofNullable(runs.get(id)).orElseThrow(() -> new NoSuchElementException("Unknown experiment: " + id)); }
    public ExperimentComparison compare(String baselineId, String candidateId, double allowedDecline) {
        var baseline = get(baselineId);
        var candidate = get(candidateId);
        var metrics = new TreeMap<String, ExperimentComparison.MetricComparison>();
        var regressions = new ArrayList<String>();
        var names = new TreeSet<String>();
        names.addAll(baseline.aggregateMetrics().keySet());
        names.addAll(candidate.aggregateMetrics().keySet());
        for (var name : names) {
            double before = baseline.aggregateMetrics().getOrDefault(name, 0d);
            double after = candidate.aggregateMetrics().getOrDefault(name, 0d);
            String outcome = after < before - allowedDecline ? "REGRESSION" : after > before ? "IMPROVEMENT" : "UNCHANGED";
            metrics.put(name, new ExperimentComparison.MetricComparison(before, after, outcome));
            if ("REGRESSION".equals(outcome)) regressions.add(name);
        }
        var baselineFailed = failedIds(baseline);
        var candidateFailed = failedIds(candidate);
        var newFailures = candidateFailed.stream().filter(id -> !baselineFailed.contains(id)).toList();
        var resolved = baselineFailed.stream().filter(id -> !candidateFailed.contains(id)).toList();
        return new ExperimentComparison(baseline, candidate, metrics, regressions, newFailures, resolved);
    }
    private Set<String> failedIds(EvaluationRun run) { var ids = new HashSet<String>(); run.cases().forEach(c -> { if (c.results().stream().anyMatch(r -> !r.passed())) ids.add(c.caseId()); }); return ids; }
}
