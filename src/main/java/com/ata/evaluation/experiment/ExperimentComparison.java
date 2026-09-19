package com.ata.evaluation.experiment;

import com.ata.evaluation.domain.EvaluationRun;
import java.util.List;
import java.util.Map;

public record ExperimentComparison(EvaluationRun baseline, EvaluationRun candidate,
                                   Map<String, MetricComparison> metricComparisons,
                                   List<String> regressions, List<String> newFailures, List<String> resolvedFailures) {
    public record MetricComparison(double baseline, double candidate, String outcome) {}
}
