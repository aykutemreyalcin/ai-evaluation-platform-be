package com.ata.evaluation.datasets;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class InternshipGoldenDatasetV1Test {
    @Test void has_fifty_anonymous_cases_and_all_required_labels() {
        var cases = InternshipGoldenDatasetV1.cases();
        assertEquals(50, cases.size());
        assertEquals(50, cases.stream().map(c -> c.id()).collect(Collectors.toSet()).size());
        assertTrue(cases.stream().allMatch(c -> c.system().equals("internship-coordinator") && Boolean.FALSE.equals(c.metadata().get("containsPii")) && c.expectedOutput().keySet().containsAll(Set.of("extractedFields", "eligibilityDecision", "missingDocuments", "finalRecommendation", "expectedReasonCodes"))));
        assertEquals(Set.of("valid", "invalid", "missing-document", "ambiguous", "incomplete", "conflicting", "extraction-error", "policy-edge"), cases.stream().map(c -> String.valueOf(c.metadata().get("category"))).collect(Collectors.toSet()));
    }
}
