package com.ata.evaluation.engine;

import com.ata.evaluation.domain.EvaluationCase;
import java.util.List;

public interface EvaluationDatasetRepository {
    List<EvaluationCase> findCases(String system, String datasetVersion);
}
