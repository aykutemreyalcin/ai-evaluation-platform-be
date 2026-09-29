package com.ata.evaluation.langfuse;

import com.ata.evaluation.domain.EvaluationContext;
import com.ata.evaluation.domain.EvaluationResult;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "app.langfuse", name = "enabled", havingValue = "false", matchIfMissing = true)
public class NoopLangfuseTraceGateway implements LangfuseTraceGateway {
    public String traceEvaluation(EvaluationContext context, List<EvaluationResult> results) { return context.traceId(); }
}
