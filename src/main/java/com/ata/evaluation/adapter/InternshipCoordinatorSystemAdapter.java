package com.ata.evaluation.adapter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InternshipCoordinatorSystemAdapter extends HttpSystemAdapter {
    @Value("${app.adapters.internship-coordinator.base-url:http://localhost:8082}") private String url;
    public String systemId() { return "internship-coordinator"; }
    String baseUrl() { return url; }
    String endpoint() { return "/api/evaluations/execute"; }
}
