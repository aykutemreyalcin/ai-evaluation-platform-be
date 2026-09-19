package com.ata.evaluation.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

public record EvaluationRequest(@NotBlank String system, @NotBlank String datasetVersion,
                                @NotBlank String applicationVersion, @NotEmpty List<String> evaluators,
                                Map<String, Double> minimumScores, Map<String, Object> configuration) {}
