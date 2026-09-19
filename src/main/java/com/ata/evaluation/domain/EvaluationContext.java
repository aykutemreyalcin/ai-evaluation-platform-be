package com.ata.evaluation.domain;

import java.util.Map;

public record EvaluationContext(EvaluationCase evaluationCase, Map<String, Object> actualOutput,
                                Map<String, Object> configuration, String traceId, long latencyMs,
                                long tokenUsage, double estimatedCost) {}
