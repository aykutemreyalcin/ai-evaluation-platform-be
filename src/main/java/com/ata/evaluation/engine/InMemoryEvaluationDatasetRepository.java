package com.ata.evaluation.engine;

import com.ata.evaluation.domain.EvaluationCase;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryEvaluationDatasetRepository implements EvaluationDatasetRepository {
    private final Map<String, List<EvaluationCase>> datasets = new ConcurrentHashMap<>();
    public void put(String system, String version, List<EvaluationCase> cases) { datasets.put(key(system, version), List.copyOf(cases)); }
    public List<EvaluationCase> findCases(String system, String datasetVersion) {
        var cases = datasets.get(key(system, datasetVersion));
        if (cases == null) throw new IllegalArgumentException("Unknown dataset: " + system + "/" + datasetVersion);
        return cases;
    }
    private String key(String system, String version) { return system + ":" + version; }
}
