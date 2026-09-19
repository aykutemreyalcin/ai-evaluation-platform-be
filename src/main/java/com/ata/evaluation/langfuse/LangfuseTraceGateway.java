package com.ata.evaluation.langfuse;

import com.ata.evaluation.domain.EvaluationContext;
import com.ata.evaluation.domain.EvaluationResult;
import java.util.List;

public interface LangfuseTraceGateway {
    String traceEvaluation(EvaluationContext context, List<EvaluationResult> results);
}
