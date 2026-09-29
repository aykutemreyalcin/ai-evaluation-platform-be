package com.ata.evaluation;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.ata.evaluation.langfuse.LangfuseTraceGateway;
import com.ata.evaluation.langfuse.NoopLangfuseTraceGateway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "app.langfuse.enabled=false")
class AiEvaluationPlatformApplicationTest {
    @Autowired LangfuseTraceGateway traceGateway;

    @Test
    void starts_with_the_safe_noop_trace_gateway_when_langfuse_is_disabled() {
        assertInstanceOf(NoopLangfuseTraceGateway.class, traceGateway);
    }
}
