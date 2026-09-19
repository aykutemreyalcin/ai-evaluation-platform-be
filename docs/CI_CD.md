# CI/CD evaluation gate

The GitHub workflow runs the deterministic unit-test suite on every internal pull request and manual dispatch using Java 21. Add `LANGFUSE_PUBLIC_KEY` and `LANGFUSE_SECRET_KEY` as repository secrets before enabling hosted tracing. Forked pull requests do not receive repository secrets, so they run only the deterministic tests.

For a Langfuse-hosted experiment gate, add `langfuse/experiment-action` after the golden dataset has been uploaded and its immutable version is known. The action should target that exact dataset version and fail when the runner raises a regression error; do not run paid LLM judges on every untrusted fork.
