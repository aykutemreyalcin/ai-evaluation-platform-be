package com.ata.evaluation.evaluator;

import static org.junit.jupiter.api.Assertions.*;
import com.ata.evaluation.domain.*;
import java.util.*;
import org.junit.jupiter.api.Test;

class EvaluatorCatalogTest {
    private final LlmJudgeProvider fake = (rubric, input) -> new LlmJudgeProvider.JudgeVerdict(.91, "GROUNDED", "Reviewed by fake judge", "fake-model");
    @Test void exposes_at_least_five_evaluators_per_system() { assertTrue(EvaluatorCatalog.ataRag(fake).size() >= 5); assertTrue(EvaluatorCatalog.internship(fake).size() >= 5); }
    @Test void judge_records_model_and_prompt_version_without_live_call() {
        var result = new LlmJudgeEvaluator("judge", "rubric", "v1", fake).evaluate(new EvaluationContext(new EvaluationCase("x", "ata-rag", Map.of("question", "q"), Map.of("expectedAnswer", "a"), Map.of()), Map.of("answer", "a"), Map.of(), "trace", 0, 0, 0));
        assertTrue(result.passed()); assertEquals("fake-model", result.metadata().get("judgeModel")); assertEquals("v1", result.metadata().get("judgePromptVersion"));
    }
}
