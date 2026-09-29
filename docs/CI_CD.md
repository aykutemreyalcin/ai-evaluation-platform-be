# CI/CD evaluation gate

Every pull request that changes backend source, configuration, the Dockerfile, or workflow configuration runs the **Validate and evaluate** GitHub Actions workflow. A push to `main` runs the same checks and publishes a deployable JAR artifact.

The workflow has three required jobs:

1. **Backend tests and package** runs the complete Maven verification suite, publishes Surefire reports, and uploads the JAR artifact.
2. **Golden dataset regression gate** executes the immutable ATA RAG and Internship Coordinator golden datasets through deterministic adapters and compares the candidate metrics with a fixed baseline. A metric decline above the configured 2% allowance or a newly failed case fails the test and therefore blocks the PR check.
3. **Production container smoke test** builds the production Docker image, starts it, and requires `GET /api/health` to return successfully.

The CI gate intentionally does not call paid LLM providers or production systems. This keeps pull-request checks repeatable, secret-free, and safe for forks. Production tracing remains configured through runtime Langfuse environment variables; secrets are never validated by requiring them in CI.

Coolify deploys the approved `main` branch image to production. The health-check job mirrors the production container contract before that deployment is accepted.
