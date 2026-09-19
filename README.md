# AI Evaluation Platform - Backend

Shared Spring Boot API that evaluates ATA RAG and Internship Coordinator runs. It owns common evaluation contracts, dataset and experiment records, Langfuse tracing, regression gates, and the dashboard API.

## Local start

```bash
cp .env.example .env
mvn spring-boot:run
curl http://localhost:8080/api/health
```

Use Java 21 and Spring Boot 3.5, matching the existing `ata_rag_be` and `internship_application_coordinator_be` repositories. The delivery contract is in [docs/api-contract.md](docs/api-contract.md); do not change it without an explicit cross-team decision.

## Team boundaries

* Alvin: `src/main/resources/evaluation-datasets/`, `src/main/java/.../evaluator/`, evaluator tests.
* Aykut: `domain/`, `engine/`, `adapter/`, `langfuse/`, `experiment/`, REST controllers and CI.
* Nizamettin: works in the frontend repository only.

All production datasets must be synthetic or anonymized. Keep credentials in `.env`, never in source control.
