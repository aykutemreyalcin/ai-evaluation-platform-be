package com.ata.evaluation.evaluator;

import com.ata.evaluation.engine.Evaluator;
import java.util.List;

public final class EvaluatorCatalog {
    private EvaluatorCatalog() {}
    public static List<Evaluator> ataRag(LlmJudgeProvider judge) { return List.of(
            new DeterministicEvaluator("rag-required-fields", "expectedAnswer", "answer"), new DeterministicEvaluator("rag-retrieval-recall-at-k", "acceptedSourceIds", "retrievedSourceIds"),
            new DeterministicEvaluator("rag-retrieval-precision-at-k", "acceptedSourceIds", "retrievedSourceIds"), new DeterministicEvaluator("rag-citation-correctness", "acceptedSourceIds", "citationSourceIds"),
            new DeterministicEvaluator("rag-latency-cost-threshold", "latencyMs", "latencyMs"), new LlmJudgeEvaluator("rag-answer-correctness", "Assess answer correctness and groundedness.", "rag-judge-v1", judge)); }
    public static List<Evaluator> internship(LlmJudgeProvider judge) { return List.of(
            new DeterministicEvaluator("internship-required-fields", "extractedFields", "extractedFields"), new DeterministicEvaluator("internship-extraction-accuracy", "extractedFields", "extractedFields"),
            new DeterministicEvaluator("internship-eligibility-decision", "eligibilityDecision", "eligibilityDecision"), new DeterministicEvaluator("internship-missing-document-detection", "missingDocuments", "missingDocuments"),
            new DeterministicEvaluator("internship-recommendation-correctness", "finalRecommendation", "finalRecommendation"), new LlmJudgeEvaluator("internship-explanation-quality", "Assess explanation quality and hallucination risk.", "internship-judge-v1", judge)); }
}
