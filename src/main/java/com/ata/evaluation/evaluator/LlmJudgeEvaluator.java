package com.ata.evaluation.evaluator;

import com.ata.evaluation.domain.*;
import com.ata.evaluation.engine.Evaluator;
import java.util.Map;

public final class LlmJudgeEvaluator implements Evaluator {
    private final String name, rubric, promptVersion; private final LlmJudgeProvider provider;
    public LlmJudgeEvaluator(String name, String rubric, String promptVersion, LlmJudgeProvider provider) { this.name = name; this.rubric = rubric; this.promptVersion = promptVersion; this.provider = provider; }
    public String name() { return name; } public String version() { return promptVersion; }
    public EvaluationResult evaluate(EvaluationContext context) {
        var verdict = provider.judge(rubric, Map.of("input", context.evaluationCase().input(), "expected", context.evaluationCase().expectedOutput(), "actual", context.actualOutput()));
        return new EvaluationResult(name, promptVersion, verdict.score(), verdict.score() >= .8, verdict.category(), verdict.reason(), Map.of("judgePromptVersion", promptVersion, "judgeModel", verdict.model()));
    }
}
