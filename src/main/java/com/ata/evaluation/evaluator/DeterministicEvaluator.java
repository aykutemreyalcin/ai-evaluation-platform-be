package com.ata.evaluation.evaluator;

import com.ata.evaluation.domain.*;
import com.ata.evaluation.engine.Evaluator;
import java.util.Map;

/** Reusable deterministic evaluator for boolean and exact-match golden labels. */
public final class DeterministicEvaluator implements Evaluator {
    private final String name; private final String expectedKey; private final String actualKey;
    public DeterministicEvaluator(String name, String expectedKey, String actualKey) { this.name = name; this.expectedKey = expectedKey; this.actualKey = actualKey; }
    public String name() { return name; }
    public String version() { return "v1"; }
    public EvaluationResult evaluate(EvaluationContext context) {
        Object expected = context.evaluationCase().expectedOutput().get(expectedKey); Object actual = context.actualOutput().get(actualKey);
        boolean passed = expected != null && expected.equals(actual);
        return new EvaluationResult(name, version(), passed ? 1 : 0, passed, passed ? "MATCH" : "MISMATCH", passed ? "Expected value matched." : "Expected and actual values differ.", Map.of("expectedKey", expectedKey, "actualKey", actualKey));
    }
}
