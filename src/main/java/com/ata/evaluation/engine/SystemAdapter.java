package com.ata.evaluation.engine;

import com.ata.evaluation.domain.EvaluationCase;
import java.util.Map;

public interface SystemAdapter {
    String systemId();
    ApplicationExecution execute(EvaluationCase evaluationCase, Map<String, Object> configuration);

    record ApplicationExecution(Map<String, Object> output, String traceId, long latencyMs,
                                long tokenUsage, double estimatedCost) {}
}
