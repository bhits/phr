package gov.samhsa.c2s.phr.infrastructure.dto;

import lombok.Data;

@Data
public class ValidationDiagnosticStatistics {
    private String diagnosticType;
    private int count;
}
