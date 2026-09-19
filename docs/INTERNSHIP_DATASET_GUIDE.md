# Internship Coordinator golden dataset v1

This immutable, synthetic 50-case dataset has IDs `internship-001` to `internship-050`. It models only facts needed for evaluation and explicitly marks every case `containsPii=false`; no application PDFs, names, addresses, emails, or existing-project content are copied.

It evenly covers valid, invalid, missing-document, ambiguous, incomplete, conflicting, extraction-error, and policy-edge outcomes. Expected fields provide labels for extraction accuracy, eligibility accuracy, missing-document accuracy, final recommendation, and confusion-matrix analysis.

False positives can approve an ineligible placement and create compliance risk. False negatives can delay an eligible student and therefore require a human clarification path rather than an automatic rejection. Publish a new version rather than editing v1 once it is used by an experiment.
