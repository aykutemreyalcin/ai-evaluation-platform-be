# API and extension contract (v1)

All endpoints are under `/api`, use JSON, and return ISO-8601 timestamps. Until persistence is added, endpoint implementations may use an in-memory repository but must keep these shapes stable.

## Core records

`EvaluationCase`: `{id, system, input, expectedOutput, metadata}`. `system` is `ata-rag` or `internship-coordinator`. `EvaluationResult`: `{evaluator, score, passed, category, reason, metadata}`. `EvaluationRun`: `{id, system, datasetVersion, applicationVersion, status, startedAt, completedAt, results, aggregateMetrics, langfuseTraceUrl}`.

## Endpoints

* `GET /systems` -> `[{id, displayName, status}]`
* `GET /datasets` -> `[{id, system, version, caseCount, immutable, createdAt}]`
* `GET /datasets/{id}` -> dataset metadata plus `cases`
* `POST /evaluations/run` body `{system,datasetVersion,applicationVersion,configuration}` -> `202 EvaluationRun`
* `GET /evaluations/{id}` -> `EvaluationRun`
* `GET /experiments` -> `[{id,system,datasetVersion,applicationVersion,status,aggregateMetrics,createdAt}]`
* `GET /experiments/{id}` -> experiment plus failed cases
* `GET /experiments/compare?baseline={id}&candidate={id}` -> `{baseline,candidate,metricComparisons,regressions,newFailures}`

## Evaluator SPI

`EvaluationContext` contains a case, actual application output, configuration, trace identifier, and timing/cost metadata. Every evaluator implements `EvaluationResult evaluate(EvaluationContext context)`. Evaluator names and versions must be stable and results must never expose secrets or raw PII.

## Frontend rules

The frontend must handle `202` run creation, `RUNNING|PASSED|FAILED` status, empty arrays, and API errors. It may use mock data matching this file before the backend endpoints are ready.
