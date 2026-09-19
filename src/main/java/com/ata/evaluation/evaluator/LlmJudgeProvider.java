package com.ata.evaluation.evaluator;

import java.util.Map;

public interface LlmJudgeProvider { JudgeVerdict judge(String rubric, Map<String, Object> input); record JudgeVerdict(double score, String category, String reason, String model) {} }
