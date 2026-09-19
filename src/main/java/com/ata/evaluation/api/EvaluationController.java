package com.ata.evaluation.api;

import com.ata.evaluation.config.EvaluationRequest;
import com.ata.evaluation.domain.EvaluationRun;
import com.ata.evaluation.experiment.ExperimentComparison;
import com.ata.evaluation.experiment.ExperimentService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
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
}
