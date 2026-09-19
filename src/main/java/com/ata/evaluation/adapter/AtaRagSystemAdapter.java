package com.ata.evaluation.adapter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AtaRagSystemAdapter extends HttpSystemAdapter {
    @Value("${app.adapters.ata-rag.base-url:http://localhost:8081}") private String url;
    public String systemId() { return "ata-rag"; }
    String baseUrl() { return url; }
    String endpoint() { return "/api/chat"; }
}
