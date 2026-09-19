package com.ata.evaluation.langfuse;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.langfuse")
public record LangfuseProperties(String publicKey, String secretKey, String baseUrl, boolean enabled, String environment) {
    public boolean configured() { return enabled && publicKey != null && !publicKey.isBlank() && secretKey != null && !secretKey.isBlank(); }
}
