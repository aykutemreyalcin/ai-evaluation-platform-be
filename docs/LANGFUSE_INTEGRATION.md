# Langfuse tracing

The backend exports one OpenTelemetry `evaluator` observation per evaluated case when `LANGFUSE_ENABLED=true`. It uses the supported OTLP HTTP endpoint (`/api/public/otel/v1/traces`) with Basic authentication and the `x-langfuse-ingestion-version: 4` header. It never uses Langfuse's legacy ingestion API.

Set `LANGFUSE_PUBLIC_KEY`, `LANGFUSE_SECRET_KEY`, and `LANGFUSE_BASE_URL` in a local git-ignored `.env.local` or GitHub Actions secrets. The trace records stable case/system identifiers, sanitized input/output, evaluator scores, dataset version, and latency. It intentionally masks values whose keys look like passwords, secrets, tokens, or API keys; synthetic evaluation data remains mandatory.

Use `development` locally and set `LANGFUSE_ENVIRONMENT` to `staging` or `production` when deployed. Open Langfuse Traces and filter by the stable `evaluate-case` name to inspect a failing case. The no-op gateway remains active when credentials are absent or tracing is disabled, so test suites never require a network call.
