package com.ata.evaluation.domain;

import java.util.Map;

public record EvaluationCase(String id, String system, Map<String, Object> input,
                             Map<String, Object> expectedOutput, Map<String, Object> metadata) {}
