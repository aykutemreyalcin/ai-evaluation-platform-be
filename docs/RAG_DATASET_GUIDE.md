# ATA RAG golden dataset v1

`RagGoldenDatasetV1` is an immutable, deterministic 100-case dataset with stable IDs `rag-001` to `rag-100`. It is synthetic by design and contains no source dumps, personal data, or generated answers from a live model.

The cases cycle evenly across factual, multi-source, ambiguous, unanswerable, misleading-assumption, and citation categories. `expectedAnswer` is a review target, `acceptedSourceIds` supports citation evaluation, and `answerability=false` means refusal/no-answer behavior is expected. Create `ata-rag-golden-v2` rather than changing this class after it has been used in an experiment.

The current questions are schema-and-behavior golden cases. Before a production gate, replace only the synthetic policy wording in a new version with reviewed, public ATA facts and stable website source IDs.
