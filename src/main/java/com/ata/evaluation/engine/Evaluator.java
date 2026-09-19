package com.ata.evaluation.engine;

import com.ata.evaluation.domain.EvaluationContext;
import com.ata.evaluation.domain.EvaluationResult;

public interface Evaluator {
    String name();
    String version();
    EvaluationResult evaluate(EvaluationContext context);
}
