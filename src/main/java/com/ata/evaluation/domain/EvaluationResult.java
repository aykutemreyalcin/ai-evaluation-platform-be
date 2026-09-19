package com.ata.evaluation.domain;

import java.util.Map;

public record EvaluationResult(String evaluator, String evaluatorVersion, double score, boolean passed,
                               String category, String reason, Map<String, Object> metadata) {
    public EvaluationResult {
        if (score < 0 || score > 1) throw new IllegalArgumentException("score must be between 0 and 1");
    }
}
