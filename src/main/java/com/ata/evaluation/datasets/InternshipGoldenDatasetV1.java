package com.ata.evaluation.datasets;

import com.ata.evaluation.domain.EvaluationCase;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/** Synthetic cases model document facts only; they contain no names, emails, or source documents. */
public final class InternshipGoldenDatasetV1 {
    public static final String VERSION = "internship-golden-v1";
    private static final List<String> CATEGORIES = List.of("valid", "invalid", "missing-document", "ambiguous", "incomplete", "conflicting", "extraction-error", "policy-edge");
    private InternshipGoldenDatasetV1() {}
    public static List<EvaluationCase> cases() {
        return IntStream.rangeClosed(1, 50).mapToObj(index -> {
            var category = CATEGORIES.get((index - 1) % CATEGORIES.size());
            boolean eligible = category.equals("valid") || category.equals("policy-edge");
            boolean missing = category.equals("missing-document") || category.equals("incomplete");
            return new EvaluationCase("internship-%03d".formatted(index), "internship-coordinator",
                    Map.of("applicationReference", "SYN-%03d".formatted(index), "documents", missing ? List.of("application-form") : List.of("application-form", "agreement", "insurance"), "extractedFacts", Map.of("hours", eligible ? 480 : 100, "companyVerified", eligible)),
                    Map.of("extractedFields", Map.of("hours", eligible ? 480 : 100, "companyVerified", eligible), "eligibilityDecision", eligible ? "ELIGIBLE" : "INELIGIBLE",
                            "missingDocuments", missing ? List.of("agreement") : List.of(), "finalRecommendation", eligible && !missing ? "APPROVE" : "REQUEST_CLARIFICATION",
                            "expectedReasonCodes", missing ? List.of("MISSING_REQUIRED_DOCUMENT") : eligible ? List.of("REQUIREMENTS_MET") : List.of("ELIGIBILITY_REQUIREMENT_FAILED")),
                    Map.of("datasetVersion", VERSION, "category", category, "containsPii", false));
        }).toList();
    }
}
