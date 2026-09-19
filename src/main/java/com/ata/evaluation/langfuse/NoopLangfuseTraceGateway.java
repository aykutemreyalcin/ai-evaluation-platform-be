package com.ata.evaluation.langfuse;

import com.ata.evaluation.domain.EvaluationContext;
import com.ata.evaluation.domain.EvaluationResult;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(LangfuseTraceGateway.class)
public class NoopLangfuseTraceGateway implements LangfuseTraceGateway {
    public String traceEvaluation(EvaluationContext context, List<EvaluationResult> results) { return context.traceId(); }
}
