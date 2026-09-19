package com.ata.evaluation.datasets;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class RagGoldenDatasetV1Test {
    @Test void has_one_hundred_unique_contract_compliant_cases_with_all_categories() {
        var cases = RagGoldenDatasetV1.cases();
        assertEquals(100, cases.size());
        assertEquals(100, cases.stream().map(c -> c.id()).collect(Collectors.toSet()).size());
        assertTrue(cases.stream().allMatch(c -> c.system().equals("ata-rag") && c.input().containsKey("question") && c.expectedOutput().containsKey("expectedAnswer")));
        assertEquals(Set.of("factual", "multi-source", "ambiguous", "unanswerable", "misleading-assumption", "citation"), cases.stream().map(c -> String.valueOf(c.metadata().get("category"))).collect(Collectors.toSet()));
    }
}
