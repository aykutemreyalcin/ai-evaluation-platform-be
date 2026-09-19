package com.ata.evaluation.datasets;

import com.ata.evaluation.domain.EvaluationCase;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/** Immutable, synthetic golden cases. No case contains student or applicant data. */
public final class RagGoldenDatasetV1 {
    public static final String VERSION = "ata-rag-golden-v1";
    private static final List<String> CATEGORIES = List.of("factual", "multi-source", "ambiguous", "unanswerable", "misleading-assumption", "citation");
    private RagGoldenDatasetV1() {}
    public static List<EvaluationCase> cases() {
        return IntStream.rangeClosed(1, 100).mapToObj(index -> {
            var category = CATEGORIES.get((index - 1) % CATEGORIES.size());
            boolean answerable = !category.equals("unanswerable");
            var question = switch (category) {
                case "factual" -> "What is the official policy topic " + index + "?";
                case "multi-source" -> "Compare official policy topics " + index + " and " + (index + 1) + ".";
                case "ambiguous" -> "What does the university rule mean for topic " + index + "?";
                case "unanswerable" -> "What is the unpublished rule for topic " + index + "?";
                case "misleading-assumption" -> "Why did the university remove the rule for topic " + index + "?";
                default -> "Which official source supports policy topic " + index + "?";
            };
            return new EvaluationCase("rag-%03d".formatted(index), "ata-rag", Map.of("question", question),
                    Map.of("expectedAnswer", answerable ? "Use official ATA information for topic " + index + "." : "The knowledge base does not provide this information.",
                            "acceptedSourceIds", answerable ? List.of("ata-policy-%03d".formatted(index)) : List.of(), "answerability", answerable),
                    Map.of("datasetVersion", VERSION, "category", category, "difficulty", index % 3 == 0 ? "hard" : "standard"));
        }).toList();
    }
}
