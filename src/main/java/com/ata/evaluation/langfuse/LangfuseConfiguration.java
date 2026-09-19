package com.ata.evaluation.langfuse;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LangfuseProperties.class)
class LangfuseConfiguration {}
