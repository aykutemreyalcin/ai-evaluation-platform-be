# Evaluator catalog

ATA RAG: required fields, Recall@K, Precision@K, citation correctness, latency/cost threshold, and LLM-judge answer correctness/groundedness. Internship Coordinator: required fields, extraction accuracy, eligibility decision, missing-document detection, recommendation correctness, and LLM-judge explanation quality/hallucination.

Deterministic metrics score exact golden-label agreement from 0 to 1. Recall and precision need a set-aware implementation before production data is enabled; the current catalog establishes stable evaluator identities and contract tests. LLM-judge results record evaluator prompt version and model, use a pass threshold of 0.80, and must be calibrated against at least 20 human-reviewed cases before they gate deployments. Investigate disagreements by case, category, and false-positive/false-negative impact; never permit a live provider in unit tests.
