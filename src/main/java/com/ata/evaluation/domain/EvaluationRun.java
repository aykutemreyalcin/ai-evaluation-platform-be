package com.ata.evaluation.domain;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record EvaluationRun(String id, String system, String datasetVersion, String applicationVersion,
                            RunStatus status, Instant startedAt, Instant completedAt,
                            List<CaseEvaluation> cases, Map<String, Double> aggregateMetrics,
                            List<String> gateFailures) {
    public record CaseEvaluation(String caseId, List<EvaluationResult> results) {}
}
