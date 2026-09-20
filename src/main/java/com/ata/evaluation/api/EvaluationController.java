package com.ata.evaluation.api;

import com.ata.evaluation.config.EvaluationRequest;
import com.ata.evaluation.datasets.InternshipGoldenDatasetV1;
import com.ata.evaluation.datasets.RagGoldenDatasetV1;
import com.ata.evaluation.domain.EvaluationCase;
import com.ata.evaluation.domain.EvaluationRun;
import com.ata.evaluation.experiment.ExperimentComparison;
import com.ata.evaluation.experiment.ExperimentService;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class EvaluationController {
    private final ExperimentService experiments;
    public EvaluationController(ExperimentService experiments) { this.experiments = experiments; }
    @PostMapping("/evaluations/run") @ResponseStatus(HttpStatus.ACCEPTED)
    public EvaluationRun run(@Valid @RequestBody EvaluationRequest request) { return experiments.run(request); }
    @GetMapping("/evaluations/{id}") public EvaluationRun evaluation(@PathVariable String id) { return experiments.get(id); }
    @GetMapping("/experiments") public List<EvaluationRun> experiments() { return experiments.list(); }
    @GetMapping("/experiments/{id}") public EvaluationRun experiment(@PathVariable String id) { return experiments.get(id); }
    @GetMapping("/experiments/compare") public ExperimentComparison compare(@RequestParam String baseline, @RequestParam String candidate, @RequestParam(defaultValue = "0.02") double allowedDecline) { return experiments.compare(baseline, candidate, allowedDecline); }
    @GetMapping("/systems") public List<Map<String, String>> systems() { return List.of(Map.of("id", "ata-rag", "displayName", "ATA RAG", "status", "CONFIGURED"), Map.of("id", "internship-coordinator", "displayName", "Internship Coordinator", "status", "CONFIGURED")); }
    @GetMapping("/datasets") public List<DatasetSummary> datasets() { return List.of(
            summary(RagGoldenDatasetV1.VERSION, "ata-rag", RagGoldenDatasetV1.cases().size()),
            summary(InternshipGoldenDatasetV1.VERSION, "internship-coordinator", InternshipGoldenDatasetV1.cases().size())); }
    @GetMapping("/datasets/{id}") public DatasetDetail dataset(@PathVariable String id) {
        return switch (id) {
            case RagGoldenDatasetV1.VERSION -> detail(id, "ata-rag", RagGoldenDatasetV1.cases());
            case InternshipGoldenDatasetV1.VERSION -> detail(id, "internship-coordinator", InternshipGoldenDatasetV1.cases());
            default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dataset not found: " + id);
        };
    }
    private static DatasetSummary summary(String id, String system, int caseCount) { return new DatasetSummary(id, system, "v1", caseCount, true, Instant.parse("2026-09-20T00:00:00Z")); }
    private static DatasetDetail detail(String id, String system, List<EvaluationCase> cases) { var summary = summary(id, system, cases.size()); return new DatasetDetail(summary.id(), summary.system(), summary.version(), summary.caseCount(), summary.immutable(), summary.createdAt(), cases.stream().map(item -> new DatasetCase(item.id(), item.metadata())).toList()); }
    public record DatasetSummary(String id, String system, String version, int caseCount, boolean immutable, Instant createdAt) {}
    public record DatasetDetail(String id, String system, String version, int caseCount, boolean immutable, Instant createdAt, List<DatasetCase> cases) {}
    public record DatasetCase(String id, Map<String, Object> metadata) {}
}
